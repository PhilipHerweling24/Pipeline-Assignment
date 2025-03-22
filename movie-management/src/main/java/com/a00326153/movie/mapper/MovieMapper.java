package com.a00326153.movie.mapper;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.entity.Movie;

public class MovieMapper {

    private MovieMapper() {
        //Private constructor to prevent instantiation
    }

    public static MovieDto mapToMovieDto(Movie movie){
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDirector(movie.getDirector());
        movieDto.setReleaseDate(movie.getReleaseDate());

        return movieDto;
    }
}
