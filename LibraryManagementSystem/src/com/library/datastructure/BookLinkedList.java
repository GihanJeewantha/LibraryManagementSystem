package com.library.datastructure;

import com.library.model.Book;

public class BookLinkedList {
    private class Node {
        Book book;
        Node next;

        Node(Book book) {
            this.book = book;
            this.next = null;
        }
    }

    private Node head;

    public BookLinkedList() {
        this.head = null;
    }

    public void addBook(Book book) {
        Node newNode = new Node(book);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void displayBooks() {
        Node current = head;
        while (current != null) {
            System.out.println(current.book);
            current = current.next;
        }
    }

    public Book findBookById(int id) {
        Node current = head;
        while (current != null) {
            if (current.book.getId() == id) {
                return current.book;
            }
            current = current.next;
        }
        return null;
    }

    public boolean removeBookById(int id) {
        Node current = head;
        Node prev = null;
        while (current != null) {
            if (current.book.getId() == id) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public void searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            System.out.println("Error: Search query cannot be empty.");
            return;
        }
        query = query.toLowerCase().trim();
        boolean found = false;
        Node current = head;
        while (current != null) {
            Book book = current.book;
            if (book.getTitle().toLowerCase().contains(query) ||
                    book.getAuthor().toLowerCase().contains(query) ||
                    book.getIsbn().toLowerCase().contains(query)) {
                System.out.println(book);
                found = true;
            }
            current = current.next;
        }
        if (!found) {
            System.out.println("No books found matching the query: " + query);
        }
    }
}