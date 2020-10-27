package com.boot.smartcontactapp.Controller;


import com.boot.smartcontactapp.Entities.UserTable;
import com.boot.smartcontactapp.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String registerUser(@ModelAttribute("user") UserTable user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model) {
        if (!agreement) {
            return "signup";
        }
        user.setRole("role_user");
        user.setEnabled(true);

        System.out.println("Agreement " + agreement);
        System.out.println("User " + user);

        UserTable ut = this.userRepo.save(user);
        System.out.println("User Saved : "+ut);

        model.addAttribute("user",user);
        return "signup";
    }
}
