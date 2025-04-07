package com.a00326153.movie.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.entity.Movie;
import com.a00326153.movie.serviceimpl.MovieServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MovieController.class)
class MovieControllerTest {

    //loads only the controller and related web components
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        //@WebMvcTest takes care of setting up the application
    }

    @Test //Test for retrieving all movies
    void getAllMovies() throws Exception {
        //Arrange
        List<MovieDto> movieList = Arrays.asList(
                new MovieDto(1L, "Inception", "Christopher Nolan", 2010),
                new MovieDto(2L, "The Dark Knight", "Christopher Nolan", 2008)
        );
        given(movieService.findAllMovies()).willReturn(movieList);

        //Act & Assert
        mockMvc.perform(get("/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Inception")))
                .andExpect(jsonPath("$[1].title", is("The Dark Knight")));
    }

    @Test //Test for retrieving a movie by ID when the movie exists
    void getMovieByIdFound() throws Exception {
        //Arrange
        MovieDto movieDto = new MovieDto(1L, "Inception", "Christopher Nolan", 2010);
        given(movieService.findMovieById(1L)).willReturn(movieDto);

        //Act & Assert
        mockMvc.perform(get("/movies/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Inception")));
    }

    @Test //Test for creating a movie successfully
    void createMovieOK() throws Exception {
        //Arrange
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDirector("Christopher Nolan");
        movie.setReleaseDate(2010);
        //The service returns a MovieDto after saving
        MovieDto savedMovieDto = new MovieDto(1L, "Inception", "Christopher Nolan", 2010);
        given(movieService.saveMovie(any(Movie.class))).willReturn(savedMovieDto);

        //Act & Assert
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode", is("201 ")))
                .andExpect(jsonPath("$.statusMsg", is("Movie Created Successfully")));
    }

    @Test //Test for updating a movie successfully
    void updateMovieOK() throws Exception {
        //Arrange
        MovieDto updateDto = new MovieDto(null, "Inception Updated", "Christopher Nolan", 2010);
        //Service returns updated MovieDto, but controller returns fixed ResponseDto
        given(movieService.updateMovie(anyLong(), any(MovieDto.class)))
                .willReturn(new MovieDto(1L, "Inception Updated", "Christopher Nolan", 2010));

        //Act & Assert
        mockMvc.perform(put("/movies/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode", is("201 ")))
                .andExpect(jsonPath("$.statusMsg", is("Movie updated Successfully")));
    }

    @Test //Test for deleting a movie successfully
    void deleteMovieOK() throws Exception {
        //Arrange
        //For deletion, we assume the service completes without exceptions.

        //Act & Assert
        mockMvc.perform(delete("/movies/{id}", 1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode", is("201 ")))
                .andExpect(jsonPath("$.statusMsg", is("Movie has been deleted successfully")));
    }

    @Test //Test for retrieving movies by director
    void findMoviesByDirector() throws Exception {
        //Arrange
        List<MovieDto> movieList = Arrays.asList(
                new MovieDto(1L, "Inception", "Christopher Nolan", 2010)
        );
        given(movieService.findMoviesByDirector(anyString())).willReturn(movieList);

        //Act & Assert
        mockMvc.perform(get("/movies/director")
                        .param("director", "Christopher Nolan")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Inception")));
    }

    @Test //Test for retrieving a movie by ID when the movie is not found
    void getMovieById_NotFound_ReturnsNotFoundError() throws Exception {
        //Arrange
        given(movieService.findMovieById(999L)).willThrow(new RuntimeException("Movie not found with id: 999"));

        //Act & Assert
        mockMvc.perform(get("/movies/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test //Test for creating a movie with invalid data (missing title)
    void createMovie_InvalidData_ReturnsBadRequest() throws Exception {
        //Arrange
        Movie invalidMovie = new Movie(); // Missing title, director, releaseDate
        given(movieService.saveMovie(any(Movie.class)))
                .willThrow(new IllegalArgumentException("Movie title cannot be blank"));

        //Act & Assert
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMovie)))
                .andExpect(status().isBadRequest());
    }

    @Test //Test for updating a movie when it doesn't exist
    void updateMovie_NotFound_ReturnsNotFoundError() throws Exception {
        //Arrange
        MovieDto updateDto = new MovieDto(null, "New Title", "New Director", 2023);
        given(movieService.updateMovie(anyLong(), any(MovieDto.class)))
                .willThrow(new RuntimeException("Movie not found with id: 5"));

        //Act & Assert
        mockMvc.perform(put("/movies/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test //Test for deleting a movie when it doesn't exist
    void deleteMovie_NotFound_ReturnsNotFoundError() throws Exception {
        //Arrange
        doThrow(new IllegalArgumentException("Movie with ID 123 not found, cannot delete"))
                .when(movieService).deleteMovie(123L);

        //Act & Assert
        mockMvc.perform(delete("/movies/{id}", 123))
                .andExpect(status().isBadRequest());
    }

    @Test //Test for retrieving movies by director when no results are found
    void findMoviesByDirector_NoResults_ReturnsEmptyList() throws Exception {
        //Arrange
        given(movieService.findMoviesByDirector(anyString())).willReturn(List.of());

        //Act & Assert
        mockMvc.perform(get("/movies/director")
                        .param("director", "Unknown Director")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}