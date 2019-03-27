package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmService {


    @GET("movies")
    Call<FilmsResults> getMovieGenre(@Query("filter") String filter,
                                     @Query("filter_id") String filterID,
                                     @Query("amount") String amount,
                                     @Query("page") String page);

    @GET("genres")
    Call<FilmGenres> getGenres();
}
