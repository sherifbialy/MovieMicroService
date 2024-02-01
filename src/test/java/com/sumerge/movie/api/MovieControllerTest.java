package com.sumerge.movie.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.sumerge.movie.config.JwtAuthenticationFilter;
import com.sumerge.movie.config.WireMockConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = {WireMockConfig.class})
class MovieControllerTest {



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
    @RegisterExtension
    static WireMockExtension wireMockServer=
            WireMockExtension.newInstance().options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();
  @DynamicPropertySource
   static  void configureProperties(DynamicPropertyRegistry registry){
      String customPath = "/api/validate";
      String fullPath = wireMockServer.baseUrl() + customPath;


      registry.add("myapp.auth_endpoint", () -> fullPath);
  }


    @Test
    void getMoviesWithoutFilter() throws Exception {
       //System.setProperty()


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
        wireMockServer.stubFor(get("/api/validate").willReturn(aResponse().withStatus(403)));
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
        wireMockServer.stubFor(get("/api/validate").willReturn(aResponse().withStatus(403)));
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(jwtAuthenticationFilter).build();
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)

                        .header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiZWx5YWtAcG9seWEucnUiLCJpYXQiOjE3MDY2MzM4NjYsImV4cCI6MTcwNjYzNTY2Nn0.6qbCOt0furCWL8oS9OGhc8XkEUGPWw7t5C4tXDlgtoY")
        ).andExpect(status().isForbidden());
    }

    @Test
    void getMoviesWithValidToken() throws Exception {

        wireMockServer.stubFor(get("/api/validate").willReturn(aResponse().withStatus(200)));
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
        movie.setBackdropPath("/backdrop1.jpg");
        movie.setOriginalLanguage("en");
        movie.setOriginalTitle("Dummy Movie 1");
        movie.setOverview("This is a dummy movie 1.");
        movie.setPopularity(7.5);
        movie.setPosterPath("/poster1.jpg");
        movie.setReleaseDate("2022-01-01");
        movie.setTitle("Dummy Movie 1");
        movie.setVideo(false);
        movie.setVoteAverage(8.0);
        movie.setVoteCount(100);
        return movie;
    }

    public static Movie createDummyMovie2() {
        Movie movie = new Movie();
        movie.setId(2L);
        movie.setAdult(true);
        movie.setBackdropPath("/backdrop2.jpg");
        movie.setOriginalLanguage("es");
        movie.setOriginalTitle("Dummy Movie 2");
        movie.setOverview("This is a dummy movie 2.");
        movie.setPopularity(6.8);
        movie.setPosterPath("/poster2.jpg");
        movie.setReleaseDate("2022-02-01");
        movie.setTitle("Dummy Movie 2");
        movie.setVideo(true);
        movie.setVoteAverage(7.5);
        movie.setVoteCount(80);
        return movie;
    }
}