// AddressBook.java
package org.example.lab3v2;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "addressBook", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER )
    @JsonManagedReference
    private List<BuddyInfo> buddys = new ArrayList<>();

    public AddressBook() {}

    public Long getId() { return id; }
    public List<BuddyInfo> getBuddies() { return buddys; }

    public void addBuddy(BuddyInfo buddy) {
        buddys.add(buddy);
        buddy.setAddressBook(this);
    }

    public void removeBuddy(BuddyInfo buddy) {
        buddys.remove(buddy);
        buddy.setAddressBook(null);
    }

    public int size() {
        return buddys.size();
    }

}
