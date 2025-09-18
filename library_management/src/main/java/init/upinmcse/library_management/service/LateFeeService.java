package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.PageResponse;
import init.upinmcse.library_management.dto.request.LateFeeCreationRequest;
import init.upinmcse.library_management.dto.response.LateFeeResponse;

public interface LateFeeService {
    LateFeeResponse createLateFee(LateFeeCreationRequest request);
    LateFeeResponse paidLateFee(int lateFeeId);
    PageResponse<LateFeeResponse> getLateFees(int page, int size);
    PageResponse<LateFeeResponse> getLateFeeOfUser(int userId, int page, int size);
}
