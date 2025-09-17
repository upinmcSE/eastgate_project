package init.upinmcse.library_management.mapper;

import init.upinmcse.library_management.dto.response.LateFeeResponse;
import init.upinmcse.library_management.model.LateFee;
import org.springframework.stereotype.Component;

@Component
public class LateFeeMapper {

    public LateFeeResponse toLateFeeResponse(LateFee lateFee) {
        return LateFeeResponse.builder()
                .lateFeeId(lateFee.getId())
                .bookId(lateFee.getBook().getId())
                .userId(lateFee.getUser().getId())
                .fee(lateFee.getFee())
                .description(lateFee.getDescription())
                .status(lateFee.getStatus().toString())
                .build();
    }
}
