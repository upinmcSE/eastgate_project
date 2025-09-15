package init.upinmcse.library_management.exception;

import init.upinmcse.library_management.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFound(EntityNotFound e) {
        String message = e.getMessage();

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message(message)
                .statusCode(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
