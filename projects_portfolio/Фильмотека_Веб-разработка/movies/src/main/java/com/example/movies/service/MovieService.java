package com.example.movies.service;

import com.example.movies.model.Genre;
import com.example.movies.model.Movie;
import com.example.movies.storage.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final InMemoryStorage storage;

    public MovieService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public List<Movie> getAllMovies() {
        return storage.getAllMovies();
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Movie createMovie(Movie movie) {
        movie.setStatus("WATCH_LATER");
        return storage.addMovie(movie);
    }

    public Movie markAsWatched(Long id) {
        Movie movie = storage.getMovieById(id);
        if (movie != null) {
            movie.setStatus("WATCHED");
        }
        return movie;
    }

    public boolean deleteMovie(Long id) {
        return storage.deleteMovie(id);
    }
}
