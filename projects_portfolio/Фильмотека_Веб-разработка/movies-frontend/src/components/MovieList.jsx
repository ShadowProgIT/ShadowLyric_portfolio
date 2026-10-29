import React from 'react';

function MovieList({ title, movies, showWatchedButton, onMarkWatched, onDelete, genres }) {
  const getGenreName = (genreId) => {
    const genre = genres.find(g => g.id === genreId);
    return genre ? genre.name : 'Неизвестный жанр';
  };

  return (
    <div className="movie-list">
      <h2>{title} ({movies.length})</h2>
      {movies.length === 0 && <p>Нет фильмов</p>}
      <ul>
        {movies.map(movie => (
          <li key={movie.id} className="movie-item">
            <div className="movie-info">
              <strong>{movie.title}</strong> ({movie.year})
              <br />
              Рейтинг: {movie.rating} | Жанр: {getGenreName(movie.genreId)} | Статус: {movie.status === 'WATCH_LATER' ? 'К просмотру' : 'Просмотрено'}
            </div>
            <div className="movie-actions">
              {showWatchedButton && (
                <button onClick={() => onMarkWatched(movie.id)} className="watched-btn">
                  Просмотрено
                </button>
              )}
              <button onClick={() => onDelete(movie.id)} className="delete-btn">
                Удалить
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default MovieList;