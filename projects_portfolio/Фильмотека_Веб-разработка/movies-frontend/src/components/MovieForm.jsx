import React, { useState } from 'react';

function MovieForm({ genres, onCreate }) {
  const [title, setTitle] = useState('');
  const [year, setYear] = useState('');
  const [rating, setRating] = useState('');
  const [genreId, setGenreId] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!title || !year || !rating || !genreId) {
      alert('Пожалуйста, заполните все поля');
      return;
    }
    const newMovie = {
      title,
      year: parseInt(year),
      rating: parseFloat(rating),
      genreId: parseInt(genreId),
    };
    onCreate(newMovie);
    setTitle('');
    setYear('');
    setRating('');
    setGenreId('');
  };

  return (
    <form className="movie-form" onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Название фильма"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <input
        type="number"
        placeholder="Год выпуска"
        value={year}
        onChange={(e) => setYear(e.target.value)}
        required
      />
      <input
        type="number"
        step="0.1"
        placeholder="Рейтинг (0–10)"
        value={rating}
        onChange={(e) => setRating(e.target.value)}
        required
      />
      <select value={genreId} onChange={(e) => setGenreId(e.target.value)} required>
        <option value="">Выберите жанр</option>
        {genres.map(genre => (
          <option key={genre.id} value={genre.id}>{genre.name}</option>
        ))}
      </select>
      <button type="submit">Создать фильм</button>
    </form>
  );
}

export default MovieForm;