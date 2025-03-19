package com.a00326153.movie.service;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.entity.Movie;

import java.util.List;

public interface MovieService {

    List<MovieDto> findAllMovies();

    MovieDto findMovieById(Long id);

    MovieDto saveMovie(Movie movie);

    MovieDto updateMovie(Long id, MovieDto updatedMovieDto);

    void deleteMovie(Long id);

    List<MovieDto> findMoviesByDirector(String director);
}
