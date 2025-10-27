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

    @GetMapping("")
    public String listLanding() {
        return "index";
    }

    @GetMapping("/new")
    public String newBookForm() {
        return "new-book";
    }

    @PostMapping("/new")
    public String createBook() {
        AddressBook ab = new AddressBook();
        addressBooks.save(ab);
        return "redirect:/ui/addressbooks/" + ab.getId();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String viewBook(@PathVariable Long id, Model model) {
        AddressBook ab = addressBooks.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No book " + id));
        model.addAttribute("book", ab);
        model.addAttribute("buddies", ab.getBuddies());
        return "view-book";
    }

    @GetMapping("/{id}/buddies/new")
    public String newBuddyForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        model.addAttribute("form", new BuddyForm("", "", ""));
        return "add-buddy";
    }

    @PostMapping("/{id}/buddies")
    public String addBuddy(@PathVariable Long id, BuddyForm form) {
        AddressBook ab = addressBooks.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No book " + id));
        BuddyInfo bi = new BuddyInfo(form.address(), form.name(), form.phoneNumber());
        ab.addBuddy(bi);
        addressBooks.save(ab);
        return "redirect:/ui/addressbooks/" + id;
    }
}
