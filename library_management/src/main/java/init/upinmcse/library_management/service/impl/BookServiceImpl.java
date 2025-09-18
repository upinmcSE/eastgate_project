package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.BookStatus;
import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.mapper.BookMapper;
import init.upinmcse.library_management.model.*;
import init.upinmcse.library_management.repository.*;
import init.upinmcse.library_management.service.BookService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Transactional
    public BookResponse addBook(BookCreationRequest request) {
        // Build rawCode từ authorIds + genreIds + title + publishYear
        String rawCode = Stream.concat(
                request.getAuthorIds().stream().map(String::valueOf),
                request.getGenreIds().stream().map(String::valueOf)
        ).collect(Collectors.joining())
                + request.getTitle()
                + request.getPublishYear();

        String bookCode = generateBookCode(rawCode);

        return bookRepository.findByBookCode(bookCode)
                .map(bookMapper::toBookResponse)
                .orElseGet(() -> {
                    List<Author> authors = request.getAuthorIds().stream()
                            .map(id -> authorRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Author not found")))
                            .toList();

                    List<Genre> genres = request.getGenreIds().stream()
                            .map(id -> genreRepository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Genre not found")))
                            .toList();

                    Book newBook = bookMapper.toBook(request);
                    newBook.setBookCode(bookCode);
                    newBook.setAuthors(authors);
                    newBook.setGenres(genres);

                    return bookMapper.toBookResponse(bookRepository.save(newBook));
                });
    }

    @Override
    public BookResponse getBookById(int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return bookMapper.toBookResponse(book);
    }

    @Override
    public PageResponse<BookResponse> getAllBooks(int page, int size) {
        Pageable pageable = buildPageable(page, size);
        return toPageResponse(bookRepository.findAll(pageable), page);
    }

    @Override
    public PageResponse<BookResponse> searchBooksByTitle(String title, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return PageResponse.<BookResponse>builder().build();
    }

    @Override
    public PageResponse<BookResponse> searchBooksByAuthor(String authorName, int page, int size) {
        Author author = authorRepository.findByFullName(authorName)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        Pageable pageable = buildPageable(page, size);
        return toPageResponse(bookRepository.findBooksByAuthor(BookStatus.ENABLE, author.getFullName(), pageable), page);
    }

    @Override
    public PageResponse<BookResponse> searchBooksByGenre(String genreName, int page, int size) {
        Genre genre = genreRepository.findByGenreName(genreName)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        Pageable pageable = buildPageable(page, size);
        return toPageResponse(bookRepository.findBooksByGenre(BookStatus.ENABLE, genre.getGenreName(), pageable), page);
    }

    @Override
    @Transactional
    public void deleteBook(int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        book.setStatus(BookStatus.DISABLE);
        this.bookRepository.save(book);
    }

    private String generateBookCode(String rawCode){
        return Base64.getEncoder().encodeToString(rawCode.getBytes());
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }

    private PageResponse<BookResponse> toPageResponse(Page<Book> pageData, int page) {
        List<BookResponse> bookList = pageData.getContent()
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return PageResponse.<BookResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalRecords(pageData.getTotalElements())
                .data(bookList)
                .build();
    }
}
