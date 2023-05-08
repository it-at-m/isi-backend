package de.muenchen.isi.domain.service.transition;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
@Setter
public class CustomUserDetails {

    private String sub;

    private String surname;

    private String givenname;

    private String department;

    private List<GrantedAuthority> authorities;

    private String email;

    private String username;

    private String[] resource_access;
}
