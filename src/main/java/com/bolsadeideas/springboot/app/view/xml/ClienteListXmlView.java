package com.bolsadeideas.springboot.app.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("clientes.xml")
public class ClienteListXmlView extends MarshallingView {

	@Autowired
	public ClienteListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		model.remove("titulo");
		model.remove("page");
		@SuppressWarnings("unchecked")
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		ClienteList listCliente = new ClienteList(clientes.getContent());
		model.put("clientes", listCliente);

		super.renderMergedOutputModel(model, request, response);
	}

}
