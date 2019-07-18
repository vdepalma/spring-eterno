package com.vdepalma.springboot.app.controllers;

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
			Model model, 
			Principal principal,
			RedirectAttributes flash,
			@RequestParam(value = "logout", required = false) String logout) {
		if (principal != null) {
			flash.addFlashAttribute("info", "Ya existe una sesión iniciada.");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("danger", "Usuario o contraseña inválidos");
		}
		if(logout != null) {
			model.addAttribute("info", "Has salido.");
		}
		return "login";
	}

}
