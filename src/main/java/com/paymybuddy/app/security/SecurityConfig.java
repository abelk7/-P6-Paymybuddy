package com.paymybuddy.app.security;

import com.paymybuddy.app.exception.RoleUserNotFoundException;
import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);
    private final UtilisateurRepository utilisateurRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] staticResources = {
                "/css/**",
                "/images/**",
                "/fonts/**",
                "/scripts/**",
        };
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/login", "/register", "/register/user").permitAll()
                        .antMatchers("/profile", "/profile/modifier", "/profile/modifier/password", "/profile/modifier/user").hasAnyRole("USER", "ADMIN")
                        .antMatchers(staticResources).permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .authenticationProvider(authenticationProvider())
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        http.csrf().disable();

        return http.build();
    }

    /**
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

                if (utilisateur == null) {
                    LOG.error("L'utilisateur avec l'email : {} n'a pas été trouvé", email);
                    throw new UsernameNotFoundException("L'identifiant est incorrect");
                }
                LOG.info("L'utilisateur avec l'adresse email {} à été trouvé", email);

                if (utilisateur.getRoles() == null || utilisateur.getRoles().isEmpty()) {
                    throw new RoleUserNotFoundException("Aucun role n'est assigné à l'utilisateur");
                }

                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                for (Role role : utilisateur.getRoles()) {
                    authorities.add(new SimpleGrantedAuthority(role.getLibelle()));
                }

                return new User(utilisateur.getEmail(), utilisateur.getPassword(), authorities);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
