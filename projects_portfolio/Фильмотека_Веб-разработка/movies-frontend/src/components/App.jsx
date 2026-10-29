import React, { useState, useEffect } from 'react';
import { getMovies, getGenres, createMovie, markAsWatched, deleteMovie } from '../api';
import MovieList from './MovieList';
import MovieForm from './MovieForm';
import '../App.css';

function App() {
  const [movies, setMovies] = useState([]);
  const [genres, setGenres] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getMovies(), getGenres()])
      .then(([moviesResponse, genresResponse]) => {
        setMovies(moviesResponse.data);
        setGenres(genresResponse.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Ошибка загрузки данных:', error);
        setLoading(false);
      });
  }, []);

  const handleCreateMovie = (newMovie) => {
    createMovie(newMovie)
      .then(response => {
        setMovies(prevMovies => [...prevMovies, response.data]);
      })
      .catch(error => console.error('Ошибка создания:', error));
  };

  const handleMarkWatched = (id) => {
  markAsWatched(id)
    .then(() => {
      setMovies(prevMovies =>
        prevMovies.map(movie =>
          String(movie.id) === String(id) 
            ? { ...movie, status: 'WATCHED' } 
            : movie
        )
      );
    })
    .catch(error => {
      console.error('Ошибка обновления статуса:', error);
      alert('Не удалось обновить статус фильма');
    });
  };

  const handleDelete = (id) => {
    deleteMovie(id)
      .then(() => {
        setMovies(prevMovies => prevMovies.filter(movie => movie.id !== id));
      })
      .catch(error => console.error('Ошибка удаления:', error));
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  const watchLaterMovies = movies.filter(movie => movie.status === 'WATCH_LATER');
  const watchedMovies = movies.filter(movie => movie.status === 'WATCHED');

  return (
    <div className="app-container">
      <header className="app-header">
        <h1>Моя фильмотека</h1>
      </header>

      <section className="create-section">
        <h2>Добавить новый фильм</h2>
        <MovieForm genres={genres} onCreate={handleCreateMovie} />
      </section>

      <div className="movies-sections">
        <div className="column">
          <MovieList
            title="К просмотру"
            movies={watchLaterMovies}
            showWatchedButton={true}
            onMarkWatched={handleMarkWatched}
            onDelete={handleDelete}
            genres={genres}
          />
        </div>
        <div className="column">
          <MovieList
            title="Просмотрено"
            movies={watchedMovies}
            showWatchedButton={false}
            onMarkWatched={handleMarkWatched}
            onDelete={handleDelete}
            genres={genres}
          />
        </div>
      </div>
    </div>
  );
}

export default App;