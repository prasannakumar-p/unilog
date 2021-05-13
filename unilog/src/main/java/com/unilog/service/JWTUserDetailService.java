package com.unilog.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unilog.reporitory.UserRepository;


@Service
public class JWTUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		com.unilog.entity.User loginUser = userRepository.findByEmail(email);

		if (loginUser.getEmail().equalsIgnoreCase(email) && loginUser.isActive()) {
			return new User(loginUser.getEmail(), loginUser.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with Email: " + email);
		}

	}

}
