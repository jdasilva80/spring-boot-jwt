package com.bolsadeideas.springboot.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Bean
	public BCryptPasswordEncoder encoder() {

		return new BCryptPasswordEncoder();
	}
	// private Logger log = LoggerFactory.getLogger(getClass());

	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/error_403").setViewName("error_403");

	}

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//		WebMvcConfigurer.super.addResourceHandlers(registry);
//
//		String absolutePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//		log.info("absolutePath " + absolutePath);
//		// para ruta externa al proyecto:
//		// registry.addResourceHandler("/uploads/**").addResourceLocations("file:/C:/temp/uploads/");
//		registry.addResourceHandler("/uploads/**").addResourceLocations(absolutePath);
//
//	}

	@Bean
	public LocaleResolver localeResolver() {

		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.getDefault());

		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {

		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");

		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

		marshaller.setClassesToBeBound(new Class[] { com.bolsadeideas.springboot.app.view.xml.ClienteList.class });

		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ hola" +marshaller.getContextPath());
		return marshaller;
	}

}
