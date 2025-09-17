package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrow")
@RequiredArgsConstructor
@Tag(name= "Borrow Book", description = "Borrow Book management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BorrowBookController {
    BorrowBookService borrowBookService;

    @PostMapping()
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> borrowBook(@RequestBody BorrowBookRequest request){
        ApiResponse<BorrowBookResponse> apiResponse = ApiResponse.<BorrowBookResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Borrow Book Success")
                .data(borrowBookService.borrowBook(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/return")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> returnBook(@RequestBody BorrowBookRequest request){
        ApiResponse<BorrowBookResponse> apiResponse = ApiResponse.<BorrowBookResponse>builder()
                .message("Return Book Success")
                .data(borrowBookService.returnBook(request))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/borrow/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBook(){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(borrowBookService.getAllBorrowedBooks())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list/book/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBookOfBook(@PathVariable("id") int bookId){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(borrowBookService.getAllBorrowedBookOfBook(bookId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userId == #jwt.claims['userId'] ")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBookOfUser(
            @PathVariable("id") int userId,
            @AuthenticationPrincipal Jwt jwt
    ){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(borrowBookService.getAllBorrowedBookOfUser(userId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list/overdue")
    @Operation(summary = "xxx", description = "...")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBookOverdue(){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .message("Get All borrow book over due successful")
                .data(borrowBookService.getAllBorrowedBookOverDue())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
