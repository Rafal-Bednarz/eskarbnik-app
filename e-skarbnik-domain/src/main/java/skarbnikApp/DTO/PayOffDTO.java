package skarbnikApp.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PayOffDTO {
    @Getter
    private final String createdAt;
    @Getter
    private final String name;
    @Getter
    private final String value;

}
