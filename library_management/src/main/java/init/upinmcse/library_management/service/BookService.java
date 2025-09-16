package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse addBook(BookCreationRequest request);
    void borrowBook(BorrowBookRequest request);
    void returnBook(BorrowBookRequest request);
    BookResponse getBookById(int bookId);
    List<BookResponse> getAllBooks();
    List<BookResponse> searchBooksByTitle(String title);
    List<BookResponse> searchBooksByAuthor(String author);
    List<BookResponse> searchBooksByGenre(String genre);
    void addBorrowQueueBook(BorrowBookRequest request);
    void deleteBook(int bookId);
}
