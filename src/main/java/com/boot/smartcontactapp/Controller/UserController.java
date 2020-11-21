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
                Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
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
    public String profile(@PathVariable("id") int id, Model model, Principal principal) {

        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);


        model.addAttribute("title", "Profile - Smart Contact Manager");
        Optional<Contact> contact_optional = this.contactRepository.findById(id);
        Contact contact = contact_optional.get();

        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
        }
        return "normal/profile";
    }

    // settings handler
    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "Settings - Smart Contact Manager");
        return "normal/settings";
    }

    // deleting contact
    @GetMapping("/delete-contact/{cid}")
    public String deleteContact(@PathVariable("cid") int cid, Principal principal, HttpSession session) {
        try {
            Optional<Contact> contactOptional = this.contactRepository.findById(cid);
            Contact contact = contactOptional.get();


            // check
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
            if (user.getId() == contact.getUser().getId()) {
                // unlink that contact from User table
                contact.setUser(null);

                // remove photo

                this.contactRepository.delete(contact);
                session.setAttribute("message", new Message("Contact Deleted Successfully !!!", "alert-success"));
            } else {
                session.setAttribute("message", new Message("No user found !!!", "alert-danger"));
            }
        } catch (Exception e) {
            session.setAttribute("message", new Message("Something went wrong !!!" + e.getMessage(), "alert-danger"));
            e.printStackTrace();
        }
        return "redirect:/user/view-contacts/0";
    }

    // update contact view
    @PostMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid") int cid, Model model) {

        model.addAttribute("title", "Update Contact - Smart Contact Manager");
        Contact contact = this.contactRepository.findById(cid).get();
        model.addAttribute("contact", contact);
        return "normal/update_form";


    }

    // update contact handler
    @PostMapping("/update-contact")
    public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile multipartFile, Model model, HttpSession session, Principal principal) {

        try {
            // old contact detail
            Contact oldcontact = this.contactRepository.findById(contact.getcId()).get();
            if (!multipartFile.isEmpty()) {
                // rewrite the file
                // delete old photo
                File deletefile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deletefile, oldcontact.getImage());
                file1.delete();

                // update new photo
                File file = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(file.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(multipartFile.getOriginalFilename());
            } else {
                contact.setImage(oldcontact.getImage());
            }
            User user = this.userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);

            session.setAttribute("message", new Message("Contact Updated Successfully !!!", "alert-success"));
        } catch (Exception e) {

            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong Try Again !!!" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/user/profile/" + contact.getcId();
    }

    @GetMapping("/user-profile")
    public String userProfile() {
        return "normal/user-profile";
    }
}
