package com.bolsadeideas.springboot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.services.IUploadService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	IUploadService uploadService;

	@Autowired
	BCryptPasswordEncoder bcryptEnconder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		uploadService.deleteAll();
		uploadService.init();

		for (int i = 0; i < 2; i++) {

			String pswEncrypted = bcryptEnconder.encode("12345");

			log.info("pswEncrypted: ".concat(pswEncrypted));
		}

	}

}
