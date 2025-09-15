package init.upinmcse.library_management.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreationRequest {
    private String title;
    private List<Integer> authorIds;
    private List<Integer> genreIds;
    private int publishYear;
    private int availableCount;
}
