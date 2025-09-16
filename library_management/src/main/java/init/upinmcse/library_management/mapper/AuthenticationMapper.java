package init.upinmcse.library_management.mapper;

import init.upinmcse.library_management.dto.response.AuthenticationResponse;
import init.upinmcse.library_management.dto.response.UserCreationResponse;
import init.upinmcse.library_management.dto.response.UserResponse;
import init.upinmcse.library_management.model.Role;
import init.upinmcse.library_management.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public AuthenticationResponse toAuthenticationResponse(User user, String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponse.builder()
                        .userId(user.getId())
                        .fullName(user.getFullName())
                        .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                        .email(user.getEmail())
                        .age(user.getAge())
                        .gender(user.getGender().toString())
                        .status(user.getStatus())
                        .build())
                .build();
    }

    public UserCreationResponse toUserCreationResponse(User user, String defaultPassword) {
        return UserCreationResponse.builder()
                .user(UserResponse.builder()
                        .userId(user.getId())
                        .fullName(user.getFullName())
                        .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                        .status(user.getStatus())
                        .gender(user.getGender().toString())
                        .email(user.getEmail())
                        .age(user.getAge())
                        .build())
                .defaultPassword(defaultPassword)
                .build();
    }
}
