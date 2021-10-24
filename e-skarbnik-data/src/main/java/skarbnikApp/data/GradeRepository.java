package skarbnikApp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.Grade;


public interface GradeRepository extends CrudRepository<Grade, Long> {

    @Transactional
    void deleteAllByUserId(Long userId);
}
