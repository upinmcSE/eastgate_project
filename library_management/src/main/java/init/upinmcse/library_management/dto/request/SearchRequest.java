package init.upinmcse.library_management.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private String keyword;
}
