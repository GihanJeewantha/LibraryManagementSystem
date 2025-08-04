package com.library;

import com.library.datastructure.BookLinkedList;
import com.library.db.DatabaseManager;
import com.library.model.Book;

public class Library {
    private BookLinkedList bookList;
    private DatabaseManager dbManager;

    public Library() {
        this.bookList = new BookLinkedList();
        this.dbManager = new DatabaseManager();
        dbManager.createDatabaseAndTable(); // Ensure database and table exist
    }

    public void addBook(Book book) {
        bookList.addBook(book);
        dbManager.addBook(book);
    }

    public void borrowBook(int bookId) {
        // Simulate finding and borrowing a book
        Book book = findBookById(bookId);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            dbManager.updateBookAvailability(bookId, false);
            System.out.println("Book borrowed: " + book.getTitle());
        } else {
            System.out.println("Book not available or not found.");
        }
    }

    private Book findBookById(int id) {
        return bookList.findBookById(id);
    }

    public void displayBooks() {
        bookList.displayBooks();
        dbManager.displayAllBooks();
    }
}