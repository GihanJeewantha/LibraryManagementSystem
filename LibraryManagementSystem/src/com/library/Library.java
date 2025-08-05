package com.library;

import com.library.datastructure.BookLinkedList;
import com.library.db.DatabaseManager;
import com.library.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Library {
    private BookLinkedList bookList;
    private DatabaseManager dbManager;

    public Library() {
        this.bookList = new BookLinkedList();
        this.dbManager = new DatabaseManager();
        dbManager.createDatabaseAndTable(); // Ensure database and table exist
        syncBookList(); // Sync with database
    }

    private void syncBookList() {
        bookList = new BookLinkedList(); // Reset the list
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                        rs.getString("isbn"), rs.getBoolean("is_available"));
                bookList.addBook(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        int generatedId = dbManager.addBook(book);
        if (generatedId != -1) {
            bookList.addBook(book); // Add to linked list with updated ID
        }
    }

    public void borrowBook(int bookId) {
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