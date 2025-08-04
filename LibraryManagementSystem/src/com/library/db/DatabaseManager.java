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
        String sql = "INSERT INTO books (id, title, author, isbn, is_available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getIsbn());
            stmt.setBoolean(5, book.isAvailable());
            stmt.executeUpdate();
            System.out.println("Book added to database: " + book.getTitle());
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
}