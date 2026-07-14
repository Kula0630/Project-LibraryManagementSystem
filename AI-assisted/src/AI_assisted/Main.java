package AI_assisted;

import java.time.LocalDate;
import java.util.*;

/**
 * Main entry point for the Library Management System.
 * Provides a console-based user interface with comprehensive menu options.
 *
 * @author AI-Assisted Development
 * @version 2.0
 */
public class Main {

    private static Library library;
    private static Scanner scanner;
    private static final String LIBRARY_NAME = "Central Library";
    private static final String LIBRARY_ID = "LIB-001";

    public static void main(String[] args) {
        try {
            initializeSystem();
            displayWelcomeMessage();

            boolean running = true;
            while (running) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                try {
                    switch (choice) {
                        case 0:
                            running = false;
                            System.out.println("\nThank you for using the Library Management System!");
                            System.out.println("Goodbye!");
                            break;
                        case 1:
                            manageBooks();
                            break;
                        case 2:
                            manageMembers();
                            break;
                        case 3:
                            manageTransactions();
                            break;
                        case 4:
                            viewReports();
                            break;
                        case 5:
                            libraryStatistics();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (LibraryException e) {
                    System.err.println("Error: " + e.getMessage());
                    System.err.println("Details: " + e.getDetails());
                } catch (Exception e) {
                    System.err.println("Unexpected error: " + e.getMessage());
                }

                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }

            scanner.close();

        } catch (Exception e) {
            System.err.println("System initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== Initialization ====================

    private static void initializeSystem() {
        scanner = new Scanner(System.in);
        library = Library.getInstance(LIBRARY_NAME, LIBRARY_ID);
        loadSampleData();
    }

    private static void loadSampleData() {
        try {
            // Add sample books
            library.addBook("The Great Gatsby", "F. Scott Fitzgerald",
                    "978-0-7432-7356-5", 1925, "Fiction", "Scribner", 5, "English");
            library.addBook("To Kill a Mockingbird", "Harper Lee",
                    "978-0-06-112008-4", 1960, "Fiction", "HarperCollins", 3, "English");
            library.addBook("1984", "George Orwell",
                    "978-0-452-28423-4", 1949, "Science Fiction", "Houghton Mifflin", 4, "English");
            library.addBook("Introduction to Algorithms", "Thomas H. Cormen",
                    "978-0-262-03384-8", 2009, "Technical", "MIT Press", 2, "English");
            library.addBook("Clean Code", "Robert C. Martin",
                    "978-0-13-235088-4", 2008, "Technical", "Prentice Hall", 3, "English");
            library.addBook("The Pragmatic Programmer", "David Thomas",
                    "978-0-201-61622-4", 1999, "Technical", "Addison-Wesley", 2, "English");

            // Add sample members
            library.addMember("John Doe", "john@email.com", "123-456-7890",
                    Member.MemberType.STUDENT);
            library.addMember("Jane Smith", "jane@email.com", "098-765-4321",
                    Member.MemberType.FACULTY);
            library.addMember("Bob Johnson", "bob@email.com", "555-123-4567",
                    Member.MemberType.STAFF);
            library.addMember("Alice Williams", "alice@email.com", "555-987-6543",
                    Member.MemberType.PUBLIC);
            library.addMember("Charlie Brown", "charlie@email.com", "555-456-7890",
                    Member.MemberType.PREMIUM);

            System.out.println("Sample data loaded successfully!");

        } catch (LibraryException e) {
            System.err.println("Error loading sample data: " + e.getMessage());
        }
    }

    // ==================== UI Display Methods ====================

    private static void displayWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║   WELCOME TO " + LIBRARY_NAME + "          ║");
        System.out.println("║   Library Management System v2.0                         ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("System Date: " + LocalDate.now());
        System.out.println();
    }

    private static void displayMainMenu() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("                      MAIN MENU                          ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  1.  Manage Books                                      ");
        System.out.println("  2.  Manage Members                                   ");
        System.out.println("  3.  Manage Transactions                              ");
        System.out.println("  4.  View Reports                                     ");
        System.out.println("  5.  Library Statistics                               ");
        System.out.println("  0.  Exit                                             ");
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    // ==================== Book Management ====================

    private static void manageBooks() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("                   BOOK MANAGEMENT                      ");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("  1.  Add New Book                                      ");
            System.out.println("  2.  Remove Book                                       ");
            System.out.println("  3.  Search Books                                      ");
            System.out.println("  4.  View All Books                                    ");
            System.out.println("  5.  View Available Books                              ");
            System.out.println("  6.  Update Book Details                               ");
            System.out.println("  7.  Add Book Copies                                   ");
            System.out.println("  0.  Back to Main Menu                                 ");
            System.out.println("═══════════════════════════════════════════════════════════");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    addNewBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    displayAllBooks();
                    break;
                case 5:
                    displayAvailableBooks();
                    break;
                case 6:
                    updateBookDetails();
                    break;
                case 7:
                    addBookCopies();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addNewBook() throws LibraryException {
        System.out.println("\n--- Add New Book ---");

        String title = getStringInput("Enter title: ");
        String author = getStringInput("Enter author: ");
        String isbn = getStringInput("Enter ISBN: ");
        int year = getIntInput("Enter publication year: ");
        String category = getStringInput("Enter category: ");
        String publisher = getStringInput("Enter publisher: ");
        int copies = getIntInput("Enter number of copies: ");
        String language = getStringInput("Enter language: ");

        Book book = library.addBook(title, author, isbn, year, category,
                publisher, copies, language);
        System.out.println("Book added successfully!");
        System.out.println("Book ID: " + book.getBookId());
    }

    private static void removeBook() throws LibraryException {
        System.out.println("\n--- Remove Book ---");
        String bookId = getStringInput("Enter Book ID to remove: ");
        library.removeBook(bookId);
        System.out.println("Book removed successfully!");
    }

    private static void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ISBN");
        System.out.println("4. Search by Category");

        int choice = getIntInput("Enter search type: ");
        String query = getStringInput("Enter search query: ");

        Library.SearchType searchType = switch (choice) {
            case 1 -> Library.SearchType.TITLE;
            case 2 -> Library.SearchType.AUTHOR;
            case 3 -> Library.SearchType.ISBN;
            case 4 -> Library.SearchType.CATEGORY;
            default -> Library.SearchType.TITLE;
        };

        List<Book> results = library.searchBooks(searchType, query);
        displayBookList(results);
    }

    private static void displayAllBooks() {
        System.out.println("\n--- All Books ---");
        displayBookList(library.getAllBooks());
    }

    private static void displayAvailableBooks() {
        System.out.println("\n--- Available Books ---");
        displayBookList(library.getAvailableBooks());
    }

    private static void updateBookDetails() throws LibraryException {
        System.out.println("\n--- Update Book Details ---");
        String bookId = getStringInput("Enter Book ID to update: ");
        Book book = library.getBookById(bookId);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        System.out.println("Current details: " + book);
        System.out.println("\nWhat to update?");
        System.out.println("1. Category");
        System.out.println("2. Location");
        System.out.println("3. Description");
        System.out.println("4. Rating");

        int choice = getIntInput("Enter choice: ");

        switch (choice) {
            case 1:
                String category = getStringInput("Enter new category: ");
                // Note: Book class doesn't have setCategory in this version
                System.out.println("Category update feature coming soon.");
                break;
            case 2:
                String location = getStringInput("Enter new location: ");
                book.setLocation(location);
                System.out.println("Location updated!");
                break;
            case 3:
                String description = getStringInput("Enter new description: ");
                book.setDescription(description);
                System.out.println("Description updated!");
                break;
            case 4:
                double rating = getDoubleInput("Enter rating (1-5): ");
                book.updateRating(rating);
                System.out.println("Rating updated!");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void addBookCopies() throws LibraryException {
        System.out.println("\n--- Add Book Copies ---");
        String bookId = getStringInput("Enter Book ID: ");
        Book book = library.getBookById(bookId);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        int additionalCopies = getIntInput("Enter number of additional copies: ");
        book.addCopies(additionalCopies);
        System.out.println("Copies added successfully!");
        System.out.println("Total copies: " + book.getTotalCopies());
        System.out.println("Available copies: " + book.getAvailableCopies());
    }

    // ==================== Member Management ====================

    private static void manageMembers() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("                  MEMBER MANAGEMENT                    ");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("  1.  Register New Member                               ");
            System.out.println("  2.  Remove Member                                     ");
            System.out.println("  3.  Search Members                                    ");
            System.out.println("  4.  View All Members                                  ");
            System.out.println("  5.  View Member Details                               ");
            System.out.println("  6.  Pay Fine                                          ");
            System.out.println("  0.  Back to Main Menu                                 ");
            System.out.println("═══════════════════════════════════════════════════════════");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    registerNewMember();
                    break;
                case 2:
                    removeMember();
                    break;
                case 3:
                    searchMembers();
                    break;
                case 4:
                    displayAllMembers();
                    break;
                case 5:
                    viewMemberDetails();
                    break;
                case 6:
                    payFine();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void registerNewMember() throws LibraryException {
        System.out.println("\n--- Register New Member ---");

        String name = getStringInput("Enter full name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone number: ");

        System.out.println("Select member type:");
        System.out.println("1. STUDENT (3 books, 14 days)");
        System.out.println("2. FACULTY (10 books, 30 days)");
        System.out.println("3. STAFF (5 books, 21 days)");
        System.out.println("4. PUBLIC (3 books, 14 days)");
        System.out.println("5. PREMIUM (8 books, 21 days)");

        int typeChoice = getIntInput("Enter choice: ");
        Member.MemberType memberType = switch (typeChoice) {
            case 1 -> Member.MemberType.STUDENT;
            case 2 -> Member.MemberType.FACULTY;
            case 3 -> Member.MemberType.STAFF;
            case 4 -> Member.MemberType.PUBLIC;
            case 5 -> Member.MemberType.PREMIUM;
            default -> Member.MemberType.PUBLIC;
        };

        Member member = library.addMember(name, email, phone, memberType);
        System.out.println("Member registered successfully!");
        System.out.println("Member ID: " + member.getMemberId());
        System.out.println("Member Type: " + memberType);
        System.out.println("Max Books: " + memberType.getMaxBooks());
    }

    private static void removeMember() throws LibraryException {
        System.out.println("\n--- Remove Member ---");
        String memberId = getStringInput("Enter Member ID to remove: ");
        library.removeMember(memberId);
        System.out.println("Member removed successfully!");
    }

    private static void searchMembers() {
        System.out.println("\n--- Search Members ---");
        String query = getStringInput("Enter name or email to search: ");
        List<Member> results = library.searchMembers(query);
        displayMemberList(results);
    }

    private static void displayAllMembers() {
        System.out.println("\n--- All Members ---");
        displayMemberList(library.getAllMembers());
    }

    private static void viewMemberDetails() {
        System.out.println("\n--- View Member Details ---");
        String memberId = getStringInput("Enter Member ID: ");
        Member member = library.getMemberById(memberId);

        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("                  MEMBER DETAILS                         ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("ID: " + member.getMemberId());
        System.out.println("Name: " + member.getName());
        System.out.println("Email: " + member.getEmail());
        System.out.println("Phone: " + member.getPhone());
        System.out.println("Type: " + member.getMemberType());
        System.out.println("Status: " + (member.isActive() ? "Active" : "Inactive"));
        System.out.println("Registered: " + member.getRegistrationDate());
        System.out.println("Borrowed Books: " + member.getBorrowedBooks().size() +
                "/" + member.getMemberType().getMaxBooks());
        System.out.println("Outstanding Fine: $" + member.getOutstandingFine());
        System.out.println("\nBorrowing History:");
        for (Member.BorrowingHistory history : member.getBorrowingHistory()) {
            System.out.println("  " + history);
        }
    }

    private static void payFine() throws LibraryException {
        System.out.println("\n--- Pay Fine ---");
        String memberId = getStringInput("Enter Member ID: ");
        Member member = library.getMemberById(memberId);

        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.println("Outstanding Fine: $" + member.getOutstandingFine());
        if (member.getOutstandingFine() == 0) {
            System.out.println("No fines to pay!");
            return;
        }

        double amount = getDoubleInput("Enter amount to pay: ");
        if (library.payFine(memberId, amount)) {
            System.out.println("Payment successful!");
            System.out.println("Remaining fine: $" + member.getOutstandingFine());
        } else {
            System.out.println("Payment failed. Please try again.");
        }
    }

    // ==================== Transaction Management ====================

    private static void manageTransactions() throws LibraryException {
        boolean back = false;
        while (!back) {
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("               TRANSACTION MANAGEMENT                  ");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("  1.  Borrow Book                                       ");
            System.out.println("  2.  Return Book                                       ");
            System.out.println("  3.  View All Transactions                             ");
            System.out.println("  4.  View Member Transactions                          ");
            System.out.println("  5.  View Active Transactions                          ");
            System.out.println("  0.  Back to Main Menu                                 ");
            System.out.println("═══════════════════════════════════════════════════════════");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 0:
                    back = true;
                    break;
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewAllTransactions();
                    break;
                case 4:
                    viewMemberTransactions();
                    break;
                case 5:
                    viewActiveTransactions();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void borrowBook() throws LibraryException {
        System.out.println("\n--- Borrow Book ---");
        String bookId = getStringInput("Enter Book ID: ");
        String memberId = getStringInput("Enter Member ID: ");

        Transaction transaction = library.borrowBook(bookId, memberId);
        System.out.println("Book borrowed successfully!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Borrow Date: " + transaction.getBorrowDate());
        System.out.println("Due Date: " + transaction.getBorrowDate().plusDays(
                transaction.getMember().getMemberType().getLoanDays()));
    }

    private static void returnBook() throws LibraryException {
        System.out.println("\n--- Return Book ---");
        String bookId = getStringInput("Enter Book ID: ");
        String memberId = getStringInput("Enter Member ID: ");

        Transaction transaction = library.returnBook(bookId, memberId);
        System.out.println("Book returned successfully!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Return Date: " + transaction.getReturnDate());
        System.out.println("Days Borrowed: " + transaction.getDaysBorrowed());
        System.out.println("Overdue Days: " + transaction.getOverdueDays());
        System.out.println("Fine: $" + transaction.getFineAmount());
    }

    private static void viewAllTransactions() {
        System.out.println("\n--- All Transactions ---");
        List<Transaction> transactions = library.getAllTransactions();
        displayTransactionList(transactions);
    }

    private static void viewMemberTransactions() {
        System.out.println("\n--- Member Transactions ---");
        String memberId = getStringInput("Enter Member ID: ");
        List<Transaction> transactions = library.getMemberTransactions(memberId);
        displayTransactionList(transactions);
    }

    private static void viewActiveTransactions() {
        System.out.println("\n--- Active Transactions ---");
        List<Transaction> activeTransactions = library.getAllTransactions().stream()
                .filter(t -> !t.isReturned())
                .collect(java.util.stream.Collectors.toList());
        displayTransactionList(activeTransactions);
    }

    // ==================== Reports and Statistics ====================

    private static void viewReports() {
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("                    REPORTS                             ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  1.  Popular Books                                      ");
        System.out.println("  2.  Member Activity Report                             ");
        System.out.println("  3.  Fine Report                                        ");
        System.out.println("  0.  Back to Main Menu                                  ");
        System.out.println("═══════════════════════════════════════════════════════════");

        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewPopularBooks();
                break;
            case 2:
                viewMemberActivityReport();
                break;
            case 3:
                viewFineReport();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void viewPopularBooks() {
        System.out.println("\n--- Popular Books ---");
        int limit = getIntInput("Enter number of books to display: ");
        List<Book> popularBooks = library.getPopularBooks(limit);

        System.out.println("\nTop " + popularBooks.size() + " Most Borrowed Books:");
        for (int i = 0; i < popularBooks.size(); i++) {
            Book book = popularBooks.get(i);
            System.out.printf("%d. %s by %s (Borrowed: %d times)%n",
                    (i + 1), book.getTitle(), book.getAuthor(), book.getBorrowedCount());
        }
    }

    private static void viewMemberActivityReport() {
        System.out.println("\n--- Member Activity Report ---");
        List<Member> members = library.getAllMembers();

        System.out.println("\nMember Activity Summary:");
        for (Member member : members) {
            long totalBorrowed = member.getBorrowingHistory().size();
            long activeBorrows = member.getBorrowedBooks().size();
            System.out.printf("%s (%s): %d total borrows, %d active, Fine: $%.2f%n",
                    member.getName(), member.getMemberType(),
                    totalBorrowed, activeBorrows, member.getOutstandingFine());
        }
    }

    private static void viewFineReport() {
        System.out.println("\n--- Fine Report ---");
        List<Member> members = library.getAllMembers();

        double totalFines = 0;
        int membersWithFines = 0;

        System.out.println("\nMembers with Outstanding Fines:");
        for (Member member : members) {
            if (member.getOutstandingFine() > 0) {
                System.out.printf("  %s: $%.2f%n", member.getName(), member.getOutstandingFine());
                totalFines += member.getOutstandingFine();
                membersWithFines++;
            }
        }

        System.out.println("\nSummary:");
        System.out.println("Total Outstanding Fines: $" + totalFines);
        System.out.println("Members with Fines: " + membersWithFines);
        System.out.println("Total Members: " + members.size());
    }

    private static void libraryStatistics() {
        Map<String, Object> stats = library.getLibraryStatistics();

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("               LIBRARY STATISTICS                      ");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("Library: " + stats.get("libraryName"));
        System.out.println("\nBooks:");
        System.out.println("  Total Books: " + stats.get("totalBooks"));
        System.out.println("  Available Books: " + stats.get("availableBooks"));
        System.out.println("\nMembers:");
        System.out.println("  Total Members: " + stats.get("totalMembers"));
        System.out.println("  Active Members: " + stats.get("activeMembers"));
        System.out.println("\nTransactions:");
        System.out.println("  Total Transactions: " + stats.get("totalTransactions"));
        System.out.println("  Active Transactions: " + stats.get("activeTransactions"));
        System.out.println("\nFines:");
        System.out.println("  Total Outstanding Fines: $" + stats.get("totalOutstandingFines"));

        // Additional statistics
        System.out.println("\nAdditional Statistics:");
        System.out.println("  Average Books Per Member: " +
                String.format("%.2f", (double) library.getBookCount() / library.getMemberCount()));
    }

    // ==================== Display Methods ====================

    private static void displayBookList(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("\nFound " + books.size() + " book(s):");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("─────────────────────────────────────────────────────────────────");
    }

    private static void displayMemberList(List<Member> members) {
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.println("\nFound " + members.size() + " member(s):");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Member member : members) {
            System.out.println(member);
        }
        System.out.println("─────────────────────────────────────────────────────────────────");
    }

    private static void displayTransactionList(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\nFound " + transactions.size() + " transaction(s):");
        System.out.println("─────────────────────────────────────────────────────────────────");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println("─────────────────────────────────────────────────────────────────");
    }

    // ==================== Input Helpers ====================

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}