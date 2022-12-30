/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * This filter logs the username for requests.
 */
@Component
@Order(1)
@Slf4j
public class RequestResponseLoggingFilter implements Filter {

    @Getter
    private static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";

    private static final String TOKEN_USER_NAME = "username";

    private static final String REQUEST_LOGGING_MODE_ALL = "all";

    private static final String REQUEST_LOGGING_MODE_CHANGING = "changing";

    private static final List<String> CHANGING_METHODS = Arrays.asList("POST", "PUT", "PATCH", "DELETE");

    /**
     * The property or a zero length string if no property is available.
     */
    @Value("${spring.security.logging.requests}")
    private String requestLoggingMode;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) {
        log.debug("Initializing filter: {}", this);
    }

    /**
     * The method logs the username extracted out of the {@link SecurityContext},
     * the kind of HTTP-Request, the targeted URI and the response http status code.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (this.checkForLogging(httpRequest)) {
            log.info("User {} executed {} on URI {} with http status {}",
                    getUsername(),
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    httpResponse.getStatus()
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        log.debug("Destructing filter: {}", this);
    }

    /**
     * The method checks if logging the username should be done.
     *
     * @param httpServletRequest The request to check for logging.
     * @return True if logging should be done otherwise false.
     */
    private boolean checkForLogging(final HttpServletRequest httpServletRequest) {
        return this.requestLoggingMode.equals(REQUEST_LOGGING_MODE_ALL)
                || (this.requestLoggingMode.equals(REQUEST_LOGGING_MODE_CHANGING)
                && CHANGING_METHODS.contains(httpServletRequest.getMethod()));
    }

    /**
     * The method extracts the username out of the {@link Jwt}.
     *
     * @return The username or a placeholder if there is no {@link Jwt} available.
     */
    public static String getUsername() {
        String username = null;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final var principal = authentication.getPrincipal();
            if (Objects.equals(Jwt.class, principal.getClass())) {
                final var jwt = (Jwt) principal;
                username = jwt.getClaimAsString(TOKEN_USER_NAME);
            }
        }
        return StringUtils.isNotBlank(username) ? username : NAME_UNAUTHENTICATED_USER;
    }

}
