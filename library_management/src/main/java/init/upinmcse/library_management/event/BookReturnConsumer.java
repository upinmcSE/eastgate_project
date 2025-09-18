package init.upinmcse.library_management.event;

import init.upinmcse.library_management.constant.BorrowBookStatus;
import init.upinmcse.library_management.constant.BorrowQueueStatus;
import init.upinmcse.library_management.model.Book;
import init.upinmcse.library_management.model.BorrowBook;
import init.upinmcse.library_management.model.BorrowQueue;
import init.upinmcse.library_management.repository.BookRepository;
import init.upinmcse.library_management.repository.BorrowBookRepository;
import init.upinmcse.library_management.repository.BorrowQueueRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "BookReturnConsumer")
public class BookReturnConsumer {
    BorrowBookRepository borrowBookRepository;
    BorrowQueueRepository queueRepository;

    @Async
//    @EventListener
    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBorrowBookQueue(BookReturnedEvent event) {
        int bookId = event.getBookId();

        // select first patron in queue
        Optional<BorrowQueue> borrowQueue = this.queueRepository
                .findFirstByBookIdAndStatusOrderByCreatedAtAsc(
                        bookId, BorrowQueueStatus.PENDING);

        if (borrowQueue.isPresent()) {
            BorrowQueue bq = borrowQueue.get();
            Book book = bq.getBook();

            BorrowBook borrowBook = BorrowBook.builder()
                    .user(bq.getUser())
                    .book(bq.getBook())
                    .status(BorrowBookStatus.BORROWED)
                    .dueDate(LocalDateTime.now().plusDays(bq.getDuration()))
                    .build();

            book.setAvailableCount(book.getAvailableCount() - 1);
            book.setBorrowedCount(book.getBorrowedCount() + 1);

            this.borrowBookRepository.save(borrowBook);

            bq.setStatus(BorrowQueueStatus.COMPLETED);

            log.info("Book {} assigned to user {} from queue", bookId, bq.getUser().getId());
        }else {
            log.info("Borrow book for you in queue failed");
        }
    }
}
