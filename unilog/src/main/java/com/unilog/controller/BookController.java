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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unilog.entity.Book;
import com.unilog.service.BookService;

@RestController
public class BookController {
	@Autowired
	private BookService bookService;

	// Create Book
	@PostMapping(value = "/books", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> createBook(@RequestBody Book book) {

		Map<String, Object> createBookResponse = bookService.createBook(book);

		return new ResponseEntity<>(createBookResponse, HttpStatus.CREATED);

	}

	// Getting all Books
	@GetMapping(path = "/books/{belongsTo}")
	public HttpEntity<List<Book>> getAllBooks(@PathVariable(value = "belongsTo") String belongsTo) {
		List<Book> bookResponse = bookService.getAllBooks(belongsTo);
		return new ResponseEntity<>(bookResponse, new HttpHeaders(), HttpStatus.OK);
	}

//	@GetMapping(value = "/books/{id}")
//	public HttpEntity<Book> findByBookId(@PathVariable(value = "id") Integer id) {
//		Book bookResponse = bookService.findByBookId(id);
//		if (bookResponse == null) {
//			throw new RuntimeException(String.format("No Book found for the given id: %s", id));
//		}
//
//		return new ResponseEntity<>(bookResponse, HttpStatus.OK);
//	}

	// Deleting the Book
	@DeleteMapping(path = "/books/{belongsTo}/{id}")
	public HttpEntity<Book> deleteByBookId(@PathVariable(value = "belongsTo") String belongsTo,
			@PathVariable(value = "id") Integer id) {
		Book bookResponse = bookService.findByBookIdAnBelongsTo(belongsTo, id);
		if (bookResponse == null) {
			throw new RuntimeException(String.format("No User found for the given id: %s", id));
		}
		bookService.deleteByBelongsToAndId(belongsTo, id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	// Update a Books
	@PutMapping(path = "/books/{belongsTo}/{id}")
	public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable(value = "belongsTo") String belongsTo,
			@PathVariable("id") Integer id) {
		Book bookFromDB = bookService.findByBookIdAnBelongsTo(belongsTo, id);
		if (bookFromDB == null) {
			throw new RuntimeException(String.format("No Book found for the given id: %s", id));
		}
		Book bookResponse = bookService.updateBook(book, id);
		return new ResponseEntity<Book>(bookResponse, HttpStatus.OK);
	}

}
