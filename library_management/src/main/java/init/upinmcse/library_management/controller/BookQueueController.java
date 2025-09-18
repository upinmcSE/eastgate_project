package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BorrowQueueRequest;
import init.upinmcse.library_management.dto.request.CancelQueueRequest;
import init.upinmcse.library_management.dto.response.BorrowQueueResponse;
import init.upinmcse.library_management.service.BookQueueService;
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
@RequestMapping("/api/v1/book-queue")
@RequiredArgsConstructor
@Tag(name = "Book Queue", description = "Book Queue management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookQueueController {

    BookQueueService bookQueueService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or #request.userId == #jwt.claims['userId'] ")
    @Operation(
            summary = "Join wait list",
            description = "Add the current user to the waiting list for a specific book. "
                    + "Accessible by ADMIN or the user themselves."
    )
    public ResponseEntity<ApiResponse<BorrowQueueResponse>> joinQueue(
            @Valid @RequestBody BorrowQueueRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        ApiResponse<BorrowQueueResponse> apiResponse = ApiResponse.<BorrowQueueResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Join wait list successful")
                .data(bookQueueService.registerBookQueue(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userId == #jwt.claims['userId'] ")
    @Operation(
            summary = "Get user's book queue",
            description = "Retrieve the paginated list of books that the specified user is waiting for. "
                    + "Accessible by ADMIN or the user themselves."
    )
    public ResponseEntity<ApiResponse<PageResponse<BorrowQueueResponse>>> getQueueOfUser(
            @PathVariable("id") int userId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ) {
        ApiResponse<PageResponse<BorrowQueueResponse>> apiResponse = ApiResponse.<PageResponse<BorrowQueueResponse>>builder()
                .message("Get list book-queue of you successful")
                .data(bookQueueService.getBookQueueOfUser(userId, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get queue of a book",
            description = "Retrieve the paginated list of users currently waiting for the specified book. "
                    + "Accessible only by ADMIN."
    )
    public ResponseEntity<ApiResponse<PageResponse<BorrowQueueResponse>>> getQueueOfBook(
            @PathVariable("id") int bookId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ) {
        ApiResponse<PageResponse<BorrowQueueResponse>> apiResponse = ApiResponse.<PageResponse<BorrowQueueResponse>>builder()
                .message("Get list book-queue of you successful")
                .data(bookQueueService.getBookQueueOfBook(bookId, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN') or #request.userId == #jwt.claims['userId']")
    @Operation(
            summary = "Cancel wait list",
            description = "Cancel the waiting list request for a specific book. "
                    + "Accessible by ADMIN or the user who created the request."
    )
    public ResponseEntity<ApiResponse<Void>> cancelQueue(
            @RequestBody CancelQueueRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        bookQueueService.cancelBookQueue(request.getBookQueueId());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Cancel book queue successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
