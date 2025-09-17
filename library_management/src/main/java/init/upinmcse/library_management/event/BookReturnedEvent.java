package init.upinmcse.library_management.event;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookReturnedEvent {
    private int bookId;
}
