package init.upinmcse.library_management.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    private String email;
    private String fullName;
    private int age;
    private String gender;
}
