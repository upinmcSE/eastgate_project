package init.upinmcse.library_management.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LateFeeResponse {
    private int lateFeeId;
    private int userId;
    private int bookId;
    private double fee;
    private String description;
    private String status;
}
