package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.dto.request.UserCreationRequest;
import init.upinmcse.library_management.dto.response.UserResponse;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.model.User;
import init.upinmcse.library_management.repository.UserRepository;
import init.upinmcse.library_management.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;


    @Override
    public User getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return null;
    }

    @Override
    public UserResponse getUserById(int id) {
        return null;
    }

    @Override
    public UserResponse addUser(UserCreationRequest request) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }


}
