package skarbnikApp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.PayOff;


public interface PayOffRepository extends CrudRepository<PayOff, Long> {

    @Transactional
    void deleteAllByGradeId(Long gradeId);
}
