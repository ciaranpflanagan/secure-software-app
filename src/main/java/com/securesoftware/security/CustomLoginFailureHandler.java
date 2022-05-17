package com.securesoftware.security;
 
import java.io.IOException;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.securesoftware.model.User;
import com.securesoftware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
 
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
     
    @Autowired
    private UserService userService;
     
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        User user = userService.findUserByEmail(email);
         
        if (user != null) {
            if (!user.getIsAccountLocked()) {
                if (user.getAttempts() < UserService.MAX_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (user.getIsAccountLocked()) {
                exception = new LockedException("Your account has been unlocked. Please try to login again.");
            }
             
        }
         
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
 
}