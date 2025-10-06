package org.example.lab3v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab3V2Application {

    private static final Logger log = LoggerFactory.getLogger(Lab3V2Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Lab3V2Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(AddressBookRepository addressBooks,
                                  BuddyInfoRepository buddies) {
        return (args) -> {
            AddressBook book = new AddressBook();

            BuddyInfo a = new BuddyInfo("123 Maple Street", "Alice", "555-1234");
            BuddyInfo b = new BuddyInfo("456 Oak Avenue", "Bob", "555-5678");

            book.addBuddy(a);
            book.addBuddy(b);

            addressBooks.save(book);

            log.info("Buddies found with findAll():");
            log.info("-------------------------------");
            buddies.findAll().forEach(buddy -> log.info(buddy.toString()));
            log.info("");

            log.info("Address books found with findAll():");
            log.info("-----------------------------------");
            addressBooks.findAll().forEach(ab ->
                    log.info("Book #" + ab.getId() + " has " + ab.getBuddies().size() + " buddies")
            );
            log.info("");

            log.info("Buddy found with findByName('Alice'):");
            log.info("--------------------------------------");
            buddies.findByName("Alice").forEach(alice -> log.info(alice.toString()));
            log.info("");
        };
    }
}
