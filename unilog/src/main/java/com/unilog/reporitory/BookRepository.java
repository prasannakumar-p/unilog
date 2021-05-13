package com.unilog.reporitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unilog.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	Book findBookById(int id);

	List<Book> findAllByBelongsTo(String belongsTo);

	Book findBookByBelongsToAndId(String belongsTo, Integer id);

	Book findBookByBookName(String bookName);

}
