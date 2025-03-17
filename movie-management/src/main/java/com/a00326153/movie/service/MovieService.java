package com.a00326153.movie.service;

import com.a00326153.movie.entity.Movie;
import com.a00326153.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (!movie.isPresent()) {
            throw new IllegalArgumentException("Movie with ID " + id + " not found");
        }
        return movie;
    }

    public Movie saveMovie(Movie movie) {
        validateMovie(movie);
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Movie with ID " + id + " not found, cannot delete");
        }
        movieRepository.deleteById(id);
    }

    private void validateMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Cannot save null movie");
        }
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be blank");
        }
        if (movie.getDirector() == null || movie.getDirector().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie director cannot be blank");
        }
        if (movie.getReleaseDate() < 1878) { // Assuming movies didn't exist before 1878
            throw new IllegalArgumentException("Release date is unrealistic, must be after 1877");
        }
    }
}
