package com.a00326153.movie.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.a00326153.movie.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class MovieControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllMoviesFromTestDB() {

        ResponseEntity<MovieDto[]> response = restTemplate.getForEntity("/movies", MovieDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        MovieDto[] movies = response.getBody();
        assertNotNull(movies, "Returned movie list should not be null");
        assertTrue(movies.length > 0, "Expected at least one movie in the returned list");
    }
}