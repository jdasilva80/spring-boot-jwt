package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Repository("clienteDaoJpa")
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {

		return em.find(Cliente.class, id);
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {

		if (cliente.getId() == null) {
			em.persist(cliente);
		} else {
			em.merge(cliente);
		}
	}

	@Override
	@Transactional
	public void delete(Long id) {

		em.remove(findOne(id));

	}


}
