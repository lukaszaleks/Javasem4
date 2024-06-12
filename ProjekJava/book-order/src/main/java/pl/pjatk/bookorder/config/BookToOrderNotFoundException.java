package pl.pjatk.bookorder.config;

public class BookToOrderNotFoundException extends RuntimeException {
    public BookToOrderNotFoundException(String message) {
        super(message);
    }
}