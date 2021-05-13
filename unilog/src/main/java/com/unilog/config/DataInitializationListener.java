package com.unilog.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.unilog.entity.User;
import com.unilog.reporitory.UserRepository;

@Component
public class DataInitializationListener {
	@Bean
	BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private BCryptPasswordEncoder encoder;

	@EventListener
	public void populateIntialData(ContextRefreshedEvent event) {

		List<User> users = userRepository.findAll();
		

		if (users.size() == 0) {
			User loginUser = new User();
			loginUser.setPassword(encoder.encode("prasanna"));
			loginUser.setName("PrasannaKumar");
			loginUser.setEmail("prasanna@gmail.com");
			loginUser.setActive(true);
			userRepository.saveAndFlush(loginUser);

		}

	}

}
