package init.upinmcse.library_management.controller;

import com.nimbusds.jose.JOSEException;
import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.request.LoginRequest;
import init.upinmcse.library_management.dto.request.RefreshTokenRequest;
import init.upinmcse.library_management.dto.request.UserCreationRequest;
import init.upinmcse.library_management.dto.response.AuthenticationResponse;
import init.upinmcse.library_management.dto.response.UserCreationResponse;
import init.upinmcse.library_management.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AuthenticationController")
@Tag(name = "Authentication", description = "Authentication Management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Value("${jwt.refreshExpiryMinutes}")
    @NonFinal
    private long REFRESH_EXPIRY_SECONDS;

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticate user with username and password. "
                    + "Returns access token and refresh token if credentials are valid."
    )
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .message("Login successful")
                .data(response)
                .build();

//        ResponseCookie responseCookie = ResponseCookie
//                .from("refesh_token", response.getRefreshToken())
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(REFRESH_EXPIRY_SECONDS)
//                .build();

//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register new User",
            description = "Create a new user account in the system. "
                    + "Only accessible to users with ADMIN role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserCreationResponse>> register(@Valid @RequestBody UserCreationRequest request) {
        ApiResponse<UserCreationResponse> apiResponse = ApiResponse.<UserCreationResponse>builder()
                .message("Register successful")
                .data(authenticationService.addUser(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh token",
            description = "Generate a new access token using a valid refresh token. "
                    + "Extends the session without requiring login again."
    )
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .message("Refresh successful")
                .data(authenticationService.refreshToken(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout User",
            description = "Invalidate the current user session and tokens. "
                    + "Ensures the user cannot access protected resources until they login again."
    )
    public ResponseEntity<ApiResponse<Void>> logout() {
        authenticationService.logout();
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Logout successful")
                .build();

        return ResponseEntity.ok(apiResponse);
    }


}
