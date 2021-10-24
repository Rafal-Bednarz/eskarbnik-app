package skarbnikApp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import skarbnikApp.User;
import skarbnikApp.data.UserRepository;

@Service
@RequiredArgsConstructor
public class UserRepoUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException("Użytkownik " + username + " nie został znaleziony");
    }
}
