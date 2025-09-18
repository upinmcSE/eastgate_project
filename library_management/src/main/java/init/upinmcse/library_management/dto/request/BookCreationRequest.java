package init.upinmcse.library_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreationRequest {
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotEmpty(message = "At least one author is required")
    private List<@NotNull(message = "Author ID cannot be null") @Positive(message = "Author ID must be positive") Integer> authorIds;

    @NotEmpty(message = "At least one genre is required")
    private List<@NotNull(message = "Genre ID cannot be null") @Positive(message = "Genre ID must be positive") Integer> genreIds;

    @Min(value = 1000, message = "Publish year must be after 1000")
    @Max(value = 2025, message = "Publish year must not exceed 2025")
    private int publishYear;

    @Min(value = 1, message = "Available count must be greater than 0")
    @Max(value = 100, message = "Available count must be smaller than 101")
    private int availableCount;
}
