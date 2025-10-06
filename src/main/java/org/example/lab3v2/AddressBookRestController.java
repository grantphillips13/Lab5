package org.example.lab3v2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/addressbooks")
public class AddressBookRestController {

    private final AddressBookRepository addressBooks;
    private final BuddyInfoRepository buddies;

    public AddressBookRestController(AddressBookRepository addressBooks, BuddyInfoRepository buddies) {
        this.addressBooks = addressBooks;
        this.buddies = buddies;
    }


    // Create empty address book
    @PostMapping
    public ResponseEntity<AddressBook> createAddressBook() {
        AddressBook ab = addressBooks.save(new AddressBook());
        return ResponseEntity.created(URI.create("/api/addressbooks/" + ab.getId())).body(ab);
    }

    // Get buddies for an address book
    @GetMapping("/{id}/buddies")
    public ResponseEntity<List<BuddyInfo>> listBuddies(@PathVariable Long id) {
        return addressBooks.findById(id)
                .map(ab -> ResponseEntity.ok(ab.getBuddies()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a buddy (JSON body)
    @PostMapping("/{id}/buddies")
    public ResponseEntity<BuddyInfo> addBuddy(@PathVariable Long id, @RequestBody BuddyCreateDTO dto) {
        return addressBooks.findById(id).map(ab -> {
            BuddyInfo bi = new BuddyInfo(dto.address(), dto.name(), dto.phoneNumber());
            ab.addBuddy(bi);
            addressBooks.save(ab);
            buddies.save(bi);
            return ResponseEntity
                    .created(URI.create("/api/addressbooks/" + id + "/buddies/" + bi.getId()))
                    .body(bi);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Remove a buddy from a book (and delete it)
    @DeleteMapping("/{id}/buddies/{buddyId}")
    public ResponseEntity<Void> removeBuddy(@PathVariable Long id, @PathVariable Long buddyId) {
        var abOpt = addressBooks.findById(id);
        var bOpt  = buddies.findById(buddyId);
        if (abOpt.isEmpty() || bOpt.isEmpty()) return ResponseEntity.notFound().build();
        AddressBook ab = abOpt.get();
        BuddyInfo bi = bOpt.get();
        if (bi.getAddressBook() == null || !bi.getAddressBook().getId().equals(ab.getId()))
            return ResponseEntity.badRequest().build();
        ab.removeBuddy(bi);
        addressBooks.save(ab);
        return ResponseEntity.noContent().build();
    }

    public record BuddyCreateDTO(String address, String name, String phoneNumber) {}
}
