package com.a00326153.movie.controller;

import com.a00326153.movie.dto.MovieDto;
import com.a00326153.movie.dto.ResponseDto;
import com.a00326153.movie.entity.Movie;
import com.a00326153.movie.serviceimpl.MovieServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieServiceImpl movieService;

    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = movieService.findAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        MovieDto movieDto = movieService.findMovieById(id);
        return ResponseEntity.ok(movieDto);

    }

    @PostMapping
    public ResponseEntity<ResponseDto> createMovie(@RequestBody Movie movie) {
        movieService.saveMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("201 ", "Movie Created Successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movie) {
        movieService.updateMovie(id, movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto("201 ", "Movie updated Successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.CREATED).body( new ResponseDto("201 ", "Movie has been deleted successfully"));
    }

    @GetMapping("/director")
    public ResponseEntity<List<MovieDto>> findMoviesByDirector(@RequestParam String director) {
        return ResponseEntity.ok(movieService.findMoviesByDirector(director));
    }


}