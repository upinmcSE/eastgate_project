package init.upinmcse.library_management.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
