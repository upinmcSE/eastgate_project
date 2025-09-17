package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.request.BorrowBookRequest;
import init.upinmcse.library_management.dto.request.SearchRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.dto.response.BorrowBookResponse;
import init.upinmcse.library_management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @PostMapping()
    @Operation(summary = "Add a new book")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookResponse>> addBook(@Valid @RequestBody BookCreationRequest request){
        ApiResponse<BookResponse> apiResponse = ApiResponse.<BookResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Add Book Success")
                .data(bookService.addBook(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Retrieve a book in the library")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable("id") int id){
        ApiResponse<BookResponse> apiResponse = ApiResponse.<BookResponse>builder()
                .message("Get Book Success")
                .data(bookService.getBookById(id))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("")
    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the library")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(){
        ApiResponse<List<BookResponse>> apiResponse = ApiResponse.<List<BookResponse>>builder()
                .message("Get All Books Success")
                .data(bookService.getAllBooks())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete book by id", description = "Soft delete a book in the library")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") int id){
        bookService.deleteBook(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete Book Success")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/genre/{name}")
    @Operation(summary = "Get books by genre", description = "Retrieve a list of all books in the library")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksByGenre(@PathVariable("name") String name){
        return null;
    }

    @GetMapping("/author/{name}")
    @Operation(summary = "Get books by author", description = "Retrieve a list of all books in the library")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksByAuthor(@PathVariable("name") String name){
        return null;
    }

    @GetMapping("/search")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksBySearch(@RequestBody SearchRequest request){
        return null;
    }

    @PostMapping("/borrow")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> borrowBook(@RequestBody BorrowBookRequest request){
        ApiResponse<BorrowBookResponse> apiResponse = ApiResponse.<BorrowBookResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Borrow Book Success")
                .data(bookService.borrowBook(request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/return")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<BorrowBookResponse>> returnBook(@RequestBody BorrowBookRequest request){
        ApiResponse<BorrowBookResponse> apiResponse = ApiResponse.<BorrowBookResponse>builder()
                .message("Return Book Success")
                .data(bookService.returnBook(request))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/borrow/list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBook(){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(bookService.getAllBorrowedBooks())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/borrow/list/{id}")
    @Operation(summary = "...", description = "...")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBookOfUser(@PathVariable("id") int userId){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .message("Get All borrow book successful")
                .data(bookService.getAllBorrowedBookOfUser(userId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/borrow/list/overdue")
    @Operation(summary = "xxx", description = "...")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BorrowBookResponse>>> borrowBookOverdue(){
        ApiResponse<List<BorrowBookResponse>> apiResponse = ApiResponse.<List<BorrowBookResponse>>builder()
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
