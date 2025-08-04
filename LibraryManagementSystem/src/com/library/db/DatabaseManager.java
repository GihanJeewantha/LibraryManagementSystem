package com.library.db;

import com.library.model.Book;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Admin";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createDatabaseAndTable() {
        String createDbSql = "CREATE DATABASE IF NOT EXISTS library_db";
        String useDbSql = "USE library_db";
        String createTableSql = """
        CREATE TABLE IF NOT EXISTS books (
            id INT AUTO_INCREMENT PRIMARY KEY,
            title VARCHAR(100) NOT NULL,
            author VARCHAR(50) NOT NULL,
            isbn VARCHAR(13) UNIQUE NOT NULL,
            is_available BOOLEAN DEFAULT TRUE
        )
        """;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDbSql); // Create database if it doesn't exist
            stmt.executeUpdate(useDbSql);   // Select the database
            stmt.executeUpdate(createTableSql); // Create table
            System.out.println("Database and table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        String checkSql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        String insertSql = "INSERT INTO books (title, author, isbn, is_available) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            // Check if ISBN already exists
            checkStmt.setString(1, book.getIsbn());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // ISBN doesn't exist, proceed with insertion
                    insertStmt.setString(1, book.getTitle());
                    insertStmt.setString(2, book.getAuthor());
                    insertStmt.setString(3, book.getIsbn());
                    insertStmt.setBoolean(4, book.isAvailable());
                    insertStmt.executeUpdate();
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            System.out.println("Book added to database with ID: " + generatedKeys.getInt(1));
                        }
                    }
                } else {
                    System.out.println("Book with ISBN " + book.getIsbn() + " already exists, skipping insertion.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                        rs.getString("isbn"), rs.getBoolean("is_available"));
                System.out.println(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookAvailability(int bookId, boolean available) {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
            System.out.println("Updated availability for book ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}