package skarbnikApp.data;

import org.springframework.data.repository.CrudRepository;
import skarbnikApp.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
