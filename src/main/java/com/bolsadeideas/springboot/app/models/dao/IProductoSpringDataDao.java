package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoSpringDataDao extends CrudRepository<Producto, Long> {

	public List<Producto> findByNombreLikeIgnoreCase(String term);

	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);

}
