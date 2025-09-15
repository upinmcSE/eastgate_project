package init.upinmcse.library_management.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private int bookId;
    private String title;
    private String bookCode;
    private List<String> authors;
    private List<String> genres;
    private int publishYear;
    private int availableCount;
    private int borrowedCount;
    private String status;
}
