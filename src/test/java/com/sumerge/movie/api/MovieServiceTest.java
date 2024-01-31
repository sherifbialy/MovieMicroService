package com.sumerge.movie.api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@SpringBootTest
class MovieServiceTest {

    @Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMovies() {

        Mockito.when(repository.getMovieById(anyLong()))
                .thenReturn(

                        new Movie(1L, true, "path1", "lang1", "title1", "overview1", 1.0, "poster1", "2022-01-01", "Title 1", true, 5.0, 100)

                );


        List<Movie> result = movieService.getMovies(1, 2);


        List<Movie> expected = Arrays.asList(
                new Movie(1L, true, "path1", "lang1", "title1", "overview1", 1.0, "poster1", "2022-01-01", "Title 1", true, 5.0, 100),
                new Movie(1L, true, "path1", "lang1", "title1", "overview1", 1.0, "poster1", "2022-01-01", "Title 1", true, 5.0, 100)
        );

        assertEquals(expected.get(0), result.get(1));
    }


    @Test
    void testGetMovieById() {

        Mockito.when(repository.getMovieById(1L))
                .thenReturn(new Movie(1L, true, "path1", "lang1", "title1", "overview1", 1.0, "poster1", "2022-01-01", "Title 1", true, 5.0, 100));

        Movie result = movieService.getMovieById(1L);

        Movie expected = new Movie(1L, true, "path1", "lang1", "title1", "overview1", 1.0, "poster1", "2022-01-01", "Title 1", true, 5.0, 100);
        assertEquals(expected, result);
    }
}
