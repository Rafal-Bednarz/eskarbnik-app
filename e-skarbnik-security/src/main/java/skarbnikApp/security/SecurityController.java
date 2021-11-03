package skarbnikApp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityController extends WebSecurityConfigurerAdapter {

    @Value("${allowed.origins}")
    private String origins;

    private final UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity security) throws Exception {

        security.requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure();

    security.authorizeRequests()
            .antMatchers( "/login", "/registration/**/**", "/contact",
                    "/swagger-ui/").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").invalidateHttpSession(true)
            .and().cors()
            .and().csrf()
            .ignoringAntMatchers("/login", "/logout", "/registration/**/**", "/contact",
                    "/swagger-ui/")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .maxAge(3600)
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
