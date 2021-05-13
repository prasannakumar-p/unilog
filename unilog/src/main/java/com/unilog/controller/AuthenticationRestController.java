package com.unilog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unilog.entity.User;
import com.unilog.model.AuthenticationRequest;
import com.unilog.model.AuthenticationResponse;
import com.unilog.service.UserService;
import com.unilog.util.TokenUtil;



@RestController
public class AuthenticationRestController {

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private UserService usersService;


	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		String token = null;
		AuthenticationResponse response = new AuthenticationResponse();
		User user = usersService.findByEmailAndPassword(authenticationRequest.getEmail(),authenticationRequest.getPassword());
		if (user != null) {
			token = tokenUtil.generateToken(user);
			response.setJwttoken(token);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}