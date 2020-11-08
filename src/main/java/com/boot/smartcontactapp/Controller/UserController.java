package com.boot.smartcontactapp.Controller;

import com.boot.smartcontactapp.Entities.User;
import com.boot.smartcontactapp.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // dashboard home
    @RequestMapping("/dashboard")
    public String dashboard(Model model , Principal principal) {
        String user_email = principal.getName();
        //System.out.println("Username : "+user_email);

        // getting data from database using user email
        User user = this.userRepository.getUserByUserName(user_email);

        //System.out.println(user);
        model.addAttribute("user",user);
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }



}
