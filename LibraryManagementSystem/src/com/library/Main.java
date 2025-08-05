package com.library;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to Library Management System!");

        // Initialize library
        Library library = new Library();

        // Display existing books without adding a new one
        library.displayBooks();

        // Attempt to borrow the book (already borrowed, should fail)
        library.borrowBook(1);

        // Display books again
        library.displayBooks();
    }
}