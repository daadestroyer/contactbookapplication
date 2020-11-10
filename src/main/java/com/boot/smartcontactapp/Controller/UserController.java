package com.boot.smartcontactapp.Controller;

import com.boot.smartcontactapp.Entities.Contact;
import com.boot.smartcontactapp.Entities.User;
import com.boot.smartcontactapp.Helper.Message;
import com.boot.smartcontactapp.Repo.ContactRepository;
import com.boot.smartcontactapp.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

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
        model.addAttribute("title", "Dashboard - Smart Contact Manager");
        return "normal/user_dashboard";
    }

    // add contact handler
    @GetMapping("/add-contact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add Contacts - Smart Contact Manager");
        model.addAttribute("contact", new Contact());
        return "normal/add-contact";
    }

    // save contact handler
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute("contact") Contact contact,
                                 @RequestParam("profileImage") MultipartFile multipartFile,
                                 Principal principal,
                                 HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);


            // processing and uploading file
            if (multipartFile.isEmpty()) {
                // if file is empty set default photo
                contact.setImage("user.png");

            } else {
                // if not then save to particular folder
                contact.setImage(multipartFile.getOriginalFilename());
                File file = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(file.getAbsolutePath() + File.separator +   multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            // contact me user store kia
            contact.setUser(user);
            // user me contact store kia
            user.getContacts().add(contact);
            // saving data
            this.userRepository.save(user);

            // success message
            session.setAttribute("message", new Message("Contact Added Successfully !!!", "alert-success"));

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong Try Again !!!" + e.getMessage(), "alert-danger"));

        }
        return "normal/add-contact";

    }

    // view contacts
    // per page 5 contacts
    // current page = 0
    @GetMapping("/view-contacts/{page}")
    public String viewContact(@PathVariable("page") int page, Model model, Principal principal) {
        model.addAttribute("title", "Contacts - Smart Contact Manager");

        String userName = principal.getName();

        User user = this.userRepository.getUserByUserName(userName);
        // this of() take two things 1st is current page and 2nd is contact per page
        PageRequest pageRequest = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageRequest);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentpage", page); // getting current page
        model.addAttribute("totalpage", contacts.getTotalPages()); // getting total page
        return "normal/view-contacts";
    }

    // profile handler
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") int id, Model model) {
        model.addAttribute("title", "Profile - Smart Contact Manager");
        Optional<Contact> contact_optional = this.contactRepository.findById(id);
        Contact contact = contact_optional.get();
        model.addAttribute("contact",contact);
        return "normal/profile";
    }

    // settings handler
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "Settings - Smart Contact Manager");
        return "normal/settings";
    }

}
