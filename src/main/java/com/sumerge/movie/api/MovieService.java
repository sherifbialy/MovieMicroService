package com.sumerge.movie.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repository;

    public List<Movie> getMovies(int page,int pageSize){
        List<Movie> res=new ArrayList<Movie>();
        long start =  page==1?1L: (long) (page - 1) *pageSize;
        for (long i = start; i < start+pageSize; i++) {
            System.out.println(i);
            if(getMovieById(i)==null){
                continue;
            }
            res.add(getMovieById(i));

        }
        return res;
    }

    public Movie getMovieById(Long id){
        return this.repository.getMovieById(id);
    }
}
