package com.kverchi.diary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Book;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.BookService;

@RestController	
@RequestMapping("books")
public class BookController {
	@Autowired
	BookService bookService;
	/*@Autowired(required=true)
	@Qualifier(value="bookService")
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}*/
	@RequestMapping("/edit/{book_id}")
    public Book editBook(@PathVariable("book_id") int book_id, Model model){
		Book book = bookService.getBookById(book_id);
		return book;
	}
	//TODO return ServiceResponse
	@RequestMapping("/remove/{book_id}")
	public String removeBook(@PathVariable("book_id") int book_id) {
		bookService.deleteBook(book_id);
		String res = "OK";
		return res;
	}
	@RequestMapping(value="/add-book", method = RequestMethod.POST)
	public Book addBook(@RequestBody Book book) {
		Book addedBook;
		if(book.getBook_id() == -1) {
			addedBook = bookService.addBook(book);
		}
		else {
			addedBook = bookService.updateBook(book);
		}
		return addedBook;
	}
	@RequestMapping("/list")
	public ModelAndView showBooks() {
		List<Book> all_books = bookService.getAllBooks();
		ModelAndView mv = new ModelAndView("books");
		mv.addObject("all_books", all_books);
		return mv;
	}
}
