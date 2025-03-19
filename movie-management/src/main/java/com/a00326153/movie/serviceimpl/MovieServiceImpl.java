package com.a00326153.movie.serviceimpl;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.entity.Movie;
import com.a00326153.movie.mapper.MovieMapper;
import com.a00326153.movie.repository.MovieRepository;
import com.a00326153.movie.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> findAllMovies() {
        return movieRepository.findAll().stream().map(movie -> MovieMapper.mapToMovieDto(movie)).toList();
    }

    public MovieDto findMovieById(Long id) {
        return MovieMapper.mapToMovieDto(movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found with id: "+id)));
    }

    public MovieDto saveMovie(Movie movie) {
        validateMovie(movie);
        return MovieMapper.mapToMovieDto(movieRepository.save(movie));
    }

    public MovieDto updateMovie(Long id, MovieDto updatedMovieDto) {
        Movie existingMovie = movieRepository.findById(id).orElseThrow( () -> new RuntimeException("Movie not found with id: "+id));
        existingMovie.setTitle(updatedMovieDto.getTitle());
        existingMovie.setDirector(updatedMovieDto.getDirector());
        existingMovie.setReleaseDate(updatedMovieDto.getReleaseDate());
        return MovieMapper.mapToMovieDto(movieRepository.save(existingMovie));
    }


    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Movie with ID " + id + " not found, cannot delete");
        }
        movieRepository.deleteById(id);
    }

    public List<MovieDto> findMoviesByDirector(String director) {
        return movieRepository.findByDirector(director).stream().map(movie -> MovieMapper.mapToMovieDto(movie)).toList();
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
        if (movie.getReleaseDate() < 1800) {
            throw new IllegalArgumentException("Release date is unrealistic, must be after 1877");
        }
    }
}
