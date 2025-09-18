package init.upinmcse.library_management.mapper;

import init.upinmcse.library_management.constant.BookStatus;
import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.model.Author;
import init.upinmcse.library_management.model.Book;
import init.upinmcse.library_management.model.Genre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .bookCode(book.getBookCode())
                .publishYear(book.getPublishYear())
                .availableCount(book.getAvailableCount())
                .borrowedCount(book.getBorrowedCount())
                .status(book.getStatus().toString())
                .authors(book.getAuthors().stream().map(Author::getFullName).toList())
                .genres(book.getGenres().stream().map(Genre::getGenreName).toList())
                .build();
    }

    public Book toBook(BookCreationRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .publishYear(request.getPublishYear())
                .availableCount(request.getAvailableCount())
                .borrowedCount(0)
                .status(BookStatus.ENABLE)
                .build();
    }
}
