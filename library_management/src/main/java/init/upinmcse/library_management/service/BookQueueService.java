package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BorrowQueueRequest;
import init.upinmcse.library_management.dto.response.BorrowQueueResponse;

public interface BookQueueService {
    BorrowQueueResponse registerBookQueue(BorrowQueueRequest request);
    PageResponse<BorrowQueueResponse> getBookQueueOfUser(int userId, int page, int size);
    PageResponse<BorrowQueueResponse> getBookQueueOfBook(int bookId, int page, int size);
    boolean changeStatus(int borrowQueueId);
    void cancelBookQueue(int borrowQueueId);
}
