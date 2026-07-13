package AI_assisted;

import java.io.Serializable;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a library member with borrowing history and limits.
 *
 * @author AI-Assisted Development
 * @version 2.0
 */
public class Member implements Serializable, Comparable<Member> {

    // Member types and their borrowing limits
    public enum MemberType {
        STUDENT(3, 14, 0.50),
        FACULTY(10, 30, 0.00),
        STAFF(5, 21, 0.25),
        PUBLIC(3, 14, 1.00),
        PREMIUM(8, 21, 0.50);

        private final int maxBooks;
        private final int loanDays;
        private final double finePerDay;

        MemberType(int maxBooks, int loanDays, double finePerDay) {
            this.maxBooks = maxBooks;
            this.loanDays = loanDays;
            this.finePerDay = finePerDay;
        }

        public int getMaxBooks() { return maxBooks; }
        public int getLoanDays() { return loanDays; }
        public double getFinePerDay() { return finePerDay; }
    }

    // Member properties
    private final String memberId;
    private final String name;
    private final String email;
    private final String phone;
    private MemberType memberType;
    private final LocalDate registrationDate;
    private List<Book> borrowedBooks;
    private List<BorrowingHistory> borrowingHistory;
    private boolean isActive;
    private double outstandingFine;
    private String address;
    private String emergencyContact;

    /**
     * Inner class to track borrowing history.
     */
    public static class BorrowingHistory implements Serializable {
        private final Book book;
        private final LocalDate borrowDate;
        private LocalDate returnDate;
        private boolean isReturned;
        private double finePaid;

        public BorrowingHistory(Book book, LocalDate borrowDate) {
            this.book = book;
            this.borrowDate = borrowDate;
            this.isReturned = false;
            this.finePaid = 0.0;
        }

        public Book getBook() { return book; }
        public LocalDate getBorrowDate() { return borrowDate; }
        public LocalDate getReturnDate() { return returnDate; }
        public boolean isReturned() { return isReturned; }
        public double getFinePaid() { return finePaid; }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            this.isReturned = true;
        }

        public void setFinePaid(double finePaid) {
            this.finePaid = finePaid;
        }

        public long getDaysBorrowed() {
            if (returnDate == null) {
                return ChronoUnit.DAYS.between(borrowDate, LocalDate.now());
            }
            return ChronoUnit.DAYS.between(borrowDate, returnDate);
        }

