package init.upinmcse.library_management.controller;

import init.upinmcse.library_management.dto.ApiResponse;
import init.upinmcse.library_management.dto.PageResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BookResponse>> apiResponse = ApiResponse.<PageResponse<BookResponse>>builder()
                .message("Get All Books Success")
                .data(bookService.getAllBooks(page, size))
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
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getBooksByGenre(
            @PathVariable("name") String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BookResponse>> apiResponse = ApiResponse.<PageResponse<BookResponse>>builder()
                .message("Get All Books by genre successful")
                .data(bookService.searchBooksByGenre(name, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/author/{name}")
    @Operation(summary = "Get books by author", description = "Retrieve a list of all books in the library")
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getBooksByAuthor(
            @PathVariable("name") String name,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "order", defaultValue = "ASC") String order
    ){
        ApiResponse<PageResponse<BookResponse>> apiResponse = ApiResponse.<PageResponse<BookResponse>>builder()
                .message("Get All Books by author successful")
                .data(bookService.searchBooksByAuthor(name, page, size))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search books",
            description = "Search for books based on given criteria such as title, author. "
                    + "Returns a list of matching books."
    )
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksBySearch(@RequestBody SearchRequest request){
        return null;
    }
}
