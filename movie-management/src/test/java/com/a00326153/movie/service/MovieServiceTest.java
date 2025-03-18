package com.a00326153.movie.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.a00326153.movie.entity.Movie;
import com.a00326153.movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class MovieServiceTest {

    private MovieRepository movieRepository = mock(MovieRepository.class);
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(movieRepository);
    }

    @Test
    void findAllMoviesReturnsCorrectMovies() {
        List<Movie> expectedMovies = Arrays.asList(new Movie(1, "Inception", "Christopher Nolan", 2010),
                new Movie(2, "Interstellar", "Christopher Nolan", 2014));
        when(movieRepository.findAll()).thenReturn(expectedMovies);
        List<Movie> movies = movieService.findAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
    }

    @Test
    void findMovieByIdFound() {
        Movie movie = new Movie(1, "Inception", "Christopher Nolan", 2010);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Optional<Movie> foundMovie = movieService.findMovieById(1L);
        assertTrue(foundMovie.isPresent());
        assertEquals("Inception", foundMovie.get().getTitle());
    }

    @Test
    void findMovieByIdNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.findMovieById(1L);
        });
        assertEquals("Movie with ID 1 not found", exception.getMessage());
    }

    @Test
    void saveMovieValidatesAndSaves() {
        Movie movie = new Movie(0, "Inception", "Christopher Nolan", 2010);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie savedMovie = movieService.saveMovie(movie);
        assertNotNull(savedMovie);
        assertEquals("Inception", savedMovie.getTitle());
    }

    @Test
    void deleteMovieWhenExists() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> movieService.deleteMovie(1L));
    }

    //test
    @Test
    void deleteMovieWhenNotExists() {
        when(movieRepository.existsById(1L)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.deleteMovie(1L);
        });
        assertEquals("Movie with ID 1 not found, cannot delete", exception.getMessage());
    }
}
