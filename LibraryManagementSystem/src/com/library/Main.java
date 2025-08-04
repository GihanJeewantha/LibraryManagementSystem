package com.library;

import com.library.datastructure.BookLinkedList;
import com.library.model.Book;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to Library Management System!");
        BookLinkedList bookList = new BookLinkedList();
        bookList.addBook(new Book(1, "Java Basics", "John Doe", "1234567890123", true));
        bookList.displayBooks();
    }
}