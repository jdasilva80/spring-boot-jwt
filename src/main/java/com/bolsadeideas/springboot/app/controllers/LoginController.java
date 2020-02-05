package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flashAttributes) {

		if (principal != null) {

			flashAttributes.addFlashAttribute("info", "ya había iniciado sesión.");
			return "redirect:/";
		}
		if (error != null) {
			model.addAttribute("error", "El inicio de sesión no es válido.");
		}
		if (logout != null) {
			model.addAttribute("success", "Se ha cerrado la sesión.");
		}

		return "login";
	}
}
