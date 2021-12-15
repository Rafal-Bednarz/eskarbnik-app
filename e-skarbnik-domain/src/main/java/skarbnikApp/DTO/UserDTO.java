package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class UserDTO {

    @Getter
    private final String username;
    @Getter
    private final String email;
    @Getter
    private final List<GradeDTO> grades;

}
