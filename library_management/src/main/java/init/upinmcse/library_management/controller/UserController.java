package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.response.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getInfo(@PathVariable("id") int userId) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .message("Get information successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
