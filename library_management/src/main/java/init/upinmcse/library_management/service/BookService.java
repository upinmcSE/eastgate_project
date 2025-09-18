package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.response.BookResponse;

public interface BookService {
    BookResponse addBook(BookCreationRequest request);
    BookResponse getBookById(int bookId);
    PageResponse<BookResponse> getAllBooks(int page, int size);
    PageResponse<BookResponse> searchBooksByTitle(String title, int page, int size);
    PageResponse<BookResponse> searchBooksByAuthor(String author, int page, int size);
    PageResponse<BookResponse> searchBooksByGenre(String genre, int page, int size);
    void deleteBook(int bookId);
}
