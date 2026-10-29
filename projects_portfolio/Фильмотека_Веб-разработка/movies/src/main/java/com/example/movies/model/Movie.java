package com.example.movies.model;

public class Movie {
    private Long id;
    private String title;
    private Integer year;
    private Double rating;
    private Long genreId;
    private String status;

    public Movie() {
    }

    public Movie(Long id, String title, Integer year, Double rating, Long genreId, String status) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.genreId = genreId;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Long getGenreId() { return genreId; }
    public void setGenreId(Long genreId) { this.genreId = genreId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
