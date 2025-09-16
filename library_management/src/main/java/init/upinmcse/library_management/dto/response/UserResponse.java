package init.upinmcse.library_management.dto.response;

import init.upinmcse.library_management.constant.UserStatus;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int userId;
    private String email;
    private String fullName;
    private String gender;
    private int age;
    private UserStatus status;
    private List<String> roles;
}
