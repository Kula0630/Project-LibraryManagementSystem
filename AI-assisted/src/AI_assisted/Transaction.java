package AI_assisted;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a book borrowing transaction.
 *
 * @author AI-Assisted Development
 * @version 1.0
 */
public class Transaction implements Serializable {

    private final String transactionId;
    private final Book book;
    private final Member member;
    private final LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private double fineAmount;
    private String notes;

    /**
     * Constructor for a new transaction.
     *
     * @param transactionId Unique transaction identifier
     * @param book The book being borrowed
     * @param member The member borrowing the book
     * @param borrowDate Date of borrowing
     */
    public Transaction(String transactionId, Book book, Member member, LocalDate borrowDate) {
        this.transactionId = transactionId;
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.returnDate = null;
        this.isReturned = false;
        this.fineAmount = 0.0;
        this.notes = "";
    }

    /**
     * Calculates fine for overdue books.
     *
     * @return The calculated fine amount
     */
    public double calculateFine() {
        if (!isReturned || returnDate == null) {
            return 0.0;
        }

        // Get loan period from member type
        int loanDays = member.getMemberType().getLoanDays();
        double fineRate = member.getMemberType().getFinePerDay();

        // Calculate days borrowed
        long daysBorrowed = ChronoUnit.DAYS.between(borrowDate, returnDate);

        // Calculate overdue days
        long overdueDays = Math.max(0, daysBorrowed - loanDays);

        // Calculate fine
        fineAmount = overdueDays * fineRate;
        return fineAmount;
    }

    /**
     * Gets the number of days the book was borrowed.
     *
     * @return Days borrowed
     */
    public long getDaysBorrowed() {
        if (returnDate != null) {
            return ChronoUnit.DAYS.between(borrowDate, returnDate);
        }
        return ChronoUnit.DAYS.between(borrowDate, LocalDate.now());
    }

    /**
     * Gets the number of overdue days.
     *
     * @return Overdue days
     */
    public long getOverdueDays() {
        int loanDays = member.getMemberType().getLoanDays();
        long daysBorrowed = getDaysBorrowed();
        return Math.max(0, daysBorrowed - loanDays);
    }

    /**
     * Checks if the book is overdue.
     *
     * @return true if overdue
     */
    public boolean isOverdue() {
        return getOverdueDays() > 0;
    }

    /**
     * Gets transaction status.
     *
     * @return Formatted status string
     */
    public String getStatus() {
        if (isReturned) {
            return "Returned";
        }
        if (isOverdue()) {
            return "Overdue (" + getOverdueDays() + " days)";
        }
        return "Active";
    }

    // ==================== Getters and Setters ====================

    public String getTransactionId() { return transactionId; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return isReturned; }
    public double getFineAmount() { return fineAmount; }
    public String getNotes() { return notes; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.isReturned = true;
        calculateFine();
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setNotes(String notes) {
        this.notes = notes != null ? notes.trim() : "";
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction[ID=%s, Book='%s', Member='%s', Borrowed=%s, Status=%s, Fine=$%.2f]",
                transactionId, book.getTitle(), member.getName(),
                borrowDate, getStatus(), fineAmount
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}