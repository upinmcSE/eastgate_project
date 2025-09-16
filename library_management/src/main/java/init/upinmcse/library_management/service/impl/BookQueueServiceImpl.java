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
    public BorrowQueueResponse registerBookQueue(BorrowQueueRequest request) {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = this.bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Optional<BorrowQueue> borrowQueue = this.bookQueueRepository.findByBorrowQueueByBookIdAndUserId(book.getId(), user.getId(), BorrowQueueStatus.PENDING);

        if(borrowQueue.isPresent()) {
            throw new EntityNotFoundException("Borrow queue already exists");
        }

        BorrowQueue newBorrowQueue = BorrowQueue.builder()
                .book(book)
                .user(user)
                .status(BorrowQueueStatus.PENDING)
                .build();

        return this.borrowQueueMapper.toBorrowQueueResponse(this.bookQueueRepository.save(newBorrowQueue));
    }

    @Override
    public List<BorrowQueueResponse> getBookQueues() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<BorrowQueue> borrowQueueList = this.bookQueueRepository.findAllByUserIdAndStatus(user.getId(), BorrowQueueStatus.PENDING);

        return borrowQueueList.stream().map(this.borrowQueueMapper::toBorrowQueueResponse).toList();
    }

    @Override
    public void cancelBookQueue(BorrowQueueRequest request) {
        BorrowQueue borrowQueue = this.bookQueueRepository.findByBorrowQueueByBookIdAndUserId(request.getBookId(), request.getUserId(), BorrowQueueStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException("Borrow queue not found"));

        borrowQueue.setStatus(BorrowQueueStatus.BORROWED);
        this.bookQueueRepository.save(borrowQueue);
    }
}
