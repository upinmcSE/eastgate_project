package init.upinmcse.library_management.service;

import com.nimbusds.jose.JOSEException;
import init.upinmcse.library_management.dto.request.LoginRequest;
import init.upinmcse.library_management.dto.request.RefreshTokenRequest;
import init.upinmcse.library_management.dto.request.UserCreationRequest;
import init.upinmcse.library_management.dto.response.AuthenticationResponse;
import init.upinmcse.library_management.dto.response.UserCreationResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest request);
    UserCreationResponse addUser(UserCreationRequest request);
    void logout();
    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
