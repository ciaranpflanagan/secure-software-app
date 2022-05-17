package com.securesoftware.app;

import com.securesoftware.security.CustomLoginFailureHandler;
import com.securesoftware.security.CustomLoginSuccessHandler;
import com.securesoftware.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
	public MyUserDetailsService userDetailsService() {
		return new MyUserDetailsService();
	}
    
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
    private CustomLoginFailureHandler loginFailureHandler;
     
    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/hello", "/users/register", "/users/save", "/stats/all").permitAll()
				.antMatchers("/hse-admin/**", "/forum/reply/**", "/forum/save-reply").access("hasAuthority('HSE')")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home", true)
				.failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
				.permitAll()
				.and()
			.logout()
				.permitAll();

		http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false);
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }
}