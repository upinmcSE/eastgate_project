package init.upinmcse.library_management.schedule;

import init.upinmcse.library_management.constant.BorrowBookStatus;
import init.upinmcse.library_management.model.BorrowBook;
import init.upinmcse.library_management.repository.BorrowBookRepository;
import init.upinmcse.library_management.service.impl.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "BorrowBookScheduler")
public class BorrowBookScheduler {
    EmailService emailService;
    BorrowBookRepository borrowBookRepository;

    // Task 1: tìm bản ghi sắp hết hạn (còn 2 ngày)
//    @Scheduled(fixedRate = 90000, initialDelay = 10000)
//    @Scheduled(cron = "0 0 9 * * *")
    public void notifyRemindDueBooks(){
        LocalDateTime limitDate = LocalDateTime.now().plusDays(2);
        List<BorrowBook> remindList = this.borrowBookRepository
                .findBorrowedBooksDueInTwoDays(BorrowBookStatus.BORROWED, limitDate);

        if (remindList.isEmpty()){
            return;
        }

        for (BorrowBook bb : remindList) {
            String emailContent = "Lượt mượn sách của bạn sắp đến hạn phải trả:\n\n" +
                    String.format("%-10s được mượn bởi %-10s ngày phải trả %-25s\n",
                            bb.getBook().getTitle(),
                            bb.getUser().getFullName(),
                            bb.getDueDate());

            emailService.sendSimpleEmail(
                    bb.getUser().getEmail(),
                    "Nhắc nhở trả sách",
                    emailContent);
            log.info("Send mail successful");
        }
    }

    // Task 2: tìm bản ghi đã quá hạn mà chưa phạt
//    @Scheduled(fixedRate = 90000, initialDelay = 10000)
    public void checkOverdueBooks() {
        List<BorrowBook> overList = this.borrowBookRepository
                .findOverdueBorrowedWithoutLateFee(BorrowBookStatus.BORROWED);

        if(overList.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Danh sách sách quá hạn chưa được xử lý:\n\n");
        sb.append(String.format("%-10s %-10s %-25s\n", "BookID", "UserID", "DueDate"));
        sb.append("-----------------------------------------------------\n");

        for (BorrowBook bb : overList) {
            sb.append(String.format("%-10d %-10d %-25s\n",
                    bb.getBook().getId(),
                    bb.getUser().getId(),
                    bb.getDueDate().toString()));
        }

        String emailContent = sb.toString();
        emailService.sendSimpleEmail(
                "admin@gmail.com",
                "Danh sách mượn sách quá hạn",
                emailContent);
    }

}
