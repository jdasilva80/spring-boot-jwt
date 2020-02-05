package com.bolsadeideas.springboot.app.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClienteSpringDataDao;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.dao.IProductoSpringDataDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

//	@Autowired
//	@Qualifier("clienteDaoJpa")
//	private IClienteDao clienteDao;

	@Autowired
	private IProductoSpringDataDao productoDao;

	@Autowired
	private IClienteSpringDataDao clienteSpringDataDao;

	@Autowired
	private IFacturaDao facturaDao;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	@Transactional(readOnly = true)
	public Iterable<Cliente> findAll() {

		return clienteSpringDataDao.findAll();
	}

	@Override
	public Page<Cliente> findAll(Pageable pageable) {

		return clienteSpringDataDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {

		return clienteSpringDataDao.findById(id).orElseGet(null);
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {

		clienteSpringDataDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {

		clienteSpringDataDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductos(String term) {

		List<Producto> productos = productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
		log.info("lista de productos service : " + productos);
		return productos;

	}

	@Override
	public List<Producto> findByNombre(String term) {

		List<Producto> productos = productoDao.findByNombre(term);
		log.info("lista de productos service : " + productos);
		return productos;
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {

		facturaDao.save(factura);

	}

	@Override
	public Producto findProductoById(Long id) {

		return productoDao.findById(id).orElseGet(null);

	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {

		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {

		facturaDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchByIdWithClientWithItemsWithProducto(Long id) {

		return facturaDao.fetchByIdWithClientWithItemsWithProducto(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {

		return clienteSpringDataDao.fetchByIdWithFacturas(id);
	}

}
