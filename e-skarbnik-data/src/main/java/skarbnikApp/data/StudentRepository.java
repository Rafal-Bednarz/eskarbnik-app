package skarbnikApp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.Student;

public interface StudentRepository extends CrudRepository<Student, Long>{

    @Transactional
    void deleteAllByGradeId(Long gradeId);
}
