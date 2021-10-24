package skarbnikApp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import skarbnikApp.Payment;


@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    @Transactional
    void deleteAllByStudentId(Long studentId);
}
