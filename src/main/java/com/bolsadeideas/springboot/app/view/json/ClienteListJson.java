package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@Component("clientes.json")
public class ClienteListJson extends MappingJackson2JsonView {

	@Override
	protected Object filterModel(Map<String, Object> model) {
		
		model.remove("titulo");
		model.remove("page");
		@SuppressWarnings("unchecked")
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		ClienteList listCliente = new ClienteList(clientes.getContent());
		model.put("clientes", listCliente);
		return super.filterModel(model);
	}

}
