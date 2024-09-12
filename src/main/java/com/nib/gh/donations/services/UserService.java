package com.nib.gh.donations.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nib.gh.donations.model.Role;
import com.nib.gh.donations.model.Users;
import com.nib.gh.donations.model.UserDTO;
import com.nib.gh.donations.repository.RoleRepository;
import com.nib.gh.donations.repository.UserRepository;

@Service
public class UserService {

    Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${default.password}")
    private String defaultPassword;

    public List<Users> findAll() {
        List<Users> users = userRepository.findAll();

        for (Users user : users) {
            user.setPassword(null); // Set password to null or an empty string, or remove the field altogether
        }
        return users;
    }

    public String saveUser(UserDTO user) {
        String respMsg;
        Users userData = new Users();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {
            logger.info("USER DTO:: " + user);
            if (user.getEmail() != null && user.getPhoneNumber() != null && user.getUsername() != null) {

                Optional<Role> optionalRole = roleRepository
                        .findById(Long.parseLong(user.getRole()));

                if (optionalRole.isPresent()) {
                    Role role = optionalRole.get();

                    userData.setRoles(role);
                    userData.setEmail(user.getEmail());
                    userData.setFirstName(user.getFirstName());
                    userData.setLastName(user.getLastName());
                    userData.setPhoneNumber(user.getPhoneNumber());
                    userData.setUsername(user.getUsername());
                    userData.setPassword(passwordEncoder.encode(defaultPassword));

                    userRepository.save(userData);

                    respMsg = "00#Successful";
                } else {
                    respMsg = "06#Failed, Role not found";

                }
            } else {
                respMsg = "06#Failed, invalid request";

            }

        } catch (Exception e) {

            respMsg = "06#Failed, Not aded";
            System.out.println("Sorry, Restrict Op not done");
            e.printStackTrace();
        }

        return respMsg;
    }

    public boolean deleteById(long id) {
        try {

            Optional<Users> userOptional = userRepository.findById(id);
            // logger.info("USER DATA TO DELETE:: " + userOptional);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();
                user.setRoles(null);

                userRepository
                        .deleteById(user.getId());

                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public boolean enableById(long id) {
        try {

            Optional<Users> userOptional = userRepository.findById(id);
            logger.info("USER DATA TO ENABLE:: " + userOptional);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();

                userRepository
                        .updateDataById(user.getId(), true);

                return true;
            } else {
                logger.info("USER WAS NOT FOUND");
                return false;
            }

        } catch (Exception e) {
            logger.error("Exception occurred... " + e.getMessage());
            return false;
        }

    }

    public boolean disableById(long id) {
        try {

            Optional<Users> userOptional = userRepository.findById(id);
            logger.info("USER DATA TO ENABLE:: " + userOptional);
            if (userOptional.isPresent()) {
                Users user = userOptional.get();

                userRepository
                        .updateDataById(user.getId(), false);

                return true;
            } else {
                logger.info("USER WAS NOT FOUND");
                return false;
            }

        } catch (Exception e) {
            logger.error("Exception occurred... " + e.getMessage());
            return false;
        }

    }

    public String countUsers() {
        int totalNoOfUsers = 0;

        try {
            totalNoOfUsers = (int) userRepository.count();
            // logger.info("NO OF USERS:: " + totalNoOfUsers);
        } catch (Exception e) {
            logger.error("Exception has occurred, " + e.getMessage());
        }

        return String.valueOf(totalNoOfUsers);
    }

}
