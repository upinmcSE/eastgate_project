package init.upinmcse.library_management.exception;

import init.upinmcse.library_management.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFound(EntityNotFoundException e) {
        String message = e.getMessage();

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message(message)
                .statusCode(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidToken(InvalidTokenException e) {
        String message = e.getMessage();

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message(message)
                .statusCode(401)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        String message = e.getMessage();

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message(message)
                .statusCode(403)
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(UsernamePasswordException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernamePasswordException(UsernamePasswordException e) {
        String message = e.getMessage();

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message(message)
                .statusCode(401)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }
}
