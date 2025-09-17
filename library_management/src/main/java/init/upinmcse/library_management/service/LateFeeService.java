package init.upinmcse.library_management.service;

import init.upinmcse.library_management.dto.request.LateFeeCreationRequest;
import init.upinmcse.library_management.dto.response.LateFeeResponse;

import java.util.List;

public interface LateFeeService {
    LateFeeResponse createLateFee(LateFeeCreationRequest request);
    LateFeeResponse paidLateFee(int lateFeeId);
    List<LateFeeResponse> getLateFees();
    List<LateFeeResponse> getLateFeeOfUser(int userId);
}
