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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

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

	/*@Autowired
    private CustomLoginFailureHandler loginFailureHandler;
     
    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;*/
	@Bean
	public SimpleUrlAuthenticationFailureHandler loginFailureHandler(){
		return new CustomLoginFailureHandler();
	}

	@Bean
	public SimpleUrlAuthenticationSuccessHandler loginSuccessHandler(){
		return new CustomLoginSuccessHandler();
	}

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
				.failureHandler(loginFailureHandler())
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