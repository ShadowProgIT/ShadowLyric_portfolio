package com.example.movies.storage;

import com.example.movies.model.Genre;
import com.example.movies.model.Movie;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {

    private final List<Genre> genres = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();

    private final AtomicLong nextMovieId = new AtomicLong(1);

    @PostConstruct
    public void init() {
        genres.add(new Genre(1L, "Боевик"));
        genres.add(new Genre(2L, "Комедия"));
        genres.add(new Genre(3L, "Драма"));
        genres.add(new Genre(4L, "Фантастика"));

        movies.add(new Movie(nextMovieId.getAndIncrement(), "Безумный Макс: Дорога ярости", 2015, 8.1, 1L, "WATCH_LATER"));
        movies.add(new Movie(nextMovieId.getAndIncrement(), "Форрест Гамп", 1994, 9.0, 3L, "WATCH_LATER"));
        movies.add(new Movie(nextMovieId.getAndIncrement(), "Интерстеллар", 2014, 8.6, 4L, "WATCHED"));
        movies.add(new Movie(nextMovieId.getAndIncrement(), "Мальчишник в Вегасе", 2009, 7.7, 2L, "WATCH_LATER"));
    }

    public List<Genre> getAllGenres() {
        return new ArrayList<>(genres);
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public Movie addMovie(Movie movie) {
        movie.setId(nextMovieId.getAndIncrement());
        movies.add(movie);
        return movie;
    }

    public boolean deleteMovie(Long id) {
        return movies.removeIf(m -> m.getId().equals(id));
    }

    public Movie getMovieById(Long id) {
        return movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
