package com.vdepalma.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vdepalma.springboot.app.models.entity.Cliente;
import com.vdepalma.springboot.app.models.services.IClienteService;
import com.vdepalma.springboot.app.models.services.IUploadFileService;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private IClienteService clienteService;

	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Nuevo Cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, SessionStatus status, RedirectAttributes flash) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Nuevo Cliente");
			return "form";
		}
		if (!foto.isEmpty()) {
			if (cliente.getId() != null && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}

			String uniqueFilename = uploadFileService.copy(foto);
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
			cliente.setFoto(uniqueFilename);

		}
		clienteService.saveOrUpdate(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente actualizado con exito!");
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findById(id);
		} else {
			return "redirect:listar";
		}
		model.addAttribute("titulo", "Editar Cliente");
		model.addAttribute("cliente", cliente);
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findById(id);
			uploadFileService.delete(cliente.getFoto());
			clienteService.delete(id);

		}
		flash.addFlashAttribute("success", "Cliente eliminado con exito!");
		return "redirect:/listar";
	}


	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id);
		if (cliente == null) {
			flash.addFlashAttribute("danger", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		} else {
			model.put("cliente", cliente);
			model.put("titulo", "Detalle de cliente: " + cliente.toString());

			return "ver";

		}
	}



}
