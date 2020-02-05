package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.services.IClienteService;
import com.bolsadeideas.springboot.app.services.IUploadService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadService uploadService;

	@Autowired
	private MessageSource message;

	@Secured("ROLE_USER")
	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable(name = "id") Long id, Map<String, Object> model,
			RedirectAttributes flashAttribures) {

		Cliente cliente = clienteService.fetchByIdWithFacturas(id);// clienteService.findOne(id);

		if (cliente == null) {

			flashAttribures.addFlashAttribute("error", "El cliente no existe.");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "El detalle del cliente : " + cliente.getNombre());

		return "ver";
	}

	@GetMapping(value = "/listar-rest")
	public @ResponseBody ClienteList listarRest() {

//		List<Cliente> clientes = StreamSupport.stream(clienteService.findAll().spliterator(), false)
//				.collect(Collectors.toList());
		ClienteList clientes = new ClienteList(
				StreamSupport.stream(clienteService.findAll().spliterator(), false).collect(Collectors.toList()));
		return clientes;
	}

	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int numPage, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale) {

		if (authentication != null) {

			logger.info("el usuario logueado es: ".concat(authentication.getName()));
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {

			logger.info("SecurityContextHolder.getContext().getAuthentication(), el usuario logueado es: "
					.concat(auth.getName()));
		}

		if (hasRole("ROLE_ADMIN")) {

			logger.info("Hola ".concat(authentication.getName()).concat(" tienes Acceso!"));
		} else if (authentication != null) {
			logger.info("Hola ".concat(authentication.getName()).concat(" NO tienes Acceso!"));
		}

		SecurityContextHolderAwareRequestWrapper wrapperRequest = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");

		if (wrapperRequest.isUserInRole("ADMIN")) {

			logger.info("SecurityContextHolderAwareRequestWrapper--> Hola ".concat(authentication.getName())
					.concat(" tienes Acceso!"));
		} else if (authentication != null) {
			logger.info("SecurityContextHolderAwareRequestWrapper--> Hola ".concat(authentication.getName())
					.concat(" NO tienes Acceso!"));
		}

		if (request.isUserInRole("ADMIN")) {

			logger.info("HttpServletRequest --> Hola ".concat(authentication.getName()).concat(" tienes Acceso!"));
		} else if (authentication != null) {
			logger.info("HttpServletRequest --> Hola ".concat(authentication.getName()).concat(" NO tienes Acceso!"));
		}

		Pageable pag = PageRequest.of(numPage, 2);
		Page<Cliente> clientes = clienteService.findAll(pag);
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		model.addAttribute("titulo", message.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("page", pageRender);
		model.addAttribute("clientes", clientes);

		return "clientes";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String form(Map<String, Object> model) {

		model.put("titulo", "Formulario de cliente");
		model.put("cliente", new Cliente());

		return "form";
	}

	@Secured("ROLE_USER")
	@RequestMapping("/uploads/{fileName:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable("fileName") String fileName) {

		Resource recurso = null;

		try {
			recurso = uploadService.load(fileName);
		} catch (MalformedURLException e) {

		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile file, SessionStatus status, RedirectAttributes flashAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente");
			return "form";
		}
		// en recursos estaticos del proyecto: Path directorioRecursos =
		// Paths.get("src//main//resources//static//uploads");
		// en recursos estaticos del proyecto: String rutaRecursos =
		// directorioRecursos.toFile().getAbsolutePath();

		// ruta externa al proyecta: String rutaRecursos = "C://temp//uploads";

		if (!file.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				uploadService.delete(cliente.getFoto());

			}

			String uniqueNameFile = null;

			try {
				uniqueNameFile = uploadService.copy(file);
			} catch (IOException e) {

			}
			flashAttributes.addFlashAttribute("info", "has subido el archivo " + uniqueNameFile + " con éxito");
			cliente.setFoto(uniqueNameFile);
		}
		String msg = cliente.getId() != null ? "El cliente se ha editado con éxito."
				: "El cliente se ha guardado con éxito.";

		clienteService.save(cliente);
		flashAttributes.addFlashAttribute("success", msg);
		status.setComplete();

		return "redirect:/listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/editar/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flashAttributes) {

		Cliente cliente = null;
		if (id != null && id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flashAttributes.addFlashAttribute("error", "El cliente no existe.");
				return "redirect:/listar";
			}
		} else {
			flashAttributes.addFlashAttribute("error", "El id de cliente no es válido.");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Editar cliente");
		model.addAttribute("cliente", cliente);

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(name = "id") Long id, RedirectAttributes flashAttributes) {

		if (id != null && id > 0) {

			Cliente cliente = clienteService.findOne(id);
			if (cliente == null) {
				flashAttributes.addFlashAttribute("error", "El cliente no existe.");
				return "redirect:/listar";
			}

			if (uploadService.delete(cliente.getFoto())) {

				flashAttributes.addFlashAttribute("info",
						"Se ha eliminado la foto " + cliente.getFoto() + " con éxito.");
			}
			clienteService.delete(id);
			flashAttributes.addFlashAttribute("success", "El cliente se ha eliminado con exito.");

		} else {
			flashAttributes.addFlashAttribute("error", "El id de cliente no es válido.");
		}
		return "redirect:/listar";
	}

	public boolean hasRole(String role) {

		SecurityContext securityContext = SecurityContextHolder.getContext();

		if (securityContext == null) {

			return false;
		}

		Authentication auth = securityContext.getAuthentication();

		if (auth == null) {

			return false;
		}

		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

		for (GrantedAuthority authority : roles) {

			if (authority.getAuthority().equals(role)) {

				logger.info("Hola usuario ".concat(auth.getName()).concat(", tienes el rol: ")
						.concat(authority.getAuthority()));

				return true;
			}
		}
		return false;
	}

}
