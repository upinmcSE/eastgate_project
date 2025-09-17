package init.upinmcse.library_management.mapper;

import init.upinmcse.library_management.dto.response.BorrowBookResponse;
import init.upinmcse.library_management.model.BorrowBook;
import org.springframework.stereotype.Component;

@Component
public class BorrowBookMapper {

    public BorrowBookResponse toBorrowBookResponse(BorrowBook borrowBook) {
        return BorrowBookResponse.builder()
                .borrowBookId(borrowBook.getId())
                .bookId(borrowBook.getBook().getId())
                .userId(borrowBook.getUser().getId())
                .status(borrowBook.getStatus().toString())
                .dueDate(borrowBook.getDueDate())
                .build();
    }
}
