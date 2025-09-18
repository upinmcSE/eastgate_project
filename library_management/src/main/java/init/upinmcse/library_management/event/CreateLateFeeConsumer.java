package init.upinmcse.library_management.event;

import init.upinmcse.library_management.service.impl.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "CreateLateFeeConsumer")
public class CreateLateFeeConsumer {

    EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotifyLateFee(CreateLateFeeEvent event){
        String emailContent = "Hệ thống thông báo bạn đã bị đánh khoản phạt:\n\n" +
                String.format("%-10s vì đã mượn sách %-10s quá hạn bạn vui lòng liên hệ quản trị viên để xử lý\n",
                        event.getFee(),
                        event.getBookName());

        emailService.sendSimpleEmail(
                event.getEmail(),
                "Thông báo ăn quả phạt",
                emailContent);
        log.info("Send mail successful");
    }
}
