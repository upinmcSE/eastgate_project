package init.upinmcse.library_management.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelQueueRequest {
    @NotNull(message = "Book Queue ID is required")
    @Positive(message = "Book Queue ID must be greater than 0")
    private int bookQueueId;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than 0")
    private int userId;
}
