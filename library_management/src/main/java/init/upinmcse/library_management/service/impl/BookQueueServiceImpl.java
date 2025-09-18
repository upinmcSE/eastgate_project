package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.BorrowQueueStatus;
import init.upinmcse.library_management.dto.PageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PageResponse<BorrowQueueResponse> getBookQueueOfBook(int bookId, int page, int size) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Pageable pageable = buildPageable(page, size);
        return toPageResponse(this.bookQueueRepository.findAllByBookIdAndStatus(book.getId(), BorrowQueueStatus.PENDING, pageable), page);
    }

    @Override
    public PageResponse<BorrowQueueResponse> getBookQueueOfUser(int userId, int page, int size) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Pageable pageable = buildPageable(page, size);
        return toPageResponse(this.bookQueueRepository.findAllByUserIdAndStatus(user.getId(), BorrowQueueStatus.PENDING, pageable), page);
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

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }

    private PageResponse<BorrowQueueResponse> toPageResponse(Page<BorrowQueue> pageData, int page) {
        List<BorrowQueueResponse> queueList = pageData.getContent()
                .stream()
                .map(this.borrowQueueMapper::toBorrowQueueResponse)
                .toList();

        return PageResponse.<BorrowQueueResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalRecords(pageData.getTotalElements())
                .data(queueList)
                .build();
    }
}
