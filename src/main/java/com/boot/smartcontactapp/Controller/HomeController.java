package com.boot.smartcontactapp.Controller;


import com.boot.smartcontactapp.Entities.UserTable;
import com.boot.smartcontactapp.Helper.Message;
import com.boot.smartcontactapp.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserRepo userRepo;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "SignUp - Smart Contact Manager");
        model.addAttribute("user", new UserTable());
        return "signup";
    }

    // handler for registering user
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserTable user , BindingResult result, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model  ,HttpSession session  ) {
        try {
            if (!agreement) {
                throw new Exception("You have not agreed the terms and condition !");
            }
            if(result.hasErrors()){
                System.out.println("ERROR!!!"+result.toString());
                model.addAttribute("user",user);
                return "signup";
            }
            user.setRole("role_user");
            user.setEnabled(true);
            user.setImageURL("default.png");

            UserTable ut = this.userRepo.save(user);
            model.addAttribute("user", new UserTable()); // Putting new user for another registration after successful registration
            session.setAttribute("message", new Message("Successfully Registered !!!", "alert-success"));

            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong ! " + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }
}
