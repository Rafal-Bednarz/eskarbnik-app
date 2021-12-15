package skarbnikApp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import skarbnikApp.DTO.GradeDTO;
import skarbnikApp.DTO.UserDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "usr")
public class User implements UserDetails {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(targetEntity = Grade.class)
    private List<Grade> grades = new ArrayList<Grade>();

    private final String username;

    private final String email;

    private final String password;

    private final String registrationToken;

    private boolean isActivated = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActivated;
    }

    public UserDTO toDTO() {
        List<GradeDTO> grades = new ArrayList<>();
        if (!this.grades.isEmpty()) {
            this.grades.forEach(grade -> {
                grades.add(grade.toDTO());
            });
        }
        return new UserDTO(username, email, grades);
    }

}
