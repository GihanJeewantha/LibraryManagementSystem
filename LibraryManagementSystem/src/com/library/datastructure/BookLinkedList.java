package com.library.datastructure;

import com.library.model.Book;
import com.library.db.DatabaseManager;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void saveToFile(String filename, DatabaseManager dbManager) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Node current = head;
            while (current != null) {
                Book book = current.book;
                writer.println(book.getId() + "," + book.getTitle() + "," + book.getAuthor() + "," +
                        book.getIsbn() + "," + book.isAvailable());
                current = current.next;
            }
            System.out.println("Library data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename, DatabaseManager dbManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            head = null; // Reset the list
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    String isbn = parts[3];
                    boolean isAvailable = Boolean.parseBoolean(parts[4]);
                    Book book = new Book(id, title, author, isbn, isAvailable);
                    addBook(book);
                }
            }
            syncDatabaseFromList(dbManager); // Update database after loading
            System.out.println("Library data loaded from " + filename);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }

    private void syncDatabaseFromList(DatabaseManager dbManager) {
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM books"); // Clear existing data
            Node current = head;
            while (current != null) {
                Book book = current.book;
                String sql = "INSERT INTO books (id, title, author, isbn, is_available) VALUES (" +
                        book.getId() + ", '" + book.getTitle() + "', '" + book.getAuthor() + "', '" +
                        book.getIsbn() + "', " + book.isAvailable() + ")";
                stmt.executeUpdate(sql);
                current = current.next;
            }
        } catch (SQLException e) {
            System.out.println("Error syncing database: " + e.getMessage());
        }
    }
}