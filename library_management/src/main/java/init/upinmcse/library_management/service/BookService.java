package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse addBook(BookCreationRequest request);
    BookResponse getBookById(int bookId);
    List<BookResponse> getAllBooks();
    void deleteBook(int bookId);
}
