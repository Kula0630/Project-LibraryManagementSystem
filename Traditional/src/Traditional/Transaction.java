package Traditional;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private Book book;
    private Member member;
    private Date borrowDate;
    private Date returnDate;
    private boolean isReturned;
    private double fineAmount;

    public Transaction(String transactionId, Book book, Member member, Date borrowDate) {
        this.transactionId = transactionId;
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.isReturned = false;
        this.fineAmount = 0.0;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
        this.isReturned = true;
        calculateFine();
    }

    private void calculateFine() {
        // Simple fine calculation: $1 per day overdue (assuming 14 days borrowing period)
        long diffInMillies = Math.abs(returnDate.getTime() - borrowDate.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

        if (diffInDays > 14) {
            long overdueDays = diffInDays - 14;
            fineAmount = overdueDays * 1.0;
        }
    }

    @Override
    public String toString() {
        return String.format("Transaction: %s | Book: %s | Member: %s | Borrowed: %s | Returned: %s",
                transactionId, book.getTitle(), member.getName(),
                borrowDate.toString(), isReturned ? "Yes" : "No");
    }
}