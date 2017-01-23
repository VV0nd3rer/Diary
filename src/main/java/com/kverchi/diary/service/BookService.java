package com.kverchi.diary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.Book;

public interface BookService {
	public List<Book> getAllBooks();
	public Book getBookById(int book_id);
	public Book addBook(Book book);
	public Book updateBook(Book book);
	public void deleteBook(int book_id);
}
