package com.bolsadeideas.springboot.app.auth.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGrantedAuthorityMixin {

	@JsonCreator
	public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {

	}

}