        @Override
        public String toString() {
            return String.format("Book: %s | Borrowed: %s | Returned: %s | Days: %d | Fine: $%.2f",
                    book.getTitle(), borrowDate,
                    isReturned ? returnDate : "Not Returned",
                    getDaysBorrowed(), finePaid);
        }
    }

    /**
     * Constructor for creating a new member.
     *
     * @param memberId Unique member identifier
     * @param name Member's full name
     * @param email Member's email address
     * @param phone Member's phone number
     * @param memberType Type of membership
     */
    public Member(String memberId, String name, String email, String phone, MemberType memberType) {
        validateMemberInputs(name, email, phone);

        this.memberId = memberId;
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone.trim();
        this.memberType = memberType;
        this.registrationDate = LocalDate.now();
        this.borrowedBooks = new ArrayList<>();
        this.borrowingHistory = new ArrayList<>();
        this.isActive = true;
        this.outstandingFine = 0.0;
        this.address = "";
        this.emergencyContact = "";
    }

    /**
     * Validates member inputs.
     */
    private void validateMemberInputs(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
    }

    /**
     * Checks if member can borrow more books.
     *
     * @return true if can borrow, false otherwise
     */
    public boolean canBorrow() {
        return isActive &&
                outstandingFine == 0 &&
                borrowedBooks.size() < memberType.getMaxBooks();
    }

    /**
     * Borrows a book.
     *
     * @param book The book to borrow
     * @return true if borrowing successful
     * @throws LibraryException if borrowing not possible
     */
    public boolean borrowBook(Book book) throws LibraryException {
        if (!isActive) {
            throw new LibraryException(
                    LibraryException.ErrorType.MEMBER_INACTIVE,
                    "Member account is inactive"
            );
        }

        if (outstandingFine > 0) {
            throw new LibraryException(
                    LibraryException.ErrorType.BORROW_LIMIT_EXCEEDED,
                    "Please pay outstanding fine: $" + outstandingFine
            );
        }

        if (borrowedBooks.size() >= memberType.getMaxBooks()) {
            throw new LibraryException(
                    LibraryException.ErrorType.BORROW_LIMIT_EXCEEDED,
                    "Borrowing limit reached. Max: " + memberType.getMaxBooks()
            );
        }

        if (!book.borrowCopy()) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_AVAILABLE,
                    "Book is not available"
            );
        }

        borrowedBooks.add(book);
        borrowingHistory.add(new BorrowingHistory(book, LocalDate.now()));
        return true;
    }

    /**
     * Returns a borrowed book.
     *
     * @param book The book to return
     * @return true if return successful
     * @throws LibraryException if book was not borrowed
     */
    public boolean returnBook(Book book) throws LibraryException {
        if (!borrowedBooks.contains(book)) {
            throw new LibraryException(
                    LibraryException.ErrorType.BOOK_NOT_FOUND,
                    "This book was not borrowed by the member"
            );
        }

        // Remove from borrowed books
        borrowedBooks.remove(book);
        book.returnCopy();

        // Update borrowing history
        LocalDate returnDate = LocalDate.now();
        for (BorrowingHistory history : borrowingHistory) {
            if (history.getBook().equals(book) && !history.isReturned()) {
                history.setReturnDate(returnDate);

                // Calculate fine
                long daysBorrowed = history.getDaysBorrowed();
                long overdueDays = Math.max(0, daysBorrowed - memberType.getLoanDays());
                if (overdueDays > 0) {
                    double fine = overdueDays * memberType.getFinePerDay();
                    outstandingFine += fine;
                    history.setFinePaid(fine);
                }
                break;
            }
        }

        return true;
    }

    /**
     * Gets all borrowing history.
     *
     * @return List of borrowing history
     */
    public List<BorrowingHistory> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    /**
     * Gets currently borrowed books.
     *
     * @return List of borrowed books
     */
    public List<Book> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    /**
     * Pays outstanding fine.
     *
     * @param amount Amount to pay
     * @return true if payment successful
     */
    public boolean payFine(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > outstandingFine) {
            amount = outstandingFine;
        }
        outstandingFine -= amount;
        return true;
    }

    /**
     * Gets member statistics.
     *
     * @return Formatted statistics string
     */
    public String getStatistics() {
        long totalBorrowed = borrowingHistory.size();
        long totalReturned = borrowingHistory.stream()
                .filter(BorrowingHistory::isReturned)
                .count();

        return String.format(
                "Member: %s | Type: %s | Books: %d/%d | Total Borrowed: %d | Fines: $%.2f",
                name, memberType, borrowedBooks.size(), memberType.getMaxBooks(),
                totalBorrowed, outstandingFine
        );
    }

    // ==================== Getters and Setters ====================

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public MemberType getMemberType() { return memberType; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public boolean isActive() { return isActive; }
    public double getOutstandingFine() { return outstandingFine; }
    public String getAddress() { return address; }
    public String getEmergencyContact() { return emergencyContact; }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setAddress(String address) {
        this.address = address != null ? address.trim() : "";
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact != null ? emergencyContact.trim() : "";
    }

    @Override
    public String toString() {
        return String.format(
                "Member[ID=%s, Name='%s', Email='%s', Type=%s, Active=%s, %s]",
                memberId, name, email, memberType, isActive ? "Yes" : "No",
                getStatistics()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(memberId, member.memberId) ||
                Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, email);
    }

    @Override
    public int compareTo(Member other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}
