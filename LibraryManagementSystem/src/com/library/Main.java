package com.library;

import com.library.model.Book;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Display Books");
            System.out.println("2. Add Book");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Exit");
            System.out.println("7. Search Books");
            System.out.println("8. Save to File");
            System.out.println("9. Load from File");
            System.out.print("Enter your choice (1-9): ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        library.displayBooks();
                        break;
                    case 2:
                        System.out.print("Enter title: ");
                        String title = scanner.nextLine().trim();
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine().trim();
                        System.out.print("Enter ISBN: ");
                        String isbn = scanner.nextLine().trim();
                        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                            System.out.println("Error: All fields must be filled.");
                        } else {
                            library.addBook(new Book(0, title, author, isbn, true));
                        }
                        break;
                    case 3:
                        System.out.print("Enter book ID to borrow: ");
                        if (scanner.hasNextInt()) {
                            int borrowId = scanner.nextInt();
                            library.borrowBook(borrowId);
                        } else {
                            System.out.println("Error: Please enter a valid number.");
                            scanner.next(); // Clear invalid input
                        }
                        scanner.nextLine(); // Consume newline
                        break;
                    case 4:
                        System.out.print("Enter book ID to return: ");
                        if (scanner.hasNextInt()) {
                            int returnId = scanner.nextInt();
                            library.returnBook(returnId);
                        } else {
                            System.out.println("Error: Please enter a valid number.");
                            scanner.next(); // Clear invalid input
                        }
                        scanner.nextLine(); // Consume newline
                        break;
                    case 5:
                        System.out.print("Enter book ID to delete: ");
                        if (scanner.hasNextInt()) {
                            int deleteId = scanner.nextInt();
                            library.deleteBook(deleteId);
                        } else {
                            System.out.println("Error: Please enter a valid number.");
                            scanner.next(); // Clear invalid input
                        }
                        scanner.nextLine(); // Consume newline
                        break;
                    case 6:
                        System.out.println("Exiting Library Management System. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    case 7:
                        System.out.print("Enter search query (title, author, or ISBN): ");
                        String query = scanner.nextLine().trim();
                        library.searchBooks(query);
                        break;
                    case 8:
                        System.out.print("Enter filename to save: ");
                        String saveFilename = scanner.nextLine().trim();
                        if (saveFilename.isEmpty()) {
                            System.out.println("Error: Filename cannot be empty.");
                        } else {
                            library.saveToFile(saveFilename);
                        }
                        break;
                    case 9:
                        System.out.print("Enter filename to load: ");
                        String loadFilename = scanner.nextLine().trim();
                        if (loadFilename.isEmpty()) {
                            System.out.println("Error: Filename cannot be empty.");
                        } else {
                            library.loadFromFile(loadFilename);
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } else {
                System.out.println("Error: Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}