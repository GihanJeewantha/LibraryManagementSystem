package com.library;

import com.library.datastructure.BookLinkedList;
import com.library.db.DatabaseManager;
import com.library.model.Book;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to Library Management System!");

        // Initialize database
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.createDatabaseAndTable();

        // Add a book to linked list and database
        BookLinkedList bookList = new BookLinkedList();
        Book book = new Book(1, "Java Basics", "Gihan Jeewantha", "1234567890123", true);
        bookList.addBook(book);
        dbManager.addBook(book);

        // Display books
        bookList.displayBooks();
        dbManager.displayAllBooks();
    }
}