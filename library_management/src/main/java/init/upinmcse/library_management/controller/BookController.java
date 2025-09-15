package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.request.BookCreationRequest;
import init.upinmcse.library_management.dto.response.BookResponse;
import init.upinmcse.library_management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<BookResponse>> addBook(@RequestBody BookCreationRequest request){
        ApiResponse<BookResponse> apiResponse = ApiResponse.<BookResponse>builder()
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
        List<BookResponse> books = bookService.getAllBooks();
        ApiResponse<List<BookResponse>> apiResponse = ApiResponse.<List<BookResponse>>builder()
                .message("Get All Books Success")
                .data(books)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id", description = "Soft delete a book in the library")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") int id){
        bookService.deleteBook(id);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Delete Book Success")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
