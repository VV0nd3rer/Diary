package com.kverchi.diary.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.BookDao;
import com.kverchi.diary.domain.Book;

@Repository
public class BookDaoImpl extends GenericDaoImpl<Book> implements BookDao {

	
	/*@Autowired
	private SessionFactory sessionFactory; */
	final static Logger logger = Logger.getLogger(BookDaoImpl.class);
	
	
	/*@Transactional
	@Override
	public List<Book> getAllBooks() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Book> bookList = session.createQuery("from Book").list();
		return bookList;
	}
	@Transactional
	@Override
	public Book getBookById(int book_id) {
		Session session = sessionFactory.getCurrentSession();
		Book book = session.get(Book.class, new Integer(book_id));
		return book;
	}
	@Transactional
	@Override
	public int addBook(Book book) {
		Session session = sessionFactory.getCurrentSession();
		Integer id = (Integer)session.save(book);
		return id;
	}
	@Transactional
	@Override
	public void updateBook(Book book) {
		Session session = sessionFactory.getCurrentSession();
		session.update(book);
	}
	@Transactional
	@Override
	public void deleteBook(int book_id) {
		Session session = sessionFactory.getCurrentSession();
		Book bookToDel = session.get(Book.class, new Integer(book_id));
		if(bookToDel != null) {
			session.delete(bookToDel);
		}
	}
*/
}
