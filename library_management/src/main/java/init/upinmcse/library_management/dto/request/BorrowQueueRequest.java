package init.upinmcse.library_management.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowQueueRequest {
    private int bookId;
    private int userId;
}
