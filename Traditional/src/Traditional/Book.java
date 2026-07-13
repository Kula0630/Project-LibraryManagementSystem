package Traditional;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private int year;
    private boolean isAvailable;
    private String category;

    // Constructor
    public Book(String bookId, String title, String author, String isbn, int year, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.category = category;
        this.isAvailable = true;
    }

    // Getters and Setters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("Book ID: %s | Title: %s | Author: %s | Year: %d | " +
                        "Category: %s | Available: %s",
                bookId, title, author, year, category,
                isAvailable ? "Yes" : "No");
    }
}

