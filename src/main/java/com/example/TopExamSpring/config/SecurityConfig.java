package com.example.TopExamSpring.config;

import com.example.TopExamSpring.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final PersonDetailsService personDetailsService;

    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers( "/auth/ban", "/person/person/product/**","/auth/login", "/auth/registration", "/error").permitAll()
                .antMatchers("/auto_product", "/auto_product/images/{**}", "/auto_product/{**}",
                        "/product", "/product/images/{**}", "/product/{**}",
                        "/home", "/home/images/{**}", "/home/{**}",
                        "/real_estate", "/real_estate/images/{**}", "/real_estate/{**}").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/home",true)
                .failureHandler((req, res, exp) -> {
                    if (exp instanceof DisabledException) {
                        res.sendRedirect("/auth/ban");
                    } else {
                        res.sendRedirect("/auth/login?error");
                    }
                })
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/home");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
