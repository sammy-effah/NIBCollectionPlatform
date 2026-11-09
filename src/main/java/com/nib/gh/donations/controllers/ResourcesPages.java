package com.nib.gh.donations.controllers;

import java.util.Optional;

import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nib.gh.donations.model.Users;
import com.nib.gh.donations.repository.UserRepository;

@Controller
public class ResourcesPages {

   @Autowired
   private UserRepository userRepository;

   @GetMapping("/collection")
   public String collection(Model model, Authentication authentication) {
      String username = authentication.getName().split("@")[0];

      Optional<Users> user = userRepository.findByUsername(username.split("@")[0]);
      if (user.isPresent()) {
         String role = user.get().getRoles().getName();
         System.out.println("User role: " + role);
         model.addAttribute("role", role);

      }
      model.addAttribute("username", username);

      return "collection"; // View name
   }

   @GetMapping("/admin")
   public String admin(Model model, Authentication authentication) {
      String username = authentication.getName().split("@")[0];
      model.addAttribute("username", username);
      return "admin"; // View name
   }

   @GetMapping("/access-denied")
   public String accessDenied() {
      return "access-denied"; // View name
   }

   @GetMapping("/recoverpw")
   public String recoverPassword() {
      return "recoverpw"; // View name
   }

   @GetMapping("/resetpw")
   public String resetPw() {
      return "resetpw"; // View name
   }

   @GetMapping("/details")
   public String donationDetailsPage(Model model, Authentication authentication) {
      String username = authentication.getName().split("@")[0];
      model.addAttribute("username", username);
      return "details"; // View name
   }

   @GetMapping("/donatee")
   public String donateePage(Model model, Authentication authentication) {
      String username = authentication.getName().split("@")[0];
      model.addAttribute("username", username);
      return "donatee"; // View name
   }

   @GetMapping("/institutions")
   public String Institution(Model model, Authentication authentication) {
      String username = authentication.getName().split("@")[0];
      model.addAttribute("username", username);
       // Add any data to the model if needed
       return "institutions"; // Assuming the template is in src/main/resources/templates/esb/fundstransfer.html
   }

   // institutionpay
@GetMapping("/institutionpay")
   public String InstitutionsPay(Model model, Authentication authentication) {
      String username = authentication.getName().split("@")[0];

      Optional<Users> user = userRepository.findByUsername(username.split("@")[0]);
      if (user.isPresent()) {
         String role = user.get().getRoles().getName();
         System.out.println("User role: " + role);
         model.addAttribute("role", role);

      }
      model.addAttribute("username", username);
       // Add any data to the model if needed
       return "institutionpay"; // Assuming the template is in src/main/resources/templates/esb/fundstransfer.html
   }

}
