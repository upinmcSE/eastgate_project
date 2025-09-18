package init.upinmcse.library_management.dto;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    int currentPage;
    int totalPages;
    int pageSize;
    long totalRecords;

    @Builder.Default
    private List<T> data = Collections.emptyList();
}
