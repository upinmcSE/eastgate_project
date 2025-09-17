package init.upinmcse.library_management.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LateFeeCreationRequest {
    private int userId;
    private int bookId;
    private double fee;
    private String description;
}
