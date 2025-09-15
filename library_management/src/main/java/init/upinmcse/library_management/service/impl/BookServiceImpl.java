package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.BookStatus;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.exception.EntityNotFound;
import init.upinmcse.library_management.mapper.BookMapper;
import init.upinmcse.library_management.model.Author;
import init.upinmcse.library_management.model.Book;
import init.upinmcse.library_management.model.Genre;
import init.upinmcse.library_management.repository.AuthorRepository;
import init.upinmcse.library_management.repository.BookRepository;
import init.upinmcse.library_management.repository.GenreRepository;
import init.upinmcse.library_management.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BookService")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    GenreRepository genreRepository;
    BookMapper bookMapper;

    @Override
    public BookResponse addBook(BookCreationRequest request) {
        StringBuilder rawCode = new StringBuilder();

        List<Author> authors = new ArrayList<>();
        for(int i = 0; i < request.getAuthorIds().size(); i++) {
            Author author = authorRepository.findById(request.getAuthorIds().get(i))
                    .orElseThrow(() -> new EntityNotFound("Author not found"));
            authors.add(author);
            rawCode.append(author.getId());
        }

        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < request.getGenreIds().size(); i++) {
            Genre genre = genreRepository.findById(request.getGenreIds().get(i))
                    .orElseThrow(() -> new EntityNotFound("Genre not found"));
            genres.add(genre);
            rawCode.append(genre.getId());
        }

        rawCode.append(request.getTitle());
        rawCode.append(request.getPublishYear());

        String bookCode = generateBookCode(rawCode.toString());

        Optional<Book> book = this.bookRepository.findByBookCode(bookCode);

        if (book.isPresent()) {
            return bookMapper.toBookResponse(book.get());
        }

        Book newBook = bookMapper.toBook(request);
        newBook.setBookCode(bookCode);
        newBook.setAuthors(authors);
        newBook.setGenres(genres);

        newBook = this.bookRepository.save(newBook);

        return bookMapper.toBookResponse(newBook);
    }

    @Override
    public BookResponse getBookById(int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFound("Book not found"));
        return bookMapper.toBookResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return this.bookRepository.findAll().stream().map(bookMapper::toBookResponse).toList();
    }

    @Override
    public void deleteBook(int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFound("Book not found"));

        book.setStatus(BookStatus.DISABLE);
        this.bookRepository.save(book);
    }

    private String generateBookCode(String rawCode){
        return Base64.getEncoder().encodeToString(rawCode.getBytes());
    }
}
