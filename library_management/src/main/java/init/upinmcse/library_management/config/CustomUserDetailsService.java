package init.upinmcse.library_management.config;

import init.upinmcse.library_management.exception.EntityNotFoundException;
import init.upinmcse.library_management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j(topic = "CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        init.upinmcse.library_management.model.User user = this.userService.getUserByEmail(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

        log.info("authorities: {}", authorities);

        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
