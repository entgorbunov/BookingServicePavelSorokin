package sorokin.java.course.bookingservicepavelsorokin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sorokin.java.course.bookingservicepavelsorokin.security.CustomAccessDeniedHandler;
import sorokin.java.course.bookingservicepavelsorokin.security.CustomAuthenticationEntryPoint;
import sorokin.java.course.bookingservicepavelsorokin.security.JwtTokenFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
public class SecurityConfiguration {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfiguration(JwtTokenFilter jwtTokenFilter, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }



    @Bean(name = "customSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Правила для локаций
                                .requestMatchers(HttpMethod.GET, "/locations/**").hasAnyAuthority(ADMIN, USER)
                                .requestMatchers(HttpMethod.POST, "/locations").hasAuthority(ADMIN)
                                .requestMatchers(HttpMethod.DELETE, "/locations/{locationId}").hasAuthority(ADMIN)
                                .requestMatchers(HttpMethod.GET, "/locations/{locationId}").hasAnyAuthority(ADMIN, USER)
                                .requestMatchers(HttpMethod.PUT, "/locations/{locationId}").hasAuthority(ADMIN)

                                // Правила для событий
                                .requestMatchers(HttpMethod.POST, "/events").hasAuthority(USER)
                                .requestMatchers(HttpMethod.DELETE, "/events/{eventId}").hasAnyAuthority(ADMIN, USER)
                                .requestMatchers(HttpMethod.GET, "/events/{eventId}").hasAnyAuthority(ADMIN, USER)
                                .requestMatchers(HttpMethod.PUT, "/events/{eventId}").hasAnyAuthority(ADMIN, USER)
                                .requestMatchers(HttpMethod.POST, "/events/search").hasAnyAuthority(ADMIN, USER)
                                .requestMatchers(HttpMethod.GET, "/events/my").hasAuthority(USER)
                                .requestMatchers(HttpMethod.POST, "/events/registrations/{eventId}").hasAuthority(USER)
                                .requestMatchers(HttpMethod.DELETE, "/events/registrations/cancel/{eventId}").hasAuthority(USER)
                                .requestMatchers(HttpMethod.GET, "/events/registrations/my").hasAuthority(USER)

                                // Публичные эндпоинты
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/openapi.yaml").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()

                                // Админский доступ
                                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAuthority(ADMIN)

                                // Все остальные запросы требуют аутентификации
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}