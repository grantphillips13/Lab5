package org.example.lab3v2;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class BuddyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String address;
    private String name;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(
            name = "addressbook_id",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @JsonBackReference
    private AddressBook addressBook;

    public BuddyInfo() {}

    public BuddyInfo(String address, String name, String phoneNumber) {
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() { return id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public AddressBook getAddressBook() { return addressBook; }

    public void setAddressBook(AddressBook addressBook) { this.addressBook = addressBook; }


    public String toString() {
        return address + " " + name + " " + phoneNumber;
    }
}
