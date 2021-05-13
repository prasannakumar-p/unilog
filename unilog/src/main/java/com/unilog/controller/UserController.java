package com.unilog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unilog.entity.User;
import com.unilog.service.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userService;

	// Creating the user
	@PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {

		Map<String, Object> createUserResponse = userService.createUser(user);

		return new ResponseEntity<>(createUserResponse, HttpStatus.CREATED);

	}

	// Getting all users
	@GetMapping(path = "/users")
	public HttpEntity<List<User>> getAllUser() {
		List<User> userResponse = userService.getAllUser();
		return new ResponseEntity<>(userResponse, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping(value = "/users/{id}")
	public HttpEntity<User> findByUserId(@PathVariable(value = "id") Integer id) {
		User userById = userService.findByUserId(id);
		if (userById == null) {
			throw new RuntimeException(String.format("No User found for the given id: %s", id));
		}

		return new ResponseEntity<>(userById, HttpStatus.OK);
	}

	// Deleting the user
	@DeleteMapping(path = "/users/{id}")
	public HttpEntity<User> deleteByUserId(@PathVariable(value = "id") Integer id) {
		User userById = userService.findByUserId(id);
		if (userById == null) {
			throw new RuntimeException(String.format("No User found for the given id: %s", id));
		}
		userService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	// Update a Users
	@PutMapping(path = "/users/{id}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") Integer id) {

		User userFromDB = userService.findByUserId(id);
		if (userFromDB == null) {
			throw new RuntimeException(String.format("No User found for the given id: %s", id));
		}
		User userResponse = userService.updateUser(user, id);
		return new ResponseEntity<User>(userResponse, HttpStatus.OK);
	}

	// Partial update
	@PatchMapping("/users/{id}")
	public HttpEntity<User> partialUpdate(@RequestBody User user, @PathVariable("id") Integer id) {
		User userResponse = userService.updateUserById(user, id);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

}
