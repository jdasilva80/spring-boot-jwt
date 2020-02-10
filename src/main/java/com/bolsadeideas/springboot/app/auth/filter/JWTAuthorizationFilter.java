package com.bolsadeideas.springboot.app.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bolsadeideas.springboot.app.auth.service.JWTService;
import com.bolsadeideas.springboot.app.auth.service.JWTServiceImpl;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTService jwtservice;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtservice) {

		super(authenticationManager);
		this.jwtservice = jwtservice;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader(JWTServiceImpl.HEADER_AUTHORITATION);

		if (!jwtservice.requiresAuthoritation(header)) {

			chain.doFilter(request, response);
			return;

		} else {

			UsernamePasswordAuthenticationToken authentication = null;

			if (jwtservice.validate(header)) {

				authentication = new UsernamePasswordAuthenticationToken(jwtservice.getClaims(header).getSubject(),
						null, jwtservice.getRoles(header));
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}
	}

}
