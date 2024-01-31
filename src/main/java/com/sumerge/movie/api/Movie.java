package com.sumerge.movie.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie {




        @Id
        private Long id;

        private boolean adult;
        private String  backdropPath;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private double popularity;
        private String posterPath;
        private String releaseDate;
        private String title;
        private boolean video;
        private double voteAverage;
        private int voteCount;
}
