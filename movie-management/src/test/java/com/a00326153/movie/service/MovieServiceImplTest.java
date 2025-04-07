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

    //Ensures that a fresh mock instance is created before each test
    @BeforeEach
    void setup() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieServiceImpl(movieRepository);
    }

    @Test //test to find all movies
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

    @Test //Test to find a movie by an ID which exists
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

    @Test //Test to find a movie by an ID which doesn't exist
    void testFindMovieById_WhenMovieDoesNotExist_ThrowsException() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> movieService.findMovieById(1L));
        assertEquals("Movie not found with id: 1", exception.getMessage());
    }

    @Test //Test to save a movie and return said movie
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

    @Test //Tests to update a movie which does exist
    void testUpdateMovie_WhenMovieExists_ReturnsUpdatedMovie() {
        // Arrange
        Long movieId = 1L;
        Movie existingMovie = new Movie(movieId, "Old Title", "Old Director", 2000);
        MovieDto updatedDto = new MovieDto(null, "New Title", "New Director", 2023);
        Movie savedMovie = new Movie(movieId, "New Title", "New Director", 2023);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        // Act
        MovieDto result = movieService.updateMovie(movieId, updatedDto);

        // Assert
        assertNotNull(result, "Returned MovieDto should not be null");
        assertEquals("New Title", result.getTitle(), "Expected updated title");
        assertEquals("New Director", result.getDirector(), "Expected updated director");
        assertEquals(2023, result.getReleaseDate(), "Expected updated release year");
    }

    @Test //Test to update a movie when the movie doesn't exist
    void testUpdateMovie_WhenMovieNotFound_ThrowsException() {
        // Arrange
        Long movieId = 99L;
        MovieDto updateDto = new MovieDto(null, "Doesn't Matter", "No One", 2023);
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> movieService.updateMovie(movieId, updateDto));
        assertEquals("Movie not found with id: 99", exception.getMessage(), "Expected 'not found' exception message");
    }


    @Test //Test to delete a movie which does exist
    void testDeleteMovie_WhenMovieExists_CompletesWithoutError() {
        // Arrange
        when(movieRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> movieService.deleteMovie(1L));
    }

    @Test //Test to delete a movie which doesn't exist
    void testDeleteMovie_WhenMovieDoesNotExist_ThrowsException() {
        // Arrange
        when(movieRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> movieService.deleteMovie(1L));
        assertEquals("Movie with ID 1 not found, cannot delete", exception.getMessage());
    }

    @Test //Test to save a movie when the title is left blank
    void testSaveMovie_WhenTitleIsBlank_ThrowsException() {
        Movie movie = new Movie();
        movie.setTitle(" ");
        movie.setDirector("Nolan");
        movie.setReleaseDate(2020);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> movieService.saveMovie(movie));
        assertEquals("Movie title cannot be blank", ex.getMessage());
    }

    @Test //test to a save a movie when the director is left blank
    void testSaveMovie_WhenDirectorIsBlank_ThrowsException() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDirector(" ");
        movie.setReleaseDate(2020);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> movieService.saveMovie(movie));
        assertEquals("Movie director cannot be blank", ex.getMessage());
    }

    @Test //test to save a movie when teh release date is less than 1800
    void testSaveMovie_WhenReleaseIsLessThan1800_ThrowsException() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDirector("Nolan");
        movie.setReleaseDate(1799);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> movieService.saveMovie(movie));
        assertEquals("Release date is unrealistic, must be after 1800", ex.getMessage());
    }
}
