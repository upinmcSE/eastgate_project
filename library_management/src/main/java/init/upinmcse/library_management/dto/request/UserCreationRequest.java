package init.upinmcse.library_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Min(value = 6, message = "Age must be non-negative")
    @Max(value = 150, message = "Age must not exceed 150")
    private int age;

    @NotBlank(message = "Gender is required")
    private String gender;
}
