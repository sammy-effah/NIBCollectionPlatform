package com.nib.gh.donations.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourcesPages {

   @GetMapping("/collection")
   public String collection(Model model, Authentication authentication) {
      String username = authentication.getName();

      model.addAttribute("username", username);

      return "collection"; // View name
   }

   @GetMapping("/admin")
   public String admin(Model model, Authentication authentication) {
      String username = authentication.getName();
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
      String username = authentication.getName();
      model.addAttribute("username", username);
      return "details"; // View name
   }

   @GetMapping("/donatee")
   public String donateePage(Model model, Authentication authentication) {
      String username = authentication.getName();
      model.addAttribute("username", username);
      return "donatee"; // View name
   }

   @GetMapping("/esb/fundstransfer")
   public String fundTransfer(Model model, Authentication authentication) {
      String username = authentication.getName();
      model.addAttribute("username", username);
       // Add any data to the model if needed
       return "esb/fundstransfer"; // Assuming the template is in src/main/resources/templates/esb/fundstransfer.html
   }


}
