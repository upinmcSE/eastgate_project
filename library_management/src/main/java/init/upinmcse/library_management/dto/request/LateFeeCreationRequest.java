package init.upinmcse.library_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LateFeeCreationRequest {
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than 0")
    private int userId;

    @NotNull(message = "Book ID is required")
    @Positive(message = "Book ID must be greater than 0")
    private int bookId;

    @DecimalMin(value = "0.0", message = "Fee must be greater than or equal to 0")
    private double fee;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
