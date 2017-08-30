package com.kverchi.diary.service.impl;

import java.util.List;

import com.kverchi.diary.domain.SearchAttributes;
import com.kverchi.diary.domain.SearchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.BookDao;
import com.kverchi.diary.domain.Book;
import com.kverchi.diary.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookDao bookDao;
	
	/*public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}*/

	@Override
	
	public List<Book> getAllBooks() {
		return bookDao.getAllRecords();
	}

	@Override
	
	public Book getBookById(int book_id) {
		return bookDao.getById(book_id);
	}

	@Override
	
	public Book addBook(Book book) {
		//int addedId = (Integer)bookDao.create(book);
		//Book addedBook = bookDao.getById(addedId);
		Book addedBook = (Book)bookDao.persist(book);
		return addedBook;
	}

	@Override
	
	public Book updateBook(Book book) {
		bookDao.update(book);
		Book updatedBook = bookDao.getById(book.getBook_id());
		return updatedBook;
	}

	@Override
	
	public void deleteBook(int book_id) {
		Book bookToDel = bookDao.getById(book_id);
		bookDao.delete(bookToDel);
	}

	@Override
	public SearchResults<Book> search(SearchAttributes searchAttributes) {
		return null;
	}

}
