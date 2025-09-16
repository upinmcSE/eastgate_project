package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.UserCreationRequest;
import init.upinmcse.library_management.dto.response.UserResponse;
import init.upinmcse.library_management.model.User;

public interface UserService {
    User getUserByEmail(String email);
    UserResponse getUserById(int id);
    UserResponse addUser(UserCreationRequest request);
    void deleteUser(int id);
}
