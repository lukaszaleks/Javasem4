package pl.pjatk.bookshop.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static pl.pjatk.bookshop.config.security.AuthorityList.adminAuthority;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .userDetailsService(userDetailsService)
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .authorizeHttpRequests(customizer -> customizer

                .requestMatchers(HttpMethod.GET,"/book/search").permitAll()
                .requestMatchers(HttpMethod.GET,"/book").permitAll()
                .requestMatchers(HttpMethod.GET,"/book/{bookId}").permitAll()
                .requestMatchers(HttpMethod.POST,"/order").authenticated()
                .requestMatchers(HttpMethod.PUT,"/book/{bookId}").hasAnyAuthority(adminAuthority)
                .requestMatchers(HttpMethod.DELETE,"/book/{bookId}").hasAnyAuthority(adminAuthority)
                .requestMatchers(HttpMethod.POST,"/book/{bookId}").hasAnyAuthority(adminAuthority)
                .requestMatchers(HttpMethod.POST,"/order-report ").hasAnyAuthority(adminAuthority)
                .requestMatchers(HttpMethod.GET,"/order-report ").hasAnyAuthority(adminAuthority)
                .requestMatchers(HttpMethod.PUT,"/order/complete/{orderId}").hasAnyAuthority(adminAuthority)
                .requestMatchers(HttpMethod.GET,"/order").hasAnyAuthority(adminAuthority)
                )

                .formLogin(customizer -> customizer
                        .loginProcessingUrl("/login")
                        .permitAll())

                .logout(customizer -> customizer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true))
                .build();
    }
}