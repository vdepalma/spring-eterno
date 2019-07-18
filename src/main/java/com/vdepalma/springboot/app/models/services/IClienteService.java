package com.vdepalma.springboot.app.models.services;

import java.util.List;

import com.vdepalma.springboot.app.models.entity.Cliente;
import com.vdepalma.springboot.app.models.entity.Factura;
import com.vdepalma.springboot.app.models.entity.Producto;

public interface IClienteService {
	
	List<Cliente> findAll();

	Cliente findById(Long id);

	void saveOrUpdate(Cliente cliente);

	void delete(Long id);

	public List<Producto> findByName(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	
	public Factura fetchByIDWithClienteWithItemFacturaWithProducto(Long id);
	
	public Cliente fetchClienteByIdWithFacturas(Long id);
}
