package Traditional;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private String phone;
    private List<Book> borrowedBooks;
    private int maxBooksAllowed;
    private boolean isActive;

    public Member(String memberId, String name, String email, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.borrowedBooks = new ArrayList<>();
        this.maxBooksAllowed = 5;
        this.isActive = true;
    }

    // Getters
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean canBorrow() {
        return isActive && borrowedBooks.size() < maxBooksAllowed;
    }

    public void borrowBook(Book book) {
        if (canBorrow() && book.isAvailable()) {
            borrowedBooks.add(book);
            book.setAvailable(false);
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            book.setAvailable(true);
        }
    }

    @Override
    public String toString() {
        return String.format("Member ID: %s | Name: %s | Email: %s | Books Borrowed: %d/%d",
                memberId, name, email, borrowedBooks.size(), maxBooksAllowed);
    }
}
