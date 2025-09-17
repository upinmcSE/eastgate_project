package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.request.BorrowQueueRequest;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-queue")
@RequiredArgsConstructor
@Tag(name = "Book Queue", description = "Book Queue management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookQueueController {

    BookQueueService bookQueueService;

    @PostMapping()
    @Operation(summary = "Join wait list", description = "...")
    public ResponseEntity<ApiResponse<BorrowQueueResponse>> joinQueue(@Valid @RequestBody BorrowQueueRequest request) {
        ApiResponse<BorrowQueueResponse> apiResponse = ApiResponse.<BorrowQueueResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Join wait list successful")
                .data(bookQueueService.registerBookQueue(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping()
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BorrowQueueResponse>>> getQueue() {
        ApiResponse<List<BorrowQueueResponse>> apiResponse = ApiResponse.<List<BorrowQueueResponse>>builder()
                .message("Get list book-queue of you successful")
                .data(bookQueueService.getBookQueues())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<Void>> cancelQueue(@PathVariable("id") int id) {
        bookQueueService.cancelBookQueue(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Cancel book queue successful")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
