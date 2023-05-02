package de.muenchen.isi.domain.service.transition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = SecurityContextFactory.class)
public @interface MockCustomUser {
    String username() default "rob";

    String surname() default "Winch";

    String givenname() default "Rob";

    String department() default "IT";

    String email() default "rob@example.com";

    String sub() default "12345678-1234-1234-1234-1234567890ab";

    String[] roles() default { "lhm-isi-admin" };
}
