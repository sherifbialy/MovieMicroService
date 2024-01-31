package com.sumerge.movie.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.sumerge.movie.config.JwtAuthenticationFilter;
import com.sumerge.movie.config.WireMockConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = {WireMockConfig.class})
class MovieControllerTest {

    @Autowired
    private WireMockServer wireMockServer;
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private MovieService movieService;
    @BeforeEach
    void setUp() {

   mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getMoviesWithoutFilter() throws Exception {



        Movie movie1=createDummyMovie1();
        Movie movie2=createDummyMovie2();
        Mockito.when(movieService.getMovies(1,2)).thenReturn(
                List.of(movie1,movie2));

     mockMvc.perform(
             MockMvcRequestBuilders.get("/api/movies?page=1&pageSize=2")
                     .contentType(MediaType.APPLICATION_JSON)

                     .header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
     ).andExpect(status().isOk())
             .andExpect(jsonPath("$.[0].id").value(movie1.getId()));
    }


    @Test
    void getMoviesWithFilterWithoutToken() throws Exception {
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(jwtAuthenticationFilter).build();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/movies?page=1&pageSize=2")
                        .contentType(MediaType.APPLICATION_JSON)

                        .header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
        ).andExpect(status().isForbidden());

    }
    @Test
    void getOneMovieWithoutFilter() throws Exception {
        Movie movie1=createDummyMovie1();
        Mockito.when(movieService.getMovieById(1L)).thenReturn(
                movie1);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/movies/1")
                                .contentType(MediaType.APPLICATION_JSON)

                                //.header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movie1.getId()));


    }

    @Test
    void getOneMovieWithFilterWithoutToken() throws Exception {
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(jwtAuthenticationFilter).build();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)

                        .header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
        ).andExpect(status().isForbidden());
    }

    @Test
    void getMoviesWithValidToken() throws Exception {
        this.wireMockServer.stubFor(get("/api/validate").willReturn(aResponse().withStatus(200)));
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(jwtAuthenticationFilter).build();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)

                        .header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
        ).andExpect(status().isOk());

    }

    public static Movie createDummyMovie1() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setAdult(false);
        movie.setBackdrop_path("/backdrop1.jpg");
        movie.setOriginal_language("en");
        movie.setOriginal_title("Dummy Movie 1");
        movie.setOverview("This is a dummy movie 1.");
        movie.setPopularity(7.5);
        movie.setPoster_path("/poster1.jpg");
        movie.setRelease_date("2022-01-01");
        movie.setTitle("Dummy Movie 1");
        movie.setVideo(false);
        movie.setVote_average(8.0);
        movie.setVote_count(100);
        return movie;
    }

    public static Movie createDummyMovie2() {
        Movie movie = new Movie();
        movie.setId(2L);
        movie.setAdult(true);
        movie.setBackdrop_path("/backdrop2.jpg");
        movie.setOriginal_language("es");
        movie.setOriginal_title("Dummy Movie 2");
        movie.setOverview("This is a dummy movie 2.");
        movie.setPopularity(6.8);
        movie.setPoster_path("/poster2.jpg");
        movie.setRelease_date("2022-02-01");
        movie.setTitle("Dummy Movie 2");
        movie.setVideo(true);
        movie.setVote_average(7.5);
        movie.setVote_count(80);
        return movie;
    }
}