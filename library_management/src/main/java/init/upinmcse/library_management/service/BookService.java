package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.dto.response.BorrowBookResponse;

import java.util.List;

public interface BookService {
    BookResponse addBook(BookCreationRequest request);
    BorrowBookResponse borrowBook(BorrowBookRequest request);
    BorrowBookResponse returnBook(BorrowBookRequest request);
    BookResponse getBookById(int bookId);
    List<BookResponse> getAllBooks();
    List<BorrowBookResponse> getAllBorrowedBookOfUser(int userId);
    List<BorrowBookResponse> getAllBorrowedBookOfBook(int bookId);
    List<BorrowBookResponse> getAllBorrowedBooks();
    List<BorrowBookResponse> getAllBorrowedBookOverDue();
    List<BookResponse> searchBooksByTitle(String title);
    List<BookResponse> searchBooksByAuthor(String author);
    List<BookResponse> searchBooksByGenre(String genre);
    void deleteBook(int bookId);
}
