package init.upinmcse.library_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowQueueRequest {
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than 0")
    int userId;

    @NotNull(message = "Book ID is required")
    @Positive(message = "Book ID must be greater than 0")
    int bookId;

    @Min(value = 1, message = "Duration must be at least 1 day")
    int duration;
}
