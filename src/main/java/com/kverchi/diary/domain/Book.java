package com.kverchi.diary.domain;

import javax.persistence.*;

@Entity
@Table(name="books")
public class Book {
	@Id
	@Column(name="book_id")
	@SequenceGenerator(name="id_generator", sequenceName="books_book_id_seq", allocationSize=1)
	@GeneratedValue(generator="id_generator", strategy=GenerationType.SEQUENCE)
	private int book_id;
	@Column(name="book_title")
	private String book_title;
	@Column(name="book_description")
	private String book_description;
	@Column(name="author")
	private String author;
	/*@ManyToOne
	@JoinColumn(name="auth_id")
	private Author author;*/

	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getBook_title() {
		return book_title;
	}
	public void setBook_title(String book_title) {
		this.book_title = book_title;
	}
	public String getBook_description() {
		return book_description;
	}
	public void setBook_description(String book_description) {
		this.book_description = book_description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
/*public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}*/
}
