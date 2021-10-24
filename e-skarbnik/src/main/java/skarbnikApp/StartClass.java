package skarbnikApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import skarbnikApp.data.GradeRepository;
import skarbnikApp.data.UserRepository;


@Configuration
public class StartClass {


    private UserRepository repo;
    private GradeRepository gradeRepo;

    public StartClass(UserRepository repo, GradeRepository gradeRepo) {

        this.repo = repo;
        this.gradeRepo = gradeRepo;
    }
    /*
    @Bean
    //@Profile("prod")
    public CommandLineRunner commandLineRunner(UserRepository repo, GradeRepository gradeRepo, PasswordEncoder passwordEncoder) {
        return args ->  {
                Grade.Grade_Id[] grade_ids = Grade.Grade_Id.values();

                for(Grade.Grade_Id grade_id : grade_ids) {
                    Grade grade = new Grade();
                    gradeRepo.save(grade);
                    repo.save(new User(grade, "skarbnik_" + grade.getGrade_id().toString(),
                            passwordEncoder.encode(grade.getGrade_id().toString())));
                }
        };
    }

     */
}
