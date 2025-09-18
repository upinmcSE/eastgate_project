package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.response.BorrowBookResponse;
import init.upinmcse.library_management.service.BorrowBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/borrow")
@RequiredArgsConstructor
@Tag(name= "Borrow Book", description = "Borrow Book management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowBookController {
    BorrowBookService borrowBookService;

    @PostMapping()
    @Operation(
            summary = "Borrow a book",
            description = "Allows a user to borrow a book from the library. "
                    + "Creates a new borrow record and decreases the available quantity of the book."
    )
    @PreAuthorize("#request.userId == #principal.claims['userId']")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> borrowBook(
            @RequestBody BorrowBookRequest request,
            @AuthenticationPrincipal Jwt principal
    ){
        ApiResponse<BorrowBookResponse> apiResponse = ApiResponse.<BorrowBookResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Borrow Book Success")
                .data(borrowBookService.borrowBook(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/return")
    @Operation(
            summary = "Return a borrowed book",
            description = "Allows a user to return a previously borrowed book. "
                    + "Updates the borrow record and increases the available quantity of the book."
    )
    @PreAuthorize("#request.userId == #principal.claims['userId']")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> returnBook(
            @RequestBody BorrowBookRequest request,
            @AuthenticationPrincipal Jwt principal
    ){
        ApiResponse<BorrowBookResponse> apiResponse = ApiResponse.<BorrowBookResponse>builder()
                .message("Return Book Success")
                .data(borrowBookService.returnBook(request))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all borrowed books",
            description = "Retrieve a paginated list of all borrow records in the system. "
                    + "Accessible only by ADMIN."
    )
    public ResponseEntity<ApiResponse<PageResponse<BorrowBookResponse>>> borrowBook(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BorrowBookResponse>> apiResponse = ApiResponse.<PageResponse<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(borrowBookService.getAllBorrowedBooks(page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list/book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get borrow records of a book",
            description = "Retrieve a paginated list of all borrow records for the specified book. "
                    + "Accessible only by ADMIN."
    )
    public ResponseEntity<ApiResponse<PageResponse<BorrowBookResponse>>> borrowBookOfBook(
            @PathVariable("id") int bookId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BorrowBookResponse>> apiResponse = ApiResponse.<PageResponse<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(borrowBookService.getAllBorrowedBookOfBook(bookId, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userId == #jwt.claims['userId'] ")
    @Operation(
            summary = "Get borrow records of a user",
            description = "Retrieve a paginated list of all books borrowed by a specific user. "
                    + "Accessible by ADMIN or the user themselves."
    )
    public ResponseEntity<ApiResponse<PageResponse<BorrowBookResponse>>> borrowBookOfUser(
            @PathVariable("id") int userId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BorrowBookResponse>> apiResponse = ApiResponse.<PageResponse<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(borrowBookService.getAllBorrowedBookOfUser(userId, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list/overdue")
    @Operation(
            summary = "Get overdue borrow records",
            description = "Retrieve a paginated list of all overdue borrow records (books not returned on time). "
                    + "Accessible only by ADMIN."
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<BorrowBookResponse>>> borrowBookOverdue(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BorrowBookResponse>> apiResponse = ApiResponse.<PageResponse<BorrowBookResponse>>builder()
                .message("Get All borrow book over due successful")
                .data(borrowBookService.getAllBorrowedBookOverDue(page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
