package skarbnikApp.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 7654390L;

    private final String token;
}
