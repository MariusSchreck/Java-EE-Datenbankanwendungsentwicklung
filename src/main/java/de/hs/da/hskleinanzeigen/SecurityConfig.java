package de.hs.da.hskleinanzeigen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/actuator/metrics").hasRole("ADMIN")
                .antMatchers("/actuator/info").permitAll()
                .antMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication)
            throws Exception
    {
        authentication.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities("ROLE_ADMIN").and()
                .withUser("user")
                .password(passwordEncoder().encode("user"))
                .authorities("ROLE_USER");


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

