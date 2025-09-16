package init.upinmcse.library_management.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowQueueResponse {
    private int borrowQueueId;
    private int bookId;
    private int userId;
//    private int position;
    private String status;

}
