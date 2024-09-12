package com.nib.gh.donations.controllers;
import com.nib.gh.donations.model.LoginDto;
import com.nib.gh.donations.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @ModelAttribute("credentials")
    public LoginDto credentialsAttribute() {
        return new LoginDto();
    }

    @GetMapping
    public String getLoginView(Model model, HttpServletRequest httpServletreq, HttpSession session,
            @CookieValue(required = false, value = "aidCookie") String aidCookie) {
        // logger.info("I GOT HERE LOGIN GET!");
        model.addAttribute("linkName", "Login");
        return "login";

    }

    // @PostMapping
    // public String logIn(@Valid @ModelAttribute("credentials") LoginDto credentials, BindingResult bindingResult,
    //         HttpSession session, HttpServletRequest httpServletReq, HttpServletResponse responsehttp, Model model,
    //         HttpServletRequest httpServletreq) throws InvalidKeyException,
    //         BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
    //             logger.info("I GOT HERE LOGIN POST!");
    //     UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(credentials.getUsername(),
    //             credentials.getPassword());
    //     String error = "User account is Temporarily Disabled! " + System.lineSeparator()
    //             + "Reset Password To Enable Account";
        

    //     if (httpServletreq.getContextPath().contains("multi-acct")) {

    //         logger.info("**urlat login" + String.valueOf(httpServletReq.getRequestURI()));

    //     }

    //     User user = userService.findByUsername(credentials.getUsername());

    //     logger.info("UPAT:: " + upat);

    //     try {
    //         Authentication authentication = authManager.authenticate(upat);

    //         if (!user.isEnabled()) {
    //             System.out.println("enabled:: first");
    //             bindingResult.reject("no-authority", error);
    //             return "login";
    //         }

    //         if (user.isForcePasswordReset()) {
    //             model.addAttribute("credentials", credentials);
    //             return "changepw";

    //         }

    //         System.out.println("authentication.getName():: " + authentication.getName());

    //         String roleNam = user.getRoles();
    //         System.out.println("roleNam:: " + roleNam);
    //         if (roleNam != null) {

    //             SecurityContextHolder.getContext().setAuthentication(authentication);

    //             session.setAttribute("role", roleNam);
    //             session.setAttribute("id", user.getId());
    //             session.setAttribute("name", user.getFirstName() + " " + user.getLastName());

    //             session.setAttribute("email", user.getEmail());
    //             session.setAttribute("username", user.getUsername());

    //             session.setAttribute("createDate", user.getCreateDate());

    //             session.setAttribute("phoneNumber", user.getPhoneNumber());

    //             Cookie endDateCookie = new Cookie("username", user.getUsername());
    //             endDateCookie.setPath("/");
    //             endDateCookie.setMaxAge(60 * 60 * 24 * 365 * 10);
    //             responsehttp.addCookie(endDateCookie);

    //             return "redirect:";

    //         } else {
    //             bindingResult.reject("no-authority", "You do not have claimed authority");
    //             return "login";
    //         }

    //     } catch (AuthenticationException ex) {

    //         if (Objects.nonNull(user)) {

    //             if (!user.isEnabled()) {
    //                 logger.info("!user.isEnabled():: " + !user.isEnabled());

    //                 bindingResult.reject("no-authority", error);
    //                 return "login";
    //             } else {

    //                 bindingResult.reject("bad-credentials", "Invalid e-mail or password");

    //                 return "login";

    //             }

    //         } else {
    //             bindingResult.reject("bad-credentials", "Invalid e-mail or password");

    //             return "login";

    //         }

    //     }

    // }

    // @PostMapping
    // public String logIn(@Valid @ModelAttribute("credentials") LoginDto credentials, BindingResult bindingResult,
    //         HttpSession session, HttpServletRequest httpServletReq, HttpServletResponse responsehttp,
    //         Model model) {

    //     if (bindingResult.hasErrors()) {
    //         return "login";
    //     }

    //     // Authentication logic here
    //     // Example: Check username and password

    //     if ("admin".equals(credentials.getUsername()) && "password".equals(credentials.getPassword())) {
    //         session.setAttribute("username", credentials.getUsername());
    //         return "redirect:/collection";
    //     } else {
    //         model.addAttribute("error", "Invalid username or password");
    //         return "login";
    //     }
    // }

}