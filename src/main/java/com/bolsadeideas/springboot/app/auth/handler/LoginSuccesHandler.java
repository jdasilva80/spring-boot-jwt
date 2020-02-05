package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccesHandler extends SimpleUrlAuthenticationSuccessHandler {

	private SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
	private FlashMap flashMap = new FlashMap();

	@Autowired
	public void prueba(LocaleResolver localeResolver) {

		flashMap.put("info", "Hola");
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		if (authentication != null) {

			logger.info("el usuario logueado es: ".concat(authentication.getName()));
		}

		flashMap.put("success", "Te has logueado con éxito!");

		flashMapManager.saveOutputFlashMap(flashMap, request, response);

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
