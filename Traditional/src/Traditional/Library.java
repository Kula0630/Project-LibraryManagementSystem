package Traditional;

import java.util.*;
import java.text.SimpleDateFormat;

public class Library {
    private String libraryName;
    private Map<String, Book> books;
    private Map<String, Member> members;
    private List<Transaction> transactions;
    private int bookCounter;
    private int memberCounter;
    private int transactionCounter;

    public Library(String libraryName) {
        this.libraryName = libraryName;
        this.books = new HashMap<>();
        this.members = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.bookCounter = 1;
        this.memberCounter = 1;
        this.transactionCounter = 1;
    }

    // Book Management
    public void addBook(String title, String author, String isbn, int year, String category) {
        String bookId = "B" + String.format("%03d", bookCounter++);
        Book book = new Book(bookId, title, author, isbn, year, category);
        books.put(bookId, book);
        System.out.println("Book added successfully! ID: " + bookId);
    }

    public boolean removeBook(String bookId) {
        Book book = books.remove(bookId);
        if (book != null) {
            System.out.println("Book removed successfully: " + book.getTitle());
            return true;
        }
        System.out.println("Book not found!");
        return false;
    }

    public Book searchBookById(String bookId) {
        return books.get(bookId);
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> searchBooksByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> searchBooksByCategory(String category) {
        List<Book> results = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getCategory().toLowerCase().contains(category.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        return available;
    }

    // Member Management
    public void addMember(String name, String email, String phone) {
        String memberId = "M" + String.format("%03d", memberCounter++);
        Member member = new Member(memberId, name, email, phone);
        members.put(memberId, member);
        System.out.println("Member added successfully! ID: " + memberId);
    }

    public boolean removeMember(String memberId) {
        Member member = members.remove(memberId);
        if (member != null) {
            System.out.println("Member removed successfully: " + member.getName());
            return true;
        }
        System.out.println("Member not found!");
        return false;
    }

    public Member searchMemberById(String memberId) {
        return members.get(memberId);
    }

    public List<Member> searchMembersByName(String name) {
        List<Member> results = new ArrayList<>();
        for (Member member : members.values()) {
            if (member.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(member);
            }
        }
        return results;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    // Transaction Management
    public void borrowBook(String bookId, String memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book is currently not available!");
            return;
        }

        if (!member.canBorrow()) {
            System.out.println("Member cannot borrow more books!");
            return;
        }

        String transactionId = "T" + String.format("%03d", transactionCounter++);
        Transaction transaction = new Transaction(transactionId, book, member, new Date());
        transactions.add(transaction);
        member.borrowBook(book);

        System.out.println("Book borrowed successfully! Transaction ID: " + transactionId);
    }

    public void returnBook(String bookId, String memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book == null || member == null) {
            System.out.println("Book or Member not found!");
            return;
        }

        // Find active transaction
        for (Transaction transaction : transactions) {
            if (transaction.getBook().equals(book) &&
                    transaction.getMember().equals(member) &&
                    !transaction.isReturned()) {

                transaction.setReturnDate(new Date());
                member.returnBook(book);
                System.out.println("Book returned successfully!");
                System.out.println("Fine: $" + transaction.getFineAmount());
                return;
            }
        }
        System.out.println("No active borrowing record found!");
    }

    public void displayAllBooks() {
        System.out.println("\n=== All Books in Library ===");
        for (Book book : books.values()) {
            System.out.println(book);
        }
    }

    public void displayAllMembers() {
        System.out.println("\n=== All Members ===");
        for (Member member : members.values()) {
            System.out.println(member);
        }
    }

    public void displayTransactions() {
        System.out.println("\n=== All Transactions ===");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    public String getLibraryName() {
        return libraryName;
    }
}
