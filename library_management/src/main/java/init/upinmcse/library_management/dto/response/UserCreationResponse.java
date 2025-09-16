package init.upinmcse.library_management.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationResponse {
    private UserResponse user;
    private String defaultPassword;
}
