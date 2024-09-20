package sorokin.java.course.bookingservicepavelsorokin.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sorokin.java.course.bookingservicepavelsorokin.users.service.User;
import sorokin.java.course.bookingservicepavelsorokin.users.service.UserService;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtManager jwtManager;
    private final UserService userService;

    public JwtTokenFilter(JwtManager jwtManager, UserService userService) {
        this.jwtManager = jwtManager;
        this.userService = userService;
    }



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().contains("/swagger-ui") ||
            request.getRequestURI().contains("/v3/api-docs") ||
            request.getRequestURI().contains("/openapi.yaml")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        if (!jwtManager.isTokenValid(token)) {
            log.info("JWT token not valid");
            filterChain.doFilter(request, response);
            return;
        }

        String login = jwtManager.getLoginFromToken(token);
        String role = jwtManager.getRoleFromToken(token);

        User userByLogin = userService.getUserByLogin(login);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userByLogin,
                null,
                List.of(new SimpleGrantedAuthority(role)));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

}
