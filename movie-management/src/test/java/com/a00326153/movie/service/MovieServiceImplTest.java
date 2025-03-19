package com.a00326153.movie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.entity.Movie;
import com.a00326153.movie.repository.MovieRepository;
import com.a00326153.movie.serviceimpl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MovieServiceImplTest {

    private MovieRepository movieRepository;
    private MovieServiceImpl movieService;

    @BeforeEach
    void setup() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test
    void testFindAllMovies_ReturnsAllMovies() {
        // Arrange
        Movie movie1 = new Movie(1L, "Inception", "Christopher Nolan", 2010);
        Movie movie2 = new Movie(2L, "The Dark Knight", "Christopher Nolan", 2008);
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        // Act
        List<MovieDto> results = movieService.findAllMovies();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size(), "Should return all movies");
        assertEquals("Inception", results.get(0).getTitle());
    }

    @Test
    void testFindMovieById_WhenMovieExists_ReturnsMovie() {
        // Arrange
        Movie movie = new Movie(1L, "Inception", "Christopher Nolan", 2010);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Act
        MovieDto result = movieService.findMovieById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void testFindMovieById_WhenMovieDoesNotExist_ThrowsException() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> movieService.findMovieById(1L));
        assertEquals("Movie not found with id: 1", exception.getMessage());
    }

    @Test
    void testSaveMovie_SavesAndReturnsMovie() {
        // Arrange
        Movie movie = new Movie(1L, "Inception", "Christopher Nolan", 2010);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Act
        MovieDto savedMovie = movieService.saveMovie(movie);

        // Assert
        assertNotNull(savedMovie);
        assertEquals("Inception", savedMovie.getTitle());
    }

    @Test
    void testDeleteMovie_WhenMovieExists_CompletesWithoutError() {
        // Arrange
        when(movieRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> movieService.deleteMovie(1L));
    }

    @Test
    void testDeleteMovie_WhenMovieDoesNotExist_ThrowsException() {
        // Arrange
        when(movieRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> movieService.deleteMovie(1L));
        assertEquals("Movie with ID 1 not found, cannot delete", exception.getMessage());
    }
}
