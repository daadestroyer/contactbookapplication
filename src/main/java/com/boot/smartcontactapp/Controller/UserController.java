package com.boot.smartcontactapp.Controller;

import com.boot.smartcontactapp.Entities.Contact;
import com.boot.smartcontactapp.Entities.User;
import com.boot.smartcontactapp.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String user_email = principal.getName();
        //System.out.println("Username : "+user_email);

        // getting data from database using user email
        User user = this.userRepository.getUserByUserName(user_email);

        //System.out.println(user);
        model.addAttribute("user", user);

    }

    // dashboard home
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    // add contact handler
    @GetMapping("/add-contact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add-contact";
    }

    // save contact handler
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute("contact") Contact contact, Principal principal) {
        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);
        // contact me user store kia
        contact.setUser(user);
        // user me contact store kia
        user.getContacts().add(contact);
        this.userRepository.save(user);

        System.out.println("Contact Added");
        return "normal/add-contact";
    }

}
