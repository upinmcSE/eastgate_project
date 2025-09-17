package init.upinmcse.library_management.service.impl;

import init.upinmcse.library_management.constant.UserStatus;
import init.upinmcse.library_management.dto.request.UserCreationRequest;
import init.upinmcse.library_management.dto.response.UserResponse;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.mapper.UserMapper;
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
    UserMapper userMapper;


    @Override
    public User getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user;
    }

    @Override
    public UserResponse getUserById(int userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return this.userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse addUser(UserCreationRequest request) {
        return null;
    }

    @Override
    public void deleteUser(int userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setStatus(UserStatus.DISABLED);
    }


}
