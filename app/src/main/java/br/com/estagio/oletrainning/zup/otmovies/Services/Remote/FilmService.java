package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmService {


    @GET("discover/movie")
    Call<FilmsResults> getMovieGenre(@Query("language") String language,
                                     @Query("sort_by") String sortBy,
                                     @Query("page") String page,
                                     @Query("with_genres") String genreID);

    @GET("genre/movie/list")
    Call<FilmGenres> getGenres(@Query("language") String language);
}
