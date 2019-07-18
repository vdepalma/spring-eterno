package com.vdepalma.springboot.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vdepalma.springboot.app.models.entity.Cliente;
import com.vdepalma.springboot.app.models.entity.Factura;
import com.vdepalma.springboot.app.models.entity.ItemFactura;
import com.vdepalma.springboot.app.models.entity.Producto;
import com.vdepalma.springboot.app.models.services.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	IClienteService clienteService;

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {

		Cliente cliente = clienteService.findById(clienteId);
		if (cliente == null) {
			flash.addFlashAttribute("danger", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);

		model.put("factura", factura);
		model.put("titulo", "Nueva Factura");

		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		return clienteService.findByName(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}

		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("danger", "Error: la factura debe tener al menos un producto");
			return "factura/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura item = new ItemFactura(producto, cantidad[i]);
			factura.addItemFactura(item);
			log.info("Agregando ITEM: " + itemId[i] + " -> Cantidad:  " + cantidad[i]);
		}

		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada con éxito!");
		return "redirect:/ver/" + factura.getCliente().getId();
	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Factura factura = clienteService.fetchByIDWithClienteWithItemFacturaWithProducto(id);
		if (Objects.isNull(factura)) {
			flash.addFlashAttribute("danger", "La factura no existe en la base de datos");
		} else {
			model.addAttribute("factura", factura);
			model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion()));

		}
		return "factura/ver";
	}
	
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		Factura factura= clienteService.findFacturaById(id);
		if(factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con éxito! ");
			return "redirect:/ver/"+ factura.getCliente().getId();
		}else {
			flash.addFlashAttribute("error", "No se encontro la factura a eliminar.");
			return "redirect:/listar";
		}
		
		
		
	}
	
}
