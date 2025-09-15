package init.upinmcse.library_management.model;

import init.upinmcse.library_management.constant.LateFeeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "late_fee")
@EntityListeners(AuditingEntityListener.class)
public class LateFee extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "late_fee_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private double fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LateFeeStatus status;
}
