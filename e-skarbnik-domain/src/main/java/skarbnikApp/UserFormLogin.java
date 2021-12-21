package skarbnikApp;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class UserFormLogin implements Serializable {

    private static final long serialVersionUID = 523498L;

    private final String username;

    private final String password;
}
