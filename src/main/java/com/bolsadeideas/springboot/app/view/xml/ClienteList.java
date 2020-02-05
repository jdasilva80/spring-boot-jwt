package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@XmlRootElement(name = "clientess")
public class ClienteList {

	@XmlElement(name = "cliente")
	public List<Cliente> clientesss;

	public ClienteList() {
	}

	public ClienteList(List<Cliente> clientes) {
		this.clientesss = clientes;
	}

	public List<Cliente> getClientes() {
		return clientesss;
	}

}
