package com.securesoftware.service;

import com.securesoftware.model.User;

import java.util.Arrays;
import java.util.HashSet;

import com.securesoftware.model.Role;
import com.securesoftware.repository.RoleRepository;
import com.securesoftware.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.securesoftware.app.AES;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public static final int MAX_ATTEMPTS = 3;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // Encrypting other important information in the DB
        final String sk = "eochair";
        user.setPPSNumber(AES.encrypt(user.getPPSNumber(), sk));
        user.setPhoneNumber(AES.encrypt(user.getPhoneNumber(), sk));
        user.setDOB(AES.encrypt(user.getDOB(), sk));

        Role userRole = roleRepository.findByRole("USER");
        user.setRole(new HashSet<Role>(Arrays.asList(userRole)));

        return userRepository.save(user);
    }

    /**
     * Increases the number of failed attempts on an account
     * @param user
     */
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getAttempts() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(User user, String email) {
        user.setAccountLocked(false);
        userRepository.updateFailedAttempts(0, email);
    }
     
    public void lock(User user) {
        user.setAccountLocked(true);
        userRepository.save(user);
    }
}
