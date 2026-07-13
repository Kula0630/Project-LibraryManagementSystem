package Traditional;

import java.util.Scanner;

public class Main {
    private static Library library;
    private static Scanner scanner;

    public static void main(String[] args) {
        library = new Library("Central Library");
        scanner = new Scanner(System.in);

        System.out.println("Welcome to " + library.getLibraryName());
        System.out.println("=====================================");

        // Add some sample data
        initializeSampleData();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addMember();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    searchBook();
                    break;
                case 6:
                    displayAllData();
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using the Library Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void initializeSampleData() {
        library.addBook("The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", 1925, "Fiction");
        library.addBook("To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", 1960, "Fiction");
        library.addBook("1984", "George Orwell", "978-0-452-28423-4", 1949, "Science Fiction");
        library.addBook("Introduction to Algorithms", "Thomas H. Cormen", "978-0-262-03384-8", 2009, "Technical");

        library.addMember("John Doe", "john@email.com", "123-456-7890");
        library.addMember("Jane Smith", "jane@email.com", "098-765-4321");
    }

    private static void displayMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. Search Book");
        System.out.println("6. Display All Data");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addBook() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        library.addBook(title, author, isbn, year, category);
    }

    private static void addMember() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        library.addMember(name, email, phone);
    }

    private static void borrowBook() {
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();

        library.borrowBook(bookId, memberId);
    }

    private static void returnBook() {
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();

        library.returnBook(bookId, memberId);
    }

    private static void searchBook() {
        System.out.println("\nSearch by:");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. Category");
        System.out.println("4. ID");
        System.out.print("Enter choice: ");
        int choice = getChoice();

        switch (choice) {
            case 1:
                System.out.print("Enter title: ");
                String title = scanner.nextLine();
                System.out.println(library.searchBooksByTitle(title));
                break;
            case 2:
                System.out.print("Enter author: ");
                String author = scanner.nextLine();
                System.out.println(library.searchBooksByAuthor(author));
                break;
            case 3:
                System.out.print("Enter category: ");
                String category = scanner.nextLine();
                System.out.println(library.searchBooksByCategory(category));
                break;
            case 4:
                System.out.print("Enter book ID: ");
                String id = scanner.nextLine();
                Book book = library.searchBookById(id);
                System.out.println(book != null ? book : "Book not found!");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void displayAllData() {
        library.displayAllBooks();
        library.displayAllMembers();
        library.displayTransactions();
    }
}
