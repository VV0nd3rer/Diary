package com.kverchi.diary.domain;

import javax.persistence.*;

@Entity
@Table(name="books")
public class Book {
	@Id
	@Column(name="book_id")
	@SequenceGenerator(name="id_generator", sequenceName="books_book_id_seq", allocationSize=1)
	@GeneratedValue(generator="id_generator", strategy=GenerationType.SEQUENCE)
	private int bookId;
	@Column(name="book_title")
	private String title;
	@Column(name="book_description")
	private String description;
	@Column(name="author")
	private String author;
	/*@ManyToOne
	@JoinColumn(name="auth_id")
	private Author author;*/

	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
