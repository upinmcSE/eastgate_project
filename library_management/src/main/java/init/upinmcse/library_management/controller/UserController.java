package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User", description = "User management APIs")
public class UserController {

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user information",
            description = "Retrieve the profile information of a specific user by user ID. "
                    + "Accessible by ADMIN or the user themselves."
    )
    public ResponseEntity<ApiResponse<UserResponse>> getInfo(@PathVariable("id") int userId) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .message("Get information successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update user information",
            description = "Update the profile details of a specific user by user ID. "
                    + "Accessible by ADMIN or the user themselves."
    )
    public ResponseEntity<ApiResponse<Void>> changeInfo(
            @PathVariable("id") String userId

    ){
        return null;
    }
}
