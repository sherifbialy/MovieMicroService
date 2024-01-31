package com.sumerge.movie.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @RequestMapping("/movies")
    public List<Movie> getMovies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ){

        return service.getMovies(page,pageSize);
    }
    @RequestMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovies(@PathVariable Long id){
        return ResponseEntity.ok( service.getMovieById(id));
    }


}
