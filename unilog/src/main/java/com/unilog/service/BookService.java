package com.unilog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.unilog.entity.Book;
import com.unilog.reporitory.BookRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;

	public Book findBookById(int id) {
		return bookRepository.findBookById(id);
	}

	public List<Book> findAllBook() {
		return bookRepository.findAll();
	}

	public Map<String, Object> createBook(Book Bookrequest) {
		Map<String, Object> response = new HashMap<String, Object>();
		Book book=new Book();
		book.setBelongsTo(Bookrequest.getBelongsTo());
		book.setBookName(Bookrequest.getBookName());
		book.setUser(Bookrequest.getUser());
		bookRepository.save(book);
		
		
		response.put("status", HttpStatus.CREATED);
		return response;
	}

	public List<Book> getAllBooks(String belongsTo) {
		return bookRepository.findAllByBelongsTo(belongsTo);

	}

	// Find by Book id
	public Book findByBookId(Integer id) {
		Book bookResponse = null;
		try {
			bookResponse = bookRepository.findBookById(id);
			if (bookResponse == null)
				throw new RuntimeException("book Id is Not found in Database");
		} catch (Exception e) {
		}
		return bookResponse;
	}

	// Delete the Book by Id
	public void deleteByBelongsToAndId(String belongsTo,Integer id) {
		Book bookResponse  = bookRepository.findBookByBelongsToAndId(belongsTo,id);
		System.out.println("id===>"+bookResponse.getId());

		if (bookResponse != null) {
			bookRepository.delete(bookResponse);
		} else {

			throw new RuntimeException("Some problems occurred during deleting Book ");
		}

	}

	// Updating the Book by Id
	public Book updateBook(Book bookDetails, Integer id) {

		Book bookResponse = null;
		if (bookDetails != null && id != null) {
			Book bookById = findByBookId(id);
			

			
			bookById.setBookName(bookDetails.getBookName());
			

			bookResponse = bookRepository.save(bookById);
		} else {

			throw new RuntimeException("Something missed in Updating the Book");
		}
		return bookResponse;

	}

	public Book findByBookIdAnBelongsTo(String belongsTo,Integer id) {
		Book bookResponse = null;
		try {
			bookResponse = bookRepository.findBookByBelongsToAndId(belongsTo,id);
			if (bookResponse == null)
				throw new RuntimeException("book Id is Not found in Database");
		} catch (Exception e) {
		}
		return bookResponse;
	}

	
	

}
