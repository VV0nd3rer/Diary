package com.kverchi.diary.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kverchi.diary.domain.Book;
import com.kverchi.diary.domain.BookSearchAttributes;
import com.kverchi.diary.domain.BookSearchResults;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.dao.BookDao;
import com.kverchi.diary.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookDao bookDao;
	@Autowired
	private PaginationService paginationService;
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
		Book updatedBook = bookDao.getById(book.getBookId());
		return updatedBook;
	}

	@Override
	
	public void deleteBook(int book_id) {
		Book bookToDel = bookDao.getById(book_id);
		bookDao.delete(bookToDel);
	}

	@Override
	public BookSearchResults search(BookSearchAttributes searchAttributes) {
		Pagination pagination = new Pagination();
		pagination.setPageSize(searchAttributes.getPageSize());
		pagination.setCurrentPage(searchAttributes.getCurrentPage());

		Map<BookSearchAttributes.BookSearchType, Object> searchCriteria = searchAttributes.getSearchCriteria();
		Map<String, Object> hasAttributes = new HashMap<>();
		Map<String, String> includingAttributes = new HashMap<>();
		Map<String, String> choosingAttributes = new HashMap<>();
		if (searchCriteria != null && !searchCriteria.isEmpty()) {
			for (Map.Entry<BookSearchAttributes.BookSearchType, Object> entry : searchCriteria.entrySet()) {
				switch (entry.getKey()) {
					case BY_AUTHOR_ID:
						hasAttributes.put("authId", entry.getValue());
						break;
					case BY_TEXT:
						choosingAttributes.put("description", entry.getValue().toString());
						choosingAttributes.put("title", entry.getValue().toString());
						break;
					case IN_TITLE_ONLY:
						includingAttributes.put("title", entry.getValue().toString());
						break;
				}
			}
		}
		int totalRows;
		if(includingAttributes.isEmpty() && choosingAttributes.isEmpty()) {
			totalRows = bookDao.getRowsNumberWithAttributes(hasAttributes);
		} else if(choosingAttributes.isEmpty()){
			totalRows = bookDao.getRowsNumberWithAttributes(hasAttributes, includingAttributes);
		} else {
			totalRows = bookDao.getRowsNumberWithAttributes(hasAttributes, includingAttributes, choosingAttributes);
		}
		pagination.setTotalRows(totalRows);
		pagination = paginationService.calculatePagination(pagination);
		BookSearchResults searchResults = new BookSearchResults();
		searchResults.setTotalPages(pagination.getTotalPages());
		List results;
		if(includingAttributes.isEmpty() && choosingAttributes.isEmpty()) {
			results = bookDao.searchWithAttributes(hasAttributes, pagination);
		} else if(choosingAttributes.isEmpty()) {
			results = bookDao.searchWithAttributes(hasAttributes, includingAttributes, pagination);
		} else {
			results = bookDao.searchWithAttributes(hasAttributes, includingAttributes, choosingAttributes, pagination);
		}
		searchResults.setResults(results);
		return searchResults;
	}

}
