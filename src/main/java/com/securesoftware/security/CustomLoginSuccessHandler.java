package com.securesoftware.security;

import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.securesoftware.model.User;
import com.securesoftware.service.MyUserDetailsService;
import com.securesoftware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
 
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
 
    @Autowired
    private UserService userService;
     
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user =  (User) authentication.getPrincipal();
        
        if (user.getAttempts() > 0) {
            userService.resetFailedAttempts(user, user.getEmail());
        }
         
        super.onAuthenticationSuccess(request, response, authentication);
    }
     
}