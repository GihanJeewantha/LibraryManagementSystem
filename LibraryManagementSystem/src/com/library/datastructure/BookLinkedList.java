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
}