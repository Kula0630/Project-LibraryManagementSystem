package AI_assisted;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a book in the library system.
 * This class includes comprehensive book management features including
 * multiple copies, rating system, and detailed book information.
 *
 * @author AI-Assisted Development
 * @version 2.0
 */
public class Book implements Serializable, Comparable<Book> {

    // Immutable properties (set once, never changed)
    private final String bookId;
    private final String title;
    private final String author;
    private final String isbn;
    private final int publicationYear;
    private final String category;
    private final String publisher;
    private final String language;

    // Mutable properties
    private int totalCopies;
    private int availableCopies;
    private int borrowedCount;
    private double rating;
    private int ratingCount;
    private String location;
    private String description;
    private boolean isActive;

    /**
     * Constructor for creating a new book instance.
     *
     * @param bookId Unique identifier for the book
     * @param title Title of the book
     * @param author Author of the book
     * @param isbn International Standard Book Number
     * @param publicationYear Year of publication
     * @param category Book category/genre
     * @param publisher Publisher name
     * @param totalCopies Total number of copies
     * @param language Language of the book
     */
    public Book(String bookId, String title, String author, String isbn,
                int publicationYear, String category, String publisher,
                int totalCopies, String language) {

        // Validate inputs
        validateBookInputs(title, author, isbn, publicationYear, totalCopies);

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.category = category != null ? category.trim() : "Uncategorized";
        this.publisher = publisher != null ? publisher.trim() : "Unknown";
        this.language = language != null ? language.trim() : "English";
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.borrowedCount = 0;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.location = "Main Section";
        this.description = "";
        this.isActive = true;
    }

    /**
     * Validates book inputs.
     *
     * @param title Book title
     * @param author Book author
     * @param isbn ISBN number
     * @param year Publication year
     * @param copies Number of copies
     * @throws IllegalArgumentException if any input is invalid
     */
    private void validateBookInputs(String title, String author, String isbn,
                                    int year, int copies) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (year < 0 || year > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Invalid publication year");
        }
        if (copies <= 0) {
            throw new IllegalArgumentException("Total copies must be greater than 0");
        }
    }

    /**
     * Borrows a copy of the book if available.
     *
     * @return true if borrowing successful, false otherwise
     */
    public synchronized boolean borrowCopy() {
        if (!isActive) {
            return false;
        }
        if (availableCopies > 0) {
            availableCopies--;
            borrowedCount++;
            return true;
        }
        return false;
    }

    /**
     * Returns a borrowed copy of the book.
     *
     * @return true if return successful, false otherwise
     */
    public synchronized boolean returnCopy() {
        if (!isActive) {
            return false;
        }
        if (availableCopies < totalCopies) {
            availableCopies++;
            return true;
        }
        return false;
    }

    /**
     * Checks if the book is available for borrowing.
     *
     * @return true if at least one copy is available
     */
    public boolean isAvailable() {
        return isActive && availableCopies > 0;
    }

    /**
     * Updates the book rating.
     *
     * @param newRating New rating value (1-5)
     * @throws IllegalArgumentException if rating is invalid
     */
    public void updateRating(double newRating) {
        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        // Calculate weighted average
        this.rating = ((this.rating * this.ratingCount) + newRating) / (this.ratingCount + 1);
        this.ratingCount++;
    }

    /**
     * Adds more copies of the book.
     *
     * @param additionalCopies Number of additional copies
     * @throws IllegalArgumentException if additionalCopies is negative
     */
    public void addCopies(int additionalCopies) {
        if (additionalCopies < 0) {
            throw new IllegalArgumentException("Additional copies cannot be negative");
        }
        this.totalCopies += additionalCopies;
        this.availableCopies += additionalCopies;
    }

    /**
     * Removes copies of the book.
     *
     * @param copiesToRemove Number of copies to remove
     * @throws IllegalArgumentException if copiesToRemove is invalid
     */
    public void removeCopies(int copiesToRemove) {
        if (copiesToRemove < 0) {
            throw new IllegalArgumentException("Copies to remove cannot be negative");
        }
        if (copiesToRemove > availableCopies) {
            throw new IllegalArgumentException("Cannot remove more copies than available");
        }
        this.totalCopies -= copiesToRemove;
        this.availableCopies -= copiesToRemove;
        if (totalCopies <= 0) {
            this.isActive = false;
        }
    }

    // ==================== Getters ====================

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }
    public String getCategory() { return category; }
    public String getPublisher() { return publisher; }
    public String getLanguage() { return language; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public int getBorrowedCount() { return borrowedCount; }
    public double getRating() { return rating; }
    public int getRatingCount() { return ratingCount; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public boolean isActive() { return isActive; }

    // ==================== Setters ====================

    public void setLocation(String location) {
        this.location = location != null ? location.trim() : "Main Section";
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Gets the availability status as a formatted string.
     *
     * @return Formatted availability string
     */
    public String getAvailabilityStatus() {
        if (!isActive) {
            return "Inactive";
        }
        if (availableCopies == 0) {
            return "Out of Stock";
        }
        if (availableCopies == totalCopies) {
            return "All Copies Available";
        }
        return String.format("%d/%d Available", availableCopies, totalCopies);
    }

    /**
     * Gets book statistics as a formatted string.
     *
     * @return Formatted statistics string
     */
    public String getStatistics() {
        return String.format(
                "Total: %d | Available: %d | Borrowed: %d | Rating: %.1f/5 (%d votes)",
                totalCopies, availableCopies, borrowedCount, rating, ratingCount
        );
    }

    @Override
    public String toString() {
        return String.format(
                "Book[ID=%s, Title='%s', Author='%s', ISBN='%s', Year=%d, Category='%s', %s, %s]",
                bookId, title, author, isbn, publicationYear, category,
                getAvailabilityStatus(), getStatistics()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(bookId, book.bookId) ||
                Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, isbn);
    }

    @Override
    public int compareTo(Book other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    /**
     * Creates a brief summary of the book.
     *
     * @return Brief summary string
     */
    public String toBriefString() {
        return String.format("%s by %s (%d)", title, author, publicationYear);
    }
}