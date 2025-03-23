package com.a00326153.movie.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
        assertTrue(movies.length >= 3, "Expected at least four movies in the returned list");
    }

    @Test
    public void testGetMovieByIdFound() {
        ResponseEntity<MovieDto> response = restTemplate.getForEntity("/movies/{id}", MovieDto.class, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        MovieDto movie = response.getBody();
        assertNotNull(movie, "Returned movie should not be null");
        assertEquals("Inception", movie.getTitle(), "Expected title 'Inception'");
        assertEquals("Christopher Nolan", movie.getDirector(), "Expected director 'Christopher Nolan'");
        assertEquals(2010, movie.getReleaseDate(), "Expected release date 2010");
    }

    @Test
    public void testUpdateMovieIntegration() {

        MovieDto updateDto = new MovieDto();
        updateDto.setTitle("Interstellar Updated");
        updateDto.setDirector("Christopher Nolan");
        updateDto.setReleaseDate(2014);

        ResponseEntity<ResponseDto> putResponse = restTemplate.exchange("/movies/{id}", HttpMethod.PUT,
                new HttpEntity<>(updateDto), ResponseDto.class, 3);

        assertEquals(HttpStatus.CREATED, putResponse.getStatusCode(), "Expected HTTP status 201 CREATED for update");
        ResponseDto putBody = putResponse.getBody();
        assertNotNull(putBody, "Response body should not be null");
        assertEquals("201 ", putBody.getStatusCode(), "Expected status code '201 '");
        assertEquals("Movie updated Successfully", putBody.getStatusMsg(), "Expected update success message");

        ResponseEntity<MovieDto> getResponse = restTemplate.getForEntity("/movies/{id}", MovieDto.class, 3);
        MovieDto updatedMovie = getResponse.getBody();
        assertNotNull(updatedMovie, "Updated movie should not be null");
        assertEquals("Interstellar Updated", updatedMovie.getTitle(), "Expected the movie title to be updated");
    }

    @Test
    public void testDeleteMovieIntegration() {
        ResponseEntity<ResponseDto> deleteResponse = restTemplate.exchange("/movies/{id}", HttpMethod.DELETE,
                null, ResponseDto.class, 4);

        assertEquals(HttpStatus.CREATED, deleteResponse.getStatusCode(), "Expected HTTP status 201 CREATED for deletion");
        ResponseDto deleteBody = deleteResponse.getBody();
        assertNotNull(deleteBody, "Response body should not be null");
        assertEquals("201 ", deleteBody.getStatusCode(), "Expected status code '201 '");
        assertEquals("Movie has been deleted successfully", deleteBody.getStatusMsg(), "Expected deletion success message");

    }
}