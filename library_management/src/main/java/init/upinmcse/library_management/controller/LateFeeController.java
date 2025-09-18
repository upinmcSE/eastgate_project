package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.PageResponse;
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

@RestController
@RequestMapping("/api/v1/late-fee")
@RequiredArgsConstructor
@Tag(name = "Late Fee", description = "Late Fee management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LateFeeController {
    LateFeeService lateFeeService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create late fee record",
            description = "Create a new late fee record for a user who returned a borrowed book after the due date. "
                    + "Accessible only by ADMIN."
    )
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
    @Operation(
            summary = "Mark late fee as paid",
            description = "Update a late fee record to mark it as paid. "
                    + "Accessible only by ADMIN."
    )
    public ResponseEntity<ApiResponse<LateFeeResponse>> paidLateFee(
            @PathVariable("id") int lateFeeId){
        ApiResponse<LateFeeResponse> apiResponse = ApiResponse.<LateFeeResponse>builder()
                .message("Paid late fee successfully")
                .data(lateFeeService.paidLateFee(lateFeeId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all late fees",
            description = "Retrieve a paginated list of all late fee records in the system. "
                    + "Accessible only by ADMIN."
    )
    public ResponseEntity<ApiResponse<PageResponse<LateFeeResponse>>> getLateFees(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<LateFeeResponse>> apiResponse = ApiResponse.<PageResponse<LateFeeResponse>>builder()
                .message("Get late fees successfully")
                .data(lateFeeService.getLateFees(page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userId == #jwt.claims['userId']")
    @Operation(
            summary = "Get late fees of a user",
            description = "Retrieve a paginated list of late fee records for a specific user. "
                    + "Accessible by ADMIN or the user themselves."
    )
    public ResponseEntity<ApiResponse<PageResponse<LateFeeResponse>>> getLateFeeOfUser(
            @PathVariable("id") int userId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<LateFeeResponse>> apiResponse = ApiResponse.<PageResponse<LateFeeResponse>>builder()
                .message("Get late fees of user successfully")
                .data(lateFeeService.getLateFeeOfUser(userId, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
