package init.upinmcse.library_management.mapper;

import init.upinmcse.library_management.dto.response.BorrowQueueResponse;
import init.upinmcse.library_management.model.BorrowQueue;
import org.springframework.stereotype.Component;

@Component
public class BorrowQueueMapper {
    public BorrowQueueResponse toBorrowQueueResponse(BorrowQueue bq) {
        return BorrowQueueResponse.builder()
                .borrowQueueId(bq.getId())
                .bookId(bq.getBook().getId())
                .userId(bq.getUser().getId())
                .status(bq.getStatus().toString())
                .build();
    }
}
