package skarbnikApp.data;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.User;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Modifying
    @Query(nativeQuery = true, value = "insert into usr_grades (user_id, grades_id) " +
            "values (:user_id, :grades_id)")
    void updateUserWhenAddGrade(@Param(value = "user_id") Long user_id,
                                @Param(value = "grades_id") Long grades_id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from usr_grades " +
            "where user_id = :user_id and grades_id = :grades_id")
    void updateUserWhenDeleteGrade(@Param(value = "user_id") Long user_id,
                                   @Param(value = "grades_id") Long grades_id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from usr_grades where user_id = :user_id")
    void deleteAllUserGrades(@Param(value = "user_id") Long id);

}
