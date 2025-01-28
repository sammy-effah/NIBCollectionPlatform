package com.nib.gh.donations.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String email;
    private String lastName;
    private String username;
    private String phoneNumber;
    private boolean enabled;
    private boolean forcePasswordReset;
    private String password;
    private LocalDateTime createDate;
    private String role;

}
