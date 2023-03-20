package de.muenchen.isi.domain.service.factorys;

import de.muenchen.isi.domain.service.annotations.MockCustomUserInfo;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class SecurityContextFactory implements WithSecurityContextFactory<MockCustomUserInfo>  {
    @Override
    public SecurityContext createSecurityContext(MockCustomUserInfo annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

    }
}
