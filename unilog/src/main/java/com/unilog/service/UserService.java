package com.unilog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unilog.entity.Book;
import com.unilog.entity.User;
import com.unilog.reporitory.BookRepository;
import com.unilog.reporitory.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public User findByEmailAndPassword(String email, String password) throws Exception {

		User userResponse = null;

		try {
			userResponse = userRepository.findByEmail(email);
			if (userResponse != null) {
				boolean passwordmatch = encoder.matches(password, userResponse.getPassword());
				if (passwordmatch == false && !userResponse.isActive()) {
					throw new BadCredentialsException("No User found for the given Email and Password");
				} else if (passwordmatch == false && userResponse.isActive()) {
					throw new BadCredentialsException("No User found for the given Email and Password");
				} else if (passwordmatch == true && !userResponse.isActive()) {
					throw new BadCredentialsException("No User found for the given Email and Password");
				}
			} else {
				throw new BadCredentialsException("No User found for the given Email and Password");
			}

		} catch (BadCredentialsException e) {

			throw new Exception("INVALID_CREDENTIALS", e);
		}
		return userResponse;
	}

	public List<User> getAllUser() {
		return userRepository.findAll();

	}

	// Find by user id
	public User findByUserId(Integer id) {
		User userResponse = null;
		try {
			userResponse = userRepository.findUserByUserId(id);
			if (userResponse == null)
				throw new RuntimeException("User Id is Not found in Database");
		} catch (Exception e) {
		}
		return userResponse;
	}

	// Delete the user by Id
	public void deleteById(Integer id) {
		User userResponse = findByUserId(id);

		if (userResponse != null) {
			userRepository.delete(userResponse);
		} else {

			throw new RuntimeException("Some problems occurred during deleting User ");
		}

	}

	// Updating the user by Id
	public User updateUser(User userDetails, Integer id) {

		User userResponse = null;
		if (userDetails != null && id != null) {
			User userById = findByUserId(id);
			if (userDetails.getBook() != null) {
				for (Book book : userDetails.getBook()) {
					Book bookById = bookRepository.findBookById(book.getId());
					ArrayList<Book> books = new ArrayList<Book>();
					books.add(bookById);
					userById.setBook(books);
				}
			}

			if (userDetails.getPassword() != null) {
				if (encoder.encode(userDetails.getPassword()) != userById.getPassword()) {
					userById.setPassword(encoder.encode(userDetails.getPassword()));
				}
			}

			userById.setEmail(userDetails.getEmail());
			userById.setName(userDetails.getName());

			userResponse = userRepository.save(userById);
		} else {

			throw new RuntimeException("Something missed in Updating the User");
		}
		return userResponse;

	}

	// Partial update
	public User updateUserById(User user, Integer id) {

		final User existing = userRepository.findUserByUserId(id);
		if (existing == null) {
			throw new RuntimeException(String.format("No User found for the given id: %s", id));
		}

		if (user.getEmail() != null) {
			existing.setEmail(user.getEmail());

		}
		if (user.getName() != null) {
			existing.setName(user.getName());
		}

		if (user.getBook().size() != 0) {
			for (Book book : user.getBook()) {

				Book bookFromDB = bookRepository.findBookById(book.getId());
				ArrayList<Book> books = new ArrayList<>();
				books.add(bookFromDB);
				existing.setBook(books);
			}
		}

		return userRepository.save(existing);
	}

	public Map<String, Object> createUser(User user) {
		Map<String, Object> createUserResponse = new HashMap<String, Object>();
		if (user.getEmail() != null && !(user.getEmail().isEmpty())) {
			User findByEmail = userRepository.findByEmail(user.getEmail());
			if (findByEmail == null) {
				User userresponse = new User();
				if (user.getEmail() != null && user.getPassword() != null) {
					userresponse.setPassword(encoder.encode(user.getPassword()));
					userresponse.setName(user.getName());
					userresponse.setEmail(user.getEmail());
					userresponse.setActive(true);
					userRepository.save(userresponse);
					createUserResponse.put("status", HttpStatus.CREATED);
				} else {
					createUserResponse.put("status", HttpStatus.OK);
					createUserResponse.put("message", "Email & password are Mandatory.");
				}

			} else {
				createUserResponse.put("status", HttpStatus.OK);
				createUserResponse.put("message", "Email Already Existed. Please Login");

			}
		} else {
			createUserResponse.put("status", HttpStatus.OK);
			createUserResponse.put("message", "No User is not created for the given Email");

		}
		return createUserResponse;
	}

	public User findByEmail(String email) {
		User userFromDB = userRepository.findByEmail(email);
		return userFromDB;
	}

}
