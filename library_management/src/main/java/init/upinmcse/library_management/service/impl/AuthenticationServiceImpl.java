package init.upinmcse.library_management.service.impl;

import com.nimbusds.jose.JOSEException;
import init.upinmcse.library_management.constant.Gender;
import init.upinmcse.library_management.constant.RoleType;
import init.upinmcse.library_management.constant.TokenType;
import init.upinmcse.library_management.constant.UserStatus;
import init.upinmcse.library_management.dto.request.LoginRequest;
import init.upinmcse.library_management.dto.request.RefreshTokenRequest;
import init.upinmcse.library_management.dto.request.UserCreationRequest;
import init.upinmcse.library_management.dto.response.AuthenticationResponse;
import init.upinmcse.library_management.dto.response.UserCreationResponse;
import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.exception.UsernamePasswordException;
import init.upinmcse.library_management.mapper.AuthenticationMapper;
import init.upinmcse.library_management.model.Role;
import init.upinmcse.library_management.model.User;
import init.upinmcse.library_management.repository.RoleRepository;
import init.upinmcse.library_management.repository.UserRepository;
import init.upinmcse.library_management.service.AuthenticationService;
import init.upinmcse.library_management.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "AuthenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    JwtUtil jwtUtil;
    PasswordEncoder passwordEncoder;
    AuthenticationMapper authenticationMapper;

    @NonFinal
    String defaultPassword = "12345678";

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = this.userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernamePasswordException("Username or password is incorrect"));

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isAuthenticated) {
            throw new UsernamePasswordException("Username or password is incorrect");
        }

        String accessToken = jwtUtil.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.generateToken(user, TokenType.REFRESH_TOKEN);

        user.setRefreshToken(refreshToken);
        user = userRepository.save(user);

        return authenticationMapper.toAuthenticationResponse(user, accessToken, refreshToken);
    }

    @Override
    public UserCreationResponse addUser(UserCreationRequest request) {
        Role role = this.roleRepository.findByRoleName(RoleType.PATRON.toString())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        Optional<User> user = this.userRepository.findByEmail(request.getEmail());

        if(user.isPresent()) {
            throw new EntityNotFoundException("User already exists");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(defaultPassword))
                .age(request.getAge())
                .gender(Gender.valueOf(request.getGender()))
                .fullName(request.getFullName())
                .roles(Set.of(role))
                .status(UserStatus.ENABLED)
                .refreshToken(null)
                .build();

        newUser = this.userRepository.save(newUser);

        return this.authenticationMapper.toUserCreationResponse(newUser, defaultPassword);
    }

    @Override
    public void logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setRefreshToken(null);
        this.userRepository.save(user);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        jwtUtil.validateToken(request.getRefreshToken(), true);

        String username = jwtUtil.extractUsername(request.getRefreshToken());

        // check user and refresh token exist
        User user = this.userRepository.findByEmailAndRefreshToken(username, request.getRefreshToken())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String accessToken = jwtUtil.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.generateToken(user, TokenType.REFRESH_TOKEN);

        user.setRefreshToken(refreshToken);
        user = this.userRepository.save(user);

        return this.authenticationMapper.toAuthenticationResponse(user, accessToken, refreshToken);
    }
}
