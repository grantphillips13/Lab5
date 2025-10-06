package org.example.lab3v2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressBookRestController.class)
class AddressBookRestControllerWebTest {

    @Autowired MockMvc mvc;

    // mock repos so the controller can be tested in isolation
    @MockBean AddressBookRepository addressBookRepository;
    @MockBean BuddyInfoRepository buddies;

    @Test
    void createAddressBook_returns201() throws Exception {
        AddressBook saved = new AddressBook();
        // if you have getId(), set it via reflection if needed; not required for 201 status check
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(saved);

        mvc.perform(post("/api/addressbooks"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/addressbooks/")));
    }
}
