package org.example.lab3v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class AddressBookViewController {

    private final AddressBookRepository addressBooks;

    public AddressBookViewController(AddressBookRepository addressBooks, BuddyInfoRepository buddies) {
        this.addressBooks = addressBooks;
    }


    @GetMapping("/addressbooks/{id}/view")
    @Transactional(readOnly = true)
    public String view(@PathVariable Long id, Model model) {
        AddressBook book = addressBooks.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "AddressBook %d not found".formatted(id)));

        model.addAttribute("book", book);
        model.addAttribute("buddies", book.getBuddies());
        return "buddies";
    }
}
