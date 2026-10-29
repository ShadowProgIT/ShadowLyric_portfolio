import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

export const getMovies = () => api.get('/movies');
export const getGenres = () => api.get('/genres');
export const createMovie = (movie) => api.post('/movies', movie);
export const markAsWatched = (id) => api.put(`/movies/${id}/watched`);
export const deleteMovie = (id) => api.delete(`/movies/${id}`);