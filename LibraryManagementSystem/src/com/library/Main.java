package com.library;

import com.library.model.Book;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to Library Management System!");

        // Initialize library
        Library library = new Library();

        // Add a book
        library.addBook(new Book(1, "Java Basics", "Gihan Jeewantha", "1234567890123", true));

        // Display books
        library.displayBooks();

        // Borrow the book
        library.borrowBook(1);

        // Display books again to show change
        library.displayBooks();
    }
}