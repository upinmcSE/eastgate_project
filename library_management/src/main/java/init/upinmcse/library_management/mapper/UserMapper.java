package init.upinmcse.library_management.mapper;

import init.upinmcse.library_management.dto.response.UserResponse;
import init.upinmcse.library_management.model.Role;
import init.upinmcse.library_management.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .roles(user.getRoles().stream().map(Role::getRoleName).toList())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender().toString())
                .status(user.getStatus())
                .build();
    }
}
