package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.request.LateFeeCreationRequest;
import init.upinmcse.library_management.dto.response.LateFeeResponse;
import init.upinmcse.library_management.service.LateFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/late-fee")
@RequiredArgsConstructor
@Tag(name = "Late Fee", description = "Late Fee management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LateFeeController {
    LateFeeService lateFeeService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<LateFeeResponse>> createLateFee(@Valid @RequestBody LateFeeCreationRequest request){
        ApiResponse<LateFeeResponse> apiResponse = ApiResponse.<LateFeeResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Create late fee successfully")
                .data(lateFeeService.createLateFee(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<LateFeeResponse>> paidLateFee(@PathVariable("id") int lateFeeId){
        ApiResponse<LateFeeResponse> apiResponse = ApiResponse.<LateFeeResponse>builder()
                .message("Paid late fee successfully")
                .data(lateFeeService.paidLateFee(lateFeeId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<LateFeeResponse>>> getLateFees(){
        ApiResponse<List<LateFeeResponse>> apiResponse = ApiResponse.<List<LateFeeResponse>>builder()
                .message("Get late fees successfully")
                .data(lateFeeService.getLateFees())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userId == #jwt.claims['userId']")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<LateFeeResponse>>> getLateFeeOfUser(
            @PathVariable("id") int userId,
            @AuthenticationPrincipal Jwt jwt
    ){
        ApiResponse<List<LateFeeResponse>> apiResponse = ApiResponse.<List<LateFeeResponse>>builder()
                .message("Get late fees of user successfully")
                .data(lateFeeService.getLateFeeOfUser(userId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
