package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.BorrowQueueRequest;
import init.upinmcse.library_management.dto.response.BorrowQueueResponse;

import java.util.List;

public interface BookQueueService {
    BorrowQueueResponse registerBookQueue(BorrowQueueRequest request);
    List<BorrowQueueResponse> getBookQueues();
    void cancelBookQueue(BorrowQueueRequest request);
}
