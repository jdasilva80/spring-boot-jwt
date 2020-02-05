package com.bolsadeideas.springboot.app.controllers;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.services.IClienteService;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/listar")
	public ClienteList listarRest() {

//		List<Cliente> clientes = StreamSupport.stream(clienteService.findAll().spliterator(), false)
//				.collect(Collectors.toList());
		ClienteList clientes = new ClienteList(
				StreamSupport.stream(clienteService.findAll().spliterator(), false).collect(Collectors.toList()));
		return clientes;
	}

}
