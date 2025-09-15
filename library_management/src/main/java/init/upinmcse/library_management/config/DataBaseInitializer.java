package init.upinmcse.library_management.config;

import init.upinmcse.library_management.constant.Gender;
import init.upinmcse.library_management.constant.UserStatus;
import init.upinmcse.library_management.model.*;
import init.upinmcse.library_management.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataBaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataBaseInitializer(
            UserRepository userRepository,
            RoleRepository roleRepository,
            GenreRepository genreRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countUser = this.userRepository.count();
        long countRole = this.roleRepository.count();
        long countGenre = this.genreRepository.count();
        long countAuthor = this.authorRepository.count();
        long countBook = this.bookRepository.count();

        if(countGenre == 0){
            genreRepository.saveAll(List.of(
                    Genre.builder().genreName("Science Fiction").build(),
                    Genre.builder().genreName("Fantasy").build(),
                    Genre.builder().genreName("Romance").build(),
                    Genre.builder().genreName("Mystery").build()
            ));

        }

        if(countAuthor == 0){
            authorRepository.saveAll(List.of(
                    Author.builder().fullName("Isaac Asimov").email("isaacasimov@gmail.com").age(50).build(),
                    Author.builder().fullName("J.K. Rowling").email("jkrowling@gmail.com").age(44).build(),
                    Author.builder().fullName("Agatha Christie").email("agethachristie@gmail.com").age(55).build()
            ));
        }

        if(countBook == 0){
            Genre fantasy = genreRepository.findByGenreName("Fantasy")
                    .orElseThrow(() -> new RuntimeException("Genre Not Found"));
            Author rowling = authorRepository.findByEmail("jkrowling@gmail.com")
                    .orElseThrow(() -> new RuntimeException("Author Not Found"));

            this.bookRepository.save(
                    Book.builder()
                            .genres(List.of(fantasy))
                            .authors(List.of(rowling))
                            .title("Harry Potter and the Philosopher's Stone")
                            .publishYear(2003)
                            .availableCount(10)
                            .borrowedCount(0)
                            .bookCode("")
                            .build());

        }

        if(countRole == 0){
            List<Role> roles = new ArrayList<>();
            Role roleAdmin = Role.builder()
                    .roleName("ADMIN")
                    .build();
            Role rolePatron = Role.builder()
                    .roleName("PATRON")
                    .build();
            roles.add(roleAdmin);
            roles.add(rolePatron);
            this.roleRepository.saveAll(roles);
        }

        if(countUser == 0){
            Role roleAdmin = this.roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role Not Found"));

            Set<Role> roles = new HashSet<>();
            roles.add(roleAdmin);

            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin"))
                    .age(22)
                    .gender(Gender.MALE)
                    .status(UserStatus.ENABLED)
                    .roles(roles)
                    .fullName("Admin")
                    .build();
            this.userRepository.save(admin);
        }

        if(countUser > 0 && countRole > 0 && countGenre > 0 && countAuthor > 0 ){
            System.out.println(">>> SKIP INIT DATABASE");
        }else{
            System.out.println(">>> END INIT DATABASE");
        }
    }

}
