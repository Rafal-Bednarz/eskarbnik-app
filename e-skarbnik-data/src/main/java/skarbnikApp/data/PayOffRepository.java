package skarbnikApp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.PayOff;

@Repository
public interface PayOffRepository extends CrudRepository<PayOff, Long> {

    @Transactional
    void deleteAllByGradeId(Long gradeId);
}
