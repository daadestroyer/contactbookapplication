package com.boot.smartcontactapp.Controller;

import com.boot.smartcontactapp.Entities.User;
import com.boot.smartcontactapp.Helper.Message;
import com.boot.smartcontactapp.Repo.UserRepository;
import com.boot.smartcontactapp.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotPasswordController {

    Random random = new Random(1000);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // forgot password form
    @RequestMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password - Smart Contact Manager");
        return "forgot-password";
    }

    // handler for getting otp
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, Model model, HttpSession session) {

        model.addAttribute("title", "Verify OTP - Smart Contact Manager");


        // generating otp of 6 digit
        int otp = random.nextInt(999999);
        System.out.println("OTP :" + otp);

        // writting code for sending otp
        String subject = "OTP from Smart Contact Manager";
        String message = "" +
                "<div style='border:1px solid black;padding:20px'>" +
                "Hello user use this OTP as a <b>one time password</b> to change your password , <b>do not</b> share this OTP with others" +
                "<h4>" +
                "OTP is : " +
                "<b>" + otp +
                "</b>" +
                "</h4>" +
                "Regards,<br>" +
                "Team : Smart Contact Manager<br> " +
                "Designed and Developed by : Shubham Nigam Aka (daadestroyer)" +
                "</div>";
        String to = email;
        boolean res = this.emailService.sendEmail(subject, message, to);
        if (res) {
            session.setAttribute("serversideotp", otp);
            session.setAttribute("email", email);
            return "verify-otp";
        } else {
            session.setAttribute("message", new Message("Something went wrong !!!", "alert-danger"));
            return "forgot-password";
        }

    }

    // validate server side and client side otp
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp")int userSideOtp ,Model model, HttpSession session){
        int  serversideotp = (int) session.getAttribute("serversideotp");
        String email = (String)session.getAttribute("email");

        if(serversideotp == userSideOtp){
            // password change
           User user =  this.userRepository.getUserByUserName(email);
           if(user == null){
               // send error message
               session.setAttribute("message", new Message("User with this email not found !!!", "alert-danger"));
               return "forgot-password";
           }
           else{
               // change password form
               model.addAttribute("title", "Change Password - Smart Contact Manager");

               return "password-change";
           }

        }
        else{
            session.setAttribute("message", new Message("You have enter wrong OTP !!!", "alert-danger"));
            return "verify-otp";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword , HttpSession session){

        String email = (String)session.getAttribute("email");
        User user = this.userRepository.getUserByUserName(email);
        user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
        this.userRepository.save(user);
        session.setAttribute("message", new Message("Password change successfully !!!", "alert-success"));
        return "redirect:/signin";
    }

}
