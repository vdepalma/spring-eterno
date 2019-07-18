package com.vdepalma.springboot.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vdepalma.springboot.app.models.dao.IFacturaDao;
import com.vdepalma.springboot.app.models.dao.IClienteDao;
import com.vdepalma.springboot.app.models.dao.IProductoDao;
import com.vdepalma.springboot.app.models.entity.Cliente;
import com.vdepalma.springboot.app.models.entity.Factura;
import com.vdepalma.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {
	@Autowired
	private IClienteDao clienteDao;

	@Autowired
	private IProductoDao productoDao;

	@Autowired
	private IFacturaDao facturaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void saveOrUpdate(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	public List<Producto> findByName(String term) {
		return productoDao.findByName(term);
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
	}

	@Override
	@Transactional(readOnly=true)
	public Producto findProductoById(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Factura findFacturaById(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Factura fetchByIDWithClienteWithItemFacturaWithProducto(Long id) {
		return facturaDao.fetchByIDWithClienteWithItemFacturaWithProducto(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente fetchClienteByIdWithFacturas(Long id) {
		return clienteDao.fetchClienteByIdWithFacturas(id);
	}

}
