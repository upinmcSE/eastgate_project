package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.BorrowQueueStatus;
import init.upinmcse.library_management.dto.request.BorrowQueueRequest;
import init.upinmcse.library_management.dto.response.BorrowQueueResponse;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.mapper.BorrowQueueMapper;
import init.upinmcse.library_management.model.Book;
import init.upinmcse.library_management.model.BorrowQueue;
import init.upinmcse.library_management.model.User;
import init.upinmcse.library_management.repository.BookRepository;
import init.upinmcse.library_management.repository.BorrowQueueRepository;
import init.upinmcse.library_management.repository.UserRepository;
import init.upinmcse.library_management.service.BookQueueService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookQueueServiceImpl implements BookQueueService {
    BorrowQueueRepository bookQueueRepository;
    UserRepository userRepository;
    BookRepository bookRepository;
    BorrowQueueMapper borrowQueueMapper;

    @Override
    @Transactional
    public BorrowQueueResponse registerBookQueue(BorrowQueueRequest request) {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = this.bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Optional<BorrowQueue> borrowQueue = this.bookQueueRepository.findBorrowQueueByBookIdAndUserId(book.getId(), user.getId(), BorrowQueueStatus.PENDING);

        if(borrowQueue.isPresent()) {
            throw new EntityNotFoundException("Borrow queue already exists");
        }

        BorrowQueue newBorrowQueue = BorrowQueue.builder()
                .book(book)
                .user(user)
                .duration(request.getDuration())
                .status(BorrowQueueStatus.PENDING)
                .build();

        return this.borrowQueueMapper.toBorrowQueueResponse(this.bookQueueRepository.save(newBorrowQueue));
    }

    @Override
    public List<BorrowQueueResponse> getBookQueueOfBook(int bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        List<BorrowQueue> borrowQueueList = this.bookQueueRepository.findAllByBookIdAndStatus(book.getId(), BorrowQueueStatus.PENDING);

        return borrowQueueList.stream().map(this.borrowQueueMapper::toBorrowQueueResponse).toList();
    }

    @Override
    public List<BorrowQueueResponse> getBookQueueOfUser(int userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<BorrowQueue> borrowQueueList = this.bookQueueRepository.findAllByUserIdAndStatus(user.getId(), BorrowQueueStatus.PENDING);

        return borrowQueueList.stream().map(this.borrowQueueMapper::toBorrowQueueResponse).toList();
    }

    @Override
    @Transactional
    public boolean changeStatus(int borrowQueueId) {
        Optional<BorrowQueue> borrowQueue = this.bookQueueRepository.findBorrowQueueByIdAndStatus(borrowQueueId, BorrowQueueStatus.PENDING);
        if(borrowQueue.isEmpty()) {
            return false;
        }
        borrowQueue.get().setStatus(BorrowQueueStatus.COMPLETED);
        this.bookQueueRepository.save(borrowQueue.get());
        return true;
    }

    @Override
    @Transactional
    public void cancelBookQueue(int borrowQueueId) {
        BorrowQueue borrowQueue = this.bookQueueRepository.findBorrowQueueByIdAndStatus(borrowQueueId, BorrowQueueStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Borrow queue not found"));

//        borrowQueue.setStatus(BorrowQueueStatus.BORROWED);
//        this.bookQueueRepository.save(borrowQueue);
        this.bookQueueRepository.delete(borrowQueue);
    }
}
