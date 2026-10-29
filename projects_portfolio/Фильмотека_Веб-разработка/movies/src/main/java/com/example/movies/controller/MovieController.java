package com.example.movies.controller;

import com.example.movies.model.Genre;
import com.example.movies.model.Movie;
import com.example.movies.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return movieService.getAllGenres();
    }

    @PostMapping("/movies")
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @PutMapping("/movies/{id}/watched")
    public ResponseEntity<Movie> markAsWatched(@PathVariable Long id) {
        Movie updatedMovie = movieService.markAsWatched(id);
        if (updatedMovie != null) {
            return ResponseEntity.ok(updatedMovie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        boolean success = movieService.deleteMovie(id);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
