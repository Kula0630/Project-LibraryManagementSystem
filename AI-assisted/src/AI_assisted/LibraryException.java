package AI_assisted;

/**
 * Custom exception class for library management system.
 * Handles various library-specific error scenarios with detailed error types.
 *
 * @author AI-Assisted Development
 * @version 1.0
 */
public class LibraryException extends Exception {

    /**
     * Enumeration of possible error types in the library system.
     */
    public enum ErrorType {
        BOOK_NOT_FOUND("Book not found in the library"),
        BOOK_NOT_AVAILABLE("Book is currently not available"),
        BOOK_ALREADY_EXISTS("Book already exists in the library"),
        MEMBER_NOT_FOUND("Member not found in the system"),
        MEMBER_ALREADY_EXISTS("Member already registered"),
        MEMBER_INACTIVE("Member account is inactive"),
        BORROW_LIMIT_EXCEEDED("Member has reached borrowing limit"),
        BOOK_ALREADY_BORROWED("Book is already borrowed by this member"),
        INVALID_INPUT("Invalid input provided"),
        DUPLICATE_TRANSACTION("Duplicate transaction detected"),
        SYSTEM_ERROR("System error occurred"),
        RESERVATION_FAILED("Failed to reserve book"),
        FINE_CALCULATION_ERROR("Error calculating fine"),
        DATABASE_ERROR("Database operation failed"),
        AUTHORIZATION_ERROR("User not authorized for this operation");

        private final String description;

        ErrorType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final ErrorType errorType;
    private final String details;
    private final String timestamp;

    /**
     * Constructor for LibraryException.
     *
     * @param errorType The type of error
     * @param message Custom error message
     */
    public LibraryException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.details = message;
        this.timestamp = new java.util.Date().toString();
    }

    /**
     * Constructor with cause.
     *
     * @param errorType The type of error
     * @param message Custom error message
     * @param cause The underlying cause
     */
    public LibraryException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.details = message;
        this.timestamp = new java.util.Date().toString();
    }

    /**
     * Gets the error type.
     *
     * @return The error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Gets the error details.
     *
     * @return The error details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Gets the timestamp when error occurred.
     *
     * @return The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s (Timestamp: %s)",
                errorType.name(), errorType.getDescription(),
                getMessage(), timestamp);
    }
}