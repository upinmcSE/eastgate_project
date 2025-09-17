package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.BookStatus;
import init.upinmcse.library_management.constant.BorrowBookStatus;
import init.upinmcse.library_management.constant.BorrowQueueStatus;
import init.upinmcse.library_management.constant.LateFeeStatus;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.dto.response.BorrowBookResponse;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.mapper.BookMapper;
import init.upinmcse.library_management.mapper.BorrowBookMapper;
import init.upinmcse.library_management.model.*;
import init.upinmcse.library_management.repository.*;
import init.upinmcse.library_management.service.BookService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    UserRepository userRepository;
    BorrowBookRepository borrowBookRepository;
    BorrowQueueRepository queueBookRepository;
    LateFeeRepository lateFeeRepository;
    BorrowBookMapper borrowBookMapper;
    BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponse addBook(BookCreationRequest request) {
        StringBuilder rawCode = new StringBuilder();

        List<Author> authors = new ArrayList<>();
        for(int i = 0; i < request.getAuthorIds().size(); i++) {
            Author author = authorRepository.findById(request.getAuthorIds().get(i))
                    .orElseThrow(() -> new EntityNotFoundException("Author not found"));
            authors.add(author);
            rawCode.append(author.getId());
        }

        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < request.getGenreIds().size(); i++) {
            Genre genre = genreRepository.findById(request.getGenreIds().get(i))
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
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

    /**
     * Handling with Pessimistic Locking
     * */
    @Override
    @Transactional
    public BorrowBookResponse borrowBook(BorrowBookRequest request) {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = this.bookRepository.findByBookIdWithPessimisticLock(request.getBookId(), BookStatus.ENABLE)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Optional<BorrowBook> borrowBook = this.borrowBookRepository.findBorrowedBookByBookIdAndUserId(
                request.getBookId(), request.getUserId(), BorrowBookStatus.BORROWED);

        if(borrowBook.isPresent()) {
            throw new EntityNotFoundException("Book already exists");
        }

        if(book.getAvailableCount() == 0){
            throw new EntityNotFoundException("Book is unavailable, you can choice join queue borrow book");
        }

        // check late fee
        Optional<LateFee> lateFee = this.lateFeeRepository.findLateFeeByUserIdAndBookIdAndStatus(user.getId(), book.getId(), LateFeeStatus.UNPAID);
        if(lateFee.isPresent()) {
            throw new EntityNotFoundException("LateFee already exists");
        }

        // check wait list
        Optional<BorrowQueue> borrowQueue = this.queueBookRepository.findBorrowQueueByBookIdAndUserId(book.getId(), user.getId(), BorrowQueueStatus.PENDING);
        if(borrowQueue.isPresent()) {
            throw new EntityNotFoundException("This Book pending borrow");
        }

        BorrowBook newBorrowBook = BorrowBook.builder()
                .user(user)
                .book(book)
                .dueDate(LocalDateTime.now().plusDays(7))
                .status(BorrowBookStatus.BORROWED)
                .build();
        newBorrowBook = this.borrowBookRepository.save(newBorrowBook);

        book.setAvailableCount(book.getAvailableCount() - 1);
        book.setBorrowedCount(book.getBorrowedCount() + 1);
        this.bookRepository.save(book);

        return this.borrowBookMapper.toBorrowBookResponse(newBorrowBook);
    }

    @Override
    @Transactional
    public BorrowBookResponse returnBook(BorrowBookRequest request) {
        BorrowBook borrowBook = this.borrowBookRepository.findBorrowedBookByBookIdAndUserId(
                request.getBookId(), request.getUserId(), BorrowBookStatus.BORROWED)
                .orElseThrow(() -> new EntityNotFoundException("Borrow Book not found"));

        Book book = this.bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        book.setAvailableCount(book.getAvailableCount() + 1);
        book.setBorrowedCount(book.getBorrowedCount() - 1);

        borrowBook.setStatus(BorrowBookStatus.RETURNED);

        bookRepository.save(book);
        borrowBook = borrowBookRepository.save(borrowBook);

        // push event message

        return this.borrowBookMapper.toBorrowBookResponse(borrowBook);
    }

    @Override
    public BookResponse getBookById(int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return bookMapper.toBookResponse(book);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return this.bookRepository.findAll().stream().map(bookMapper::toBookResponse).toList();
    }

    @Override
    public List<BorrowBookResponse> getAllBorrowedBooks() {
        return this.borrowBookRepository.findAll().stream().map(this.borrowBookMapper::toBorrowBookResponse).toList();
    }

    @Override
    public List<BorrowBookResponse> getAllBorrowedBookOfUser(int userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<BorrowBook> borrowBooks = this.borrowBookRepository.findAllByUserIdAndStatus(user.getId(), BorrowBookStatus.BORROWED);

        log.info(String.valueOf(borrowBooks.size()));

        return borrowBooks.stream().map(borrowBookMapper::toBorrowBookResponse).toList();
    }

    @Override
    public List<BookResponse> searchBooksByTitle(String title) {
        return List.of();
    }

    @Override
    public List<BookResponse> searchBooksByAuthor(String author) {
        return List.of();
    }

    @Override
    public List<BookResponse> searchBooksByGenre(String genre) {
        return List.of();
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
}
