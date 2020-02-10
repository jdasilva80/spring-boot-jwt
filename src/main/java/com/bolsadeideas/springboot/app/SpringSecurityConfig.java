package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.auth.filter.JWTAuthenticationFilter;
import com.bolsadeideas.springboot.app.auth.filter.JWTAuthorizationFilter;
import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.auth.service.JWTService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	LoginSuccesHandler LoginSuccesHandler;

	@Autowired
	DataSource ds;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JWTService jwtService;

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder, BCryptPasswordEncoder encoder) throws Exception {

//		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
//		builder.inMemoryAuthentication().withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
//				.withUser(users.username("jose").password("12345").roles("USER"));

//		builder.jdbcAuthentication().dataSource(ds).passwordEncoder(encoder).usersByUsernameQuery(""
//				+ "select username, password, enabled from users where username =?")
//		          .authoritiesByUsernameQuery(""
//						+ "select u.username, a.authority from authorities a "
//						+ " inner join users u on (a.user_id =  u.id)"
//						+ " where u.username=?");

		builder.userDetailsService(userDetailsService).passwordEncoder(encoder);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/listar**", "/", "/css/**", "/js/**", "/images/**", "/locale").permitAll()
				/*
				 * .antMatchers("/ver/**").hasAnyRole("USER").antMatchers("/eliminar/**").
				 * hasAnyRole("ADMIN")
				 * .antMatchers("/form/**").hasAnyRole("ADMIN").antMatchers("/editar/**").
				 * hasAnyRole("ADMIN")
				 * .antMatchers("/factura/**").hasAnyRole("USER").antMatchers("/uploads/**").
				 * hasAnyRole("USER")
				 */
				.anyRequest().authenticated().and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtService))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService))
//     			.and().formLogin().successHandler(LoginSuccesHandler).loginPage("/login")
//				.permitAll().and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/error_403")
				.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}
