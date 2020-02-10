package com.bolsadeideas.springboot.app.auth.service;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTServiceImpl implements JWTService {

	public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_AUTHORITATION = "Authorization";
	public static final String CLAIMS_AUTHORITIES = "authorities";
	public static final Long EXPIRATION_DATE = 360000L;

	@Override
	public String create(Authentication auth) throws IOException {

		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

		Claims claims = Jwts.claims();
		claims.put(CLAIMS_AUTHORITIES, new ObjectMapper().writeValueAsString(roles));

		String token = Jwts.builder().setClaims(claims).setSubject(auth.getName()).signWith(SECRET_KEY)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE)).compact();

		return token;
	}

	@Override
	public boolean validate(String token) {

		try {
			getClaims(token);
			return true;

		} catch (JwtException | IllegalArgumentException e) {

			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {

		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(resolve(token)).getBody();

		return claims;

	}

	@Override
	public String getUserName(String token) {

		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {

		Object roles = getClaims(token).get(CLAIMS_AUTHORITIES);

		Collection<? extends GrantedAuthority> authorities =

				Arrays.asList(new ObjectMapper()
						.addMixIn(SimpleGrantedAuthority.class,
								com.bolsadeideas.springboot.app.auth.SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

		return authorities;
	}

	@Override
	public String resolve(String token) {

		if (requiresAuthoritation(token)) {
			return token.replace(TOKEN_PREFIX, "");
		} else
			return null;
	}

	@Override
	public boolean requiresAuthoritation(String token) {

		if (token == null || !token.startsWith(TOKEN_PREFIX)) {
			return false;
		}
		return true;
	}

}
