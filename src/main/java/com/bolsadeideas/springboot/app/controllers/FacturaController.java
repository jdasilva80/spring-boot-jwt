package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.services.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model model, RedirectAttributes flashAttributes) {

		Factura factura = clienteService.fetchByIdWithClientWithItemsWithProducto(id);// clienteService.findFacturaById(id);

		if (factura == null) {

			flashAttributes.addFlashAttribute("error", "La factura no existe.");
			return "redirect:/listar";
		}

		model.addAttribute("titulo", "Factura ".concat(factura.getId().toString()));
		model.addAttribute("factura", factura);

		return "factura/ver";
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable("clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flashAttributes) {

		Cliente cliente = clienteService.findOne(clienteId);

		if (cliente == null) {

			flashAttributes.addFlashAttribute("error", "El cliente no existe");
			return "redirect:/listar";
		}

		Factura factura = new Factura();
		factura.setCliente(cliente);
		model.put("factura", factura);
		model.put("titulo", "Crear factura");

		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable("term") String term) {

		return clienteService.findByNombre(term);
	}

	@PostMapping(value = "/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(value = "item_id[]", required = false) Long[] itemId,
			@RequestParam(value = "cantidad[]", required = false) Integer[] cantidad,
			RedirectAttributes flashAttributes, SessionStatus sessionStatus) {

		if (result.hasErrors()) {

			model.addAttribute("titulo", "Crear factura");
			return "factura/form";
		}

		if (itemId == null || itemId.length == 0) {

			model.addAttribute("titulo", "Crear factura");
			model.addAttribute("error", "Error: Debe añadir productos a la factura");
			return "factura/form";
		}

		for (int i = 0; i < itemId.length; i++) {

			Producto producto = clienteService.findProductoById(itemId[i]);
			ItemFactura itemFactura = new ItemFactura();
			itemFactura.setCantidad(cantidad[i]);
			itemFactura.setProducto(producto);
			factura.addItemFactura(itemFactura);

			log.info("Id : " + itemId[i] + " ,cantidad : " + cantidad[i]);
		}

		clienteService.saveFactura(factura);
		sessionStatus.setComplete();
		flashAttributes.addFlashAttribute("success", "la factura se ha guardado con éxito");

		return "redirect:/ver/" + factura.getCliente().getId();

	}

	@GetMapping("/eliminar/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes flashAttributes) {

		Factura factura = clienteService.findFacturaById(id);

		if (factura != null) {

			clienteService.deleteFactura(id);
			flashAttributes.addFlashAttribute("success", "Se ha eliminado la fatura: " + factura.getId());

			return "redirect:/ver/" + factura.getCliente().getId();

		}
		flashAttributes.addFlashAttribute("error", "la factura no existe");
		return "redirect:/listar";
	}

}
