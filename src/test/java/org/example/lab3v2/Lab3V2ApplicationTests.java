package org.example.lab3v2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "app.seed=false" // if you gated your CommandLineRunner
)
class Lab3V2ApplicationTests {

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void fullFlow_createBook_addBuddy_thenViewPageShowsBuddy() {
        // 1) Create an address book
        ResponseEntity<String> createResp =
                rest.postForEntity(url("/api/addressbooks"), null, String.class);

        assertThat(createResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI location = createResp.getHeaders().getLocation();
        assertThat(location).isNotNull();

        Long bookId = extractIdFromLocation(location);
        assertThat(bookId).isNotNull();

        // 2) Add a buddy to that book (send JSON)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> dto = Map.of(
                "address", "125 Maple",
                "name", "Alice",
                "phoneNumber", "555-1234"
        );

        ResponseEntity<String> addResp = rest.postForEntity(
                url("/api/addressbooks/" + bookId + "/buddies"),
                new HttpEntity<>(dto, headers),
                String.class
        );

        assertThat(addResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 3) Load the Thymeleaf page and check it contains the buddy
        ResponseEntity<String> page =
                rest.getForEntity(url("/addressbooks/" + bookId + "/view"), String.class);

        assertThat(page.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(page.getBody()).contains("Alice"); // minimal proof page renders buddy
    }

    private Long extractIdFromLocation(URI loc) {
        String p = loc.getPath();              // e.g. /api/addressbooks/2
        String last = p.substring(p.lastIndexOf('/') + 1);
        try { return Long.valueOf(last); } catch (Exception e) { return null; }
    }
}