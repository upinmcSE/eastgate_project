package init.upinmcse.library_management.model;

import init.upinmcse.library_management.constant.BookStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
public class Book extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(name = "book_code", nullable = false)
    private String bookCode;

    @Column(name = "publish_year", nullable = false)
    private int publishYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @Column(name = "available_count", nullable = false)
    private int availableCount;

    @Column(name = "borrowed_count", nullable = false)
    private int borrowedCount;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                       CascadeType.REFRESH})
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                       CascadeType.REFRESH})
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();
}
