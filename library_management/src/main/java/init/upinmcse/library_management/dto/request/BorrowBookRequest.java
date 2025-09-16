package init.upinmcse.library_management.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowBookRequest {
    int userId;
    int bookId;
    int duration;
}
