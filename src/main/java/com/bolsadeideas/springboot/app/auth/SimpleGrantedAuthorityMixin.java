package com.bolsadeideas.springboot.app.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGrantedAuthorityMixin {

	@JsonCreator
	public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {

	}

}
