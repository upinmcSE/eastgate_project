package init.upinmcse.library_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnBookResponse {
    private int borrowBookId;
    private int userId;
    private int bookId;
    private LocalDateTime returnDate;
}
