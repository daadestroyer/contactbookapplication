package com.boot.smartcontactapp.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","SignUp - Smart Contact Manager");
        return "signup";
    }
}
