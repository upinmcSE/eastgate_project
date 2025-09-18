package init.upinmcse.library_management.event;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLateFeeEvent {
    private String email;
    private double fee;
    private String bookName;
    private String description;
}
