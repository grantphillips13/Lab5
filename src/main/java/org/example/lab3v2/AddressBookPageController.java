package org.example.lab3v2;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/addressbooks")
public class AddressBookPageController {

    private final AddressBookRepository addressBooks;

    public AddressBookPageController(AddressBookRepository addressBooks) {
        this.addressBooks = addressBooks;
    }

    // Landing page with simple links
    @GetMapping("")
    public String listLanding() {
        return "index";
    }

    // Show a simple page with a "Create" button
    @GetMapping("/new")
    public String newBookForm() {
        return "new-book";
    }

    // Create an address book, then redirect to its view page
    @PostMapping("/new")
    public String createBook() {
        AddressBook ab = new AddressBook();
        addressBooks.save(ab);
        return "redirect:/ui/addressbooks/" + ab.getId();
    }

    // Show an address book and its buddies
    // @Transactional so the lazy list can be rendered by Thymeleaf safely
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String viewBook(@PathVariable Long id, Model model) {
        AddressBook ab = addressBooks.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No book " + id));
        model.addAttribute("book", ab);
        model.addAttribute("buddies", ab.getBuddies());
        return "view-book";
    }

    // Show the "add buddy" form
    @GetMapping("/{id}/buddies/new")
    public String newBuddyForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        model.addAttribute("form", new BuddyForm("", "", ""));
        return "add-buddy";
    }

    // Handle "add buddy" form submit
    @PostMapping("/{id}/buddies")
    public String addBuddy(@PathVariable Long id, BuddyForm form) {
        AddressBook ab = addressBooks.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No book " + id));
        BuddyInfo bi = new BuddyInfo(form.address(), form.name(), form.phoneNumber());
        ab.addBuddy(bi);
        addressBooks.save(ab); // cascades buddy save
        return "redirect:/ui/addressbooks/" + id;
    }
}
