package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.BorrowBookStatus;
import init.upinmcse.library_management.constant.LateFeeStatus;
import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.LateFeeCreationRequest;
import init.upinmcse.library_management.dto.response.LateFeeResponse;
import init.upinmcse.library_management.event.CreateLateFeeEvent;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.mapper.LateFeeMapper;
import init.upinmcse.library_management.model.*;
import init.upinmcse.library_management.repository.BookRepository;
import init.upinmcse.library_management.repository.BorrowBookRepository;
import init.upinmcse.library_management.repository.LateFeeRepository;
import init.upinmcse.library_management.repository.UserRepository;
import init.upinmcse.library_management.service.LateFeeService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "LateFeeService")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LateFeeServiceImpl implements LateFeeService {
    LateFeeRepository lateFeeRepository;
    UserRepository userRepository;
    BookRepository bookRepository;
    BorrowBookRepository borrowBookRepository;
    LateFeeMapper lateFeeMapper;

    ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public LateFeeResponse createLateFee(LateFeeCreationRequest request) {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        Book book = this.bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book Not Found"));

        // check borrow book exits and overdue
        BorrowBook borrowBook = this.borrowBookRepository.findBorrowedBookByBookIdAndUserId(book.getId(), user.getId(), BorrowBookStatus.BORROWED)
                .orElseThrow(() -> new EntityNotFoundException("Borrowed Book Not Found"));

        if(!borrowBook.getDueDate().isBefore(LocalDateTime.now())){
            throw new EntityNotFoundException("Due Date not over");
        }

        Optional<LateFee> lateFee = this.lateFeeRepository.findLateFeeByUserIdAndBookIdAndStatus(user.getId(), book.getId(), LateFeeStatus.UNPAID);
        if(lateFee.isPresent()) {
            throw new EntityExistsException("LateFee already exists");
        }

        LateFee newLateFee = LateFee.builder()
                .book(book)
                .user(user)
                .fee(request.getFee())
                .description(request.getDescription())
                .status(LateFeeStatus.UNPAID)
                .build();

        // Option change style calculate late fee

        newLateFee = this.lateFeeRepository.save(newLateFee);

        // push event send mail cho patron
        eventPublisher.publishEvent(CreateLateFeeEvent.builder()
                        .email(user.getEmail())
                        .bookName(book.getTitle())
                        .description(newLateFee.getDescription())
                        .fee(newLateFee.getFee())
                .build());

        return this.lateFeeMapper.toLateFeeResponse(newLateFee);
    }

    @Override
    public LateFeeResponse paidLateFee(int lateFeeId) {
        LateFee lateFee = this.lateFeeRepository.findById(lateFeeId)
                .orElseThrow(() -> new EntityNotFoundException("LateFee Not Found"));

        // call return book

        lateFee.setStatus(LateFeeStatus.PAID);
        this.lateFeeRepository.save(lateFee);
        return this.lateFeeMapper.toLateFeeResponse(lateFee);
    }

    @Override
    public PageResponse<LateFeeResponse> getLateFees(int page, int size) {
        Pageable pageable = buildPageable(page, size);
        return toPageResponse(this.lateFeeRepository.findAll(pageable), page);
    }

    @Override
    public PageResponse<LateFeeResponse> getLateFeeOfUser(int userId, int page, int size) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        Pageable pageable = buildPageable(page, size);
        return toPageResponse(this.lateFeeRepository.findLateFeeByUserId(user.getId(), pageable), page);
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }

    private PageResponse<LateFeeResponse> toPageResponse(Page<LateFee> pageData, int page) {
        List<LateFeeResponse> lateFeeList = pageData.getContent()
                .stream()
                .map(this.lateFeeMapper::toLateFeeResponse)
                .toList();

        return PageResponse.<LateFeeResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalRecords(pageData.getTotalElements())
                .data(lateFeeList)
                .build();
    }
}
