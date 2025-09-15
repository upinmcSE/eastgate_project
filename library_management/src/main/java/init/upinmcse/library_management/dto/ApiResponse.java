package init.upinmcse.library_management.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{
    @Builder.Default
    private int statusCode = 200;
    private String message;
    private T data;
}
