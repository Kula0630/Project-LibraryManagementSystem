package AI_assisted;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main Library Management System class using Singleton pattern.
 * Manages books, members, and transactions with comprehensive functionality.
 *
 * @author AI-Assisted Development
 * @version 2.0
 */
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    // Singleton instance
    private static Library instance;

    // Library properties
    private final String libraryName;
    private final String libraryId;
    private final Map<String, Book> books;
    private final Map<String, Member> members;
    private final List<Transaction> transactions;

    // Counters for generating IDs
    private int bookCounter;
    private int memberCounter;
    private int transactionCounter;

    // Configuration
    private final int DEFAULT_BOOK_LIMIT = 5;
    private final int DEFAULT_LOAN_DAYS = 14;
    private final double DEFAULT_FINE_RATE = 1.0;

    /**
     * Private constructor for Singleton pattern.
     */
    private Library(String libraryName, String libraryId) {
        this.libraryName = libraryName;
        this.libraryId = libraryId;
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.bookCounter = 1;
        this.memberCounter = 1;
        this.transactionCounter = 1;
    }

    /**
     * Gets the singleton instance of Library.
     *
     * @param libraryName Name of the library
     * @param libraryId Unique library identifier
     * @return Library instance
     */
    public static synchronized Library getInstance(String libraryName, String libraryId) {
        if (instance == null) {
            instance = new Library(libraryName, libraryId);
        }
        return instance;
    }

    /**
     * Gets the existing instance (throws if not initialized).
     *
     * @return Library instance
     * @throws IllegalStateException if not initialized
     */
    public static synchronized Library getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Library not initialized");
        }
        return instance;
    }

    // ==================== Book Management ====================

    /**
     * Adds a new book to the library.
     *
     * @param title Book title
     * @param author Book author
     * @param isbn ISBN number
     * @param year Publication year
     * @param category Book category
     * @param publisher Publisher name
     * @param totalCopies Number of copies
     * @param language Book language
     * @return The created Book object
     * @throws LibraryException if book already exists
     */
    public Book addBook(String title, String author, String isbn, int year,
                        String category, String publisher, int totalCopies,
                        String language) throws LibraryException {

        // Check if book already exists (by ISBN)
        for (Book existing : books.values()) {
            if (existing.getIsbn().equalsIgnoreCase(isbn)) {
                throw new LibraryException(
                        LibraryException.ErrorType.BOOK_ALREADY_EXISTS,
                        "Book with ISBN " + isbn + " already exists"
                );
            }
        }

        String bookId = "B" + String.format("%03d", bookCounter++);
        Book book = new Book(bookId, title, author, isbn, year, category,
                publisher, totalCopies, language);
        books.put(bookId, book);
        return book;
    }

    /**
     * Removes a book from the library.
     *
     * @param bookId Book ID to remove
     * @return true if removed successfully
     * @throws LibraryException if book not found
     */
    public boolean removeBook(String bookId) throws LibraryException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_FOUND,
                    "Book with ID " + bookId + " not found"
            );
        }
        if (book.getBorrowedCount() > 0) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_AVAILABLE,
                    "Cannot remove book with outstanding borrows"
            );
        }
        books.remove(bookId);
        return true;
    }

    /**
     * Gets a book by ID.
     *
     * @param bookId Book ID
     * @return Book object or null if not found
     */
    public Book getBookById(String bookId) {
        return books.get(bookId);
    }

    /**
     * Searches books by various criteria.
     *
     * @param searchType Type of search (TITLE, AUTHOR, ISBN, CATEGORY)
     * @param query Search query
     * @return List of matching books
     */
    public List<Book> searchBooks(SearchType searchType, String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(books.values());
        }

        String lowerQuery = query.trim().toLowerCase();

        return books.values().stream()
                .filter(book -> {
                    switch (searchType) {
                        case TITLE:
                            return book.getTitle().toLowerCase().contains(lowerQuery);
                        case AUTHOR:
                            return book.getAuthor().toLowerCase().contains(lowerQuery);
                        case ISBN:
                            return book.getIsbn().toLowerCase().contains(lowerQuery);
                        case CATEGORY:
                            return book.getCategory().toLowerCase().contains(lowerQuery);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Search type enum.
     */
    public enum SearchType {
        TITLE, AUTHOR, ISBN, CATEGORY
    }

    /**
     * Gets all books.
     *
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Gets available books.
     *
     * @return List of available books
     */
    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    // ==================== Member Management ====================

    /**
     * Adds a new member.
     *
     * @param name Member name
     * @param email Member email
     * @param phone Member phone
     * @param memberType Member type
     * @return The created Member object
     * @throws LibraryException if member already exists
     */
    public Member addMember(String name, String email, String phone,
                            Member.MemberType memberType) throws LibraryException {

        // Check if member already exists
        for (Member existing : members.values()) {
            if (existing.getEmail().equalsIgnoreCase(email)) {
                throw new LibraryException(
                        LibraryException.ErrorType.MEMBER_ALREADY_EXISTS,
                        "Member with email " + email + " already exists"
                );
            }
        }

        String memberId = "M" + String.format("%03d", memberCounter++);
        Member member = new Member(memberId, name, email, phone, memberType);
        members.put(memberId, member);
        return member;
    }

    /**
     * Removes a member.
     *
     * @param memberId Member ID
     * @return true if removed successfully
     * @throws LibraryException if member not found or has borrowed books
     */
    public boolean removeMember(String memberId) throws LibraryException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.MEMBER_NOT_FOUND,
                    "Member with ID " + memberId + " not found"
            );
        }
        if (!member.getBorrowedBooks().isEmpty()) {
            throw new LibraryException(
                    LibraryException.ErrorType.BORROW_LIMIT_EXCEEDED,
                    "Cannot remove member with borrowed books"
            );
        }
        members.remove(memberId);
        return true;
    }

    /**
     * Gets a member by ID.
     *
     * @param memberId Member ID
     * @return Member object or null if not found
     */
    public Member getMemberById(String memberId) {
        return members.get(memberId);
    }

    /**
     * Searches members by name or email.
     *
     * @param query Search query
     * @return List of matching members
     */
    public List<Member> searchMembers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(members.values());
        }

        String lowerQuery = query.trim().toLowerCase();

        return members.values().stream()
                .filter(member ->
                        member.getName().toLowerCase().contains(lowerQuery) ||
                                member.getEmail().toLowerCase().contains(lowerQuery)
                )
                .collect(Collectors.toList());
    }

    /**
     * Gets all members.
     *
     * @return List of all members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // ==================== Transaction Management ====================

    /**
     * Borrows a book for a member.
     *
     * @param bookId Book ID
     * @param memberId Member ID
     * @return Transaction object
     * @throws LibraryException if borrowing fails
     */
    public Transaction borrowBook(String bookId, String memberId) throws LibraryException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_FOUND,
                    "Book with ID " + bookId + " not found"
            );
        }

        Member member = members.get(memberId);
        if (member == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.MEMBER_NOT_FOUND,
                    "Member with ID " + memberId + " not found"
            );
        }

        // Check if member can borrow
        if (!member.canBorrow()) {
            if (member.getOutstandingFine() > 0) {
                throw new LibraryException(
                        LibraryException.ErrorType.BORROW_LIMIT_EXCEEDED,
                        "Outstanding fine of $" + member.getOutstandingFine() + " must be paid"
                );
            }
            throw new LibraryException(
                    LibraryException.ErrorType.BORROW_LIMIT_EXCEEDED,
                    "Member has reached borrowing limit"
            );
        }

        // Borrow the book
        member.borrowBook(book);

        // Create transaction
        String transactionId = "T" + String.format("%03d", transactionCounter++);
        Transaction transaction = new Transaction(
                transactionId, book, member, LocalDate.now()
        );
        transactions.add(transaction);

        return transaction;
    }

    /**
     * Returns a borrowed book.
     *
     * @param bookId Book ID
     * @param memberId Member ID
     * @return Updated Transaction object
     * @throws LibraryException if return fails
     */
    public Transaction returnBook(String bookId, String memberId) throws LibraryException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_FOUND,
                    "Book with ID " + bookId + " not found"
            );
        }

        Member member = members.get(memberId);
        if (member == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.MEMBER_NOT_FOUND,
                    "Member with ID " + memberId + " not found"
            );
        }

        // Find the active transaction
        Transaction activeTransaction = null;
        for (Transaction transaction : transactions) {
            if (transaction.getBook().equals(book) &&
                    transaction.getMember().equals(member) &&
                    !transaction.isReturned()) {
                activeTransaction = transaction;
                break;
            }
        }

        if (activeTransaction == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_AVAILABLE,
                    "No active borrowing record found for this book and member"
            );
        }

        // Return the book
        member.returnBook(book);

        // Update transaction
        activeTransaction.setReturnDate(LocalDate.now());
        activeTransaction.setFineAmount(activeTransaction.calculateFine());

        return activeTransaction;
    }

    /**
     * Gets all transactions.
     *
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Gets transactions for a member.
     *
     * @param memberId Member ID
     * @return List of transactions
     */
    public List<Transaction> getMemberTransactions(String memberId) {
        Member member = members.get(memberId);
        if (member == null) {
            return new ArrayList<>();
        }
        return transactions.stream()
                .filter(t -> t.getMember().equals(member))
                .collect(Collectors.toList());
    }

    // ==================== Fine Management ====================

    /**
     * Calculates fine for a transaction.
     *
     * @param transactionId Transaction ID
     * @return Fine amount
     * @throws LibraryException if transaction not found
     */
    public double calculateFine(String transactionId) throws LibraryException {
        Transaction transaction = null;
        for (Transaction t : transactions) {
            if (t.getTransactionId().equals(transactionId)) {
                transaction = t;
                break;
            }
        }

        if (transaction == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.SYSTEM_ERROR,
                    "Transaction not found: " + transactionId
            );
        }

        return transaction.calculateFine();
    }

    /**
     * Pays outstanding fine for a member.
     *
     * @param memberId Member ID
     * @param amount Amount to pay
     * @return true if payment successful
     * @throws LibraryException if member not found
     */
    public boolean payFine(String memberId, double amount) throws LibraryException {
        Member member = members.get(memberId);
        if (member == null) {
            throw new LibraryException(
                    LibraryException.ErrorType.MEMBER_NOT_FOUND,
                    "Member with ID " + memberId + " not found"
            );
        }

        return member.payFine(amount);
    }

    // ==================== Statistics and Reports ====================

    /**
     * Generates library statistics.
     *
     * @return Statistics map
     */
    public Map<String, Object> getLibraryStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("libraryName", libraryName);
        stats.put("totalBooks", books.size());
        stats.put("totalMembers", members.size());
        stats.put("totalTransactions", transactions.size());

        // Book statistics
        long availableBooks = books.values().stream()
                .filter(Book::isAvailable)
                .count();
        stats.put("availableBooks", availableBooks);

        // Member statistics
        long activeMembers = members.values().stream()
                .filter(Member::isActive)
                .count();
        stats.put("activeMembers", activeMembers);

        // Transaction statistics
        long activeTransactions = transactions.stream()
                .filter(t -> !t.isReturned())
                .count();
        stats.put("activeTransactions", activeTransactions);

        // Fine statistics
        double totalFines = members.values().stream()
                .mapToDouble(Member::getOutstandingFine)
                .sum();
        stats.put("totalOutstandingFines", totalFines);

        return stats;
    }

    /**
     * Generates a report of popular books.
     *
     * @param limit Number of books to include
     * @return List of books sorted by borrow count
     */
    public List<Book> getPopularBooks(int limit) {
        return books.values().stream()
                .sorted((b1, b2) -> Integer.compare(b2.getBorrowedCount(), b1.getBorrowedCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ==================== Persistence ====================

    /**
     * Saves library data to a file.
     *
     * @param filename File path
     * @throws IOException if saving fails
     */
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    /**
     * Loads library data from a file.
     *
     * @param filename File path
     * @return Loaded Library instance
     * @throws IOException if loading fails
     * @throws ClassNotFoundException if class not found
     */
    public static Library loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Library loaded = (Library) ois.readObject();
            // Update the singleton instance
            instance = loaded;
            return loaded;
        }
    }

    // ==================== Getters ====================

    public String getLibraryName() { return libraryName; }
    public String getLibraryId() { return libraryId; }
    public int getBookCount() { return books.size(); }
    public int getMemberCount() { return members.size(); }
    public int getTransactionCount() { return transactions.size(); }

    @Override
    public String toString() {
        return String.format(
                "Library[Name='%s', ID='%s', Books=%d, Members=%d, Transactions=%d]",
                libraryName, libraryId, books.size(), members.size(), transactions.size()
        );
    }
}