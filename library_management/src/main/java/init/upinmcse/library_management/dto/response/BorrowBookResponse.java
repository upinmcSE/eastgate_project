package init.upinmcse.library_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowBookResponse {
    private int borrowBookId;
    private int userId;
    private int bookId;
    private LocalDateTime dueDate;
    private String status;
}
