package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.response.BorrowBookResponse;

public interface BorrowBookService {
    BorrowBookResponse borrowBook(BorrowBookRequest request);
    BorrowBookResponse returnBook(BorrowBookRequest request);
    PageResponse<BorrowBookResponse> getAllBorrowedBookOfUser(int userId, int page, int size);
    PageResponse<BorrowBookResponse> getAllBorrowedBookOfBook(int bookId, int page, int size);
    PageResponse<BorrowBookResponse> getAllBorrowedBooks(int page, int size);
    PageResponse<BorrowBookResponse> getAllBorrowedBookOverDue(int page, int size);
}
