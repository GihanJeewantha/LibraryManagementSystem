package com.library;

import com.library.datastructure.BookLinkedList;
import com.library.db.DatabaseManager;
import com.library.model.Book;
import java.sql.*;

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
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().trim().isEmpty() ||
                book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            System.out.println("Error: Book details cannot be null or empty.");
            return;
        }
        int generatedId = dbManager.addBook(book);
        if (generatedId != -1) {
            bookList.addBook(book); // Add to linked list with updated ID
        }
    }

    public void borrowBook(int bookId) {
        if (bookId <= 0) {
            System.out.println("Error: Book ID must be a positive number.");
            return;
        }
        Book book = findBookById(bookId);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            try {
                dbManager.updateBookAvailability(bookId, false);
                System.out.println("Book borrowed: " + book.getTitle());
            } catch (Exception e) {
                System.out.println("Error updating database: " + e.getMessage());
                book.setAvailable(true); // Revert change on failure
            }
        } else {
            System.out.println("Book not available or not found.");
        }
    }

    public void returnBook(int bookId) {
        if (bookId <= 0) {
            System.out.println("Error: Book ID must be a positive number.");
            return;
        }
        Book book = findBookById(bookId);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            try {
                dbManager.updateBookAvailability(bookId, true);
                System.out.println("Book returned: " + book.getTitle());
            } catch (Exception e) {
                System.out.println("Error updating database: " + e.getMessage());
                book.setAvailable(false); // Revert change on failure
            }
        } else {
            System.out.println("Book not borrowed or not found.");
        }
    }

    public void deleteBook(int bookId) {
        if (bookId <= 0) {
            System.out.println("Error: Book ID must be a positive number.");
            return;
        }
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                if (bookList.removeBookById(bookId)) {
                    System.out.println("Book deleted with ID: " + bookId);
                } else {
                    System.out.println("Book not found in linked list with ID: " + bookId);
                }
            } else {
                System.out.println("Book not found with ID: " + bookId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting book with ID: " + bookId + ": " + e.getMessage());
        }
    }

    private Book findBookById(int id) {
        return bookList.findBookById(id);
    }

    public void displayBooks() {
        bookList.displayBooks();
    }

    public void searchBooks(String query) {
        bookList.searchBooks(query);
    }

    public void saveToFile(String filename) {
        bookList.saveToFile(filename, dbManager);
    }

    public void loadFromFile(String filename) {
        bookList.loadFromFile(filename, dbManager);
    }
}