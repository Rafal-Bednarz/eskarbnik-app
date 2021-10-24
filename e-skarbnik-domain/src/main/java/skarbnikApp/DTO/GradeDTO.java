package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class GradeDTO {
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final String budget;
    @Getter
    private final List<StudentDTO> students;
    @Getter
    private final List<PayOffDTO> payOffs;

}
