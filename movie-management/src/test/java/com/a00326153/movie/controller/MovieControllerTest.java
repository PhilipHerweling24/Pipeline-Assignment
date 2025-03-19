package com.a00326153.movie.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.dto.ResponseDto;
import com.a00326153.movie.serviceimpl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieServiceImpl movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    void testGetAllMovies_ReturnsAllMovies() throws Exception {
        // Arrange
        when(movieService.findAllMovies()).thenReturn(Arrays.asList(
                new MovieDto(1L, "Inception", "Christopher Nolan", 2010),
                new MovieDto(2L, "The Dark Knight", "Christopher Nolan", 2008)
        ));

        // Act & Assert
        mockMvc.perform(get("/movies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[1].title").value("The Dark Knight"));
    }

    @Test
    void testGetMovieById_WhenMovieExists_ReturnsMovie() throws Exception {
        // Arrange
        when(movieService.findMovieById(1L)).thenReturn(new MovieDto(1L, "Inception", "Christopher Nolan", 2010));

        // Act & Assert
        mockMvc.perform(get("/movies/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }
    
}