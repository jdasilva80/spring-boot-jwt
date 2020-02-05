package com.bolsadeideas.springboot.app.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	IUsuarioDao usuarioDao;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByUsername(username);

		if (usuario == null) {

			log.info("el usuario " + username + " no esiste!");
			throw new UsernameNotFoundException("el usuario " + username + " no esiste!");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		for (Role role : usuario.getRoles()) {

			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));

		}

		if (authorities.isEmpty()) {

			log.info("el usuario " + username + " no tiene autoridades!");
			throw new UsernameNotFoundException("el usuario " + username + " no tiene autoridades!");
		}

		log.info("el usuario " + username + " existe!");
		return new User(username, usuario.getPassword(), authorities);
	}

}
