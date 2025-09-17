package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.response.BorrowBookResponse;

import java.util.List;

public interface BorrowBookService {
    BorrowBookResponse borrowBook(BorrowBookRequest request);
    BorrowBookResponse returnBook(BorrowBookRequest request);
    List<BorrowBookResponse> getAllBorrowedBookOfUser(int userId);
    List<BorrowBookResponse> getAllBorrowedBookOfBook(int bookId);
    List<BorrowBookResponse> getAllBorrowedBooks();
    List<BorrowBookResponse> getAllBorrowedBookOverDue();
}
