package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Services.Mapper.FilmMapper;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository {

    private FilmService filmService;

    public FilmRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
    }

    public LiveData<FilmGenres> getGenreList() {
        final MutableLiveData<FilmGenres> data = new MutableLiveData<>();
        filmService.getGenres()
                .enqueue(new Callback<FilmGenres>() {
                    @Override
                    public void onResponse(Call<FilmGenres> call, Response<FilmGenres> response) {
                        SingletonAccessToken.saveAccessToken(response.headers().get("x-access-token"));
                        if(response.body() !=null){
                            if(response.isSuccessful()){
                                data.setValue(response.body());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmGenres> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<List<Film>> getMovieGenre(String genreID) {
        final MutableLiveData<List<Film>> data = new MutableLiveData<>();
        filmService.getMovieGenre("genres",genreID,"20","1")
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.saveAccessToken(response.headers().get("x-access-token"));
                        if(response.body() !=null){
                            if(response.isSuccessful()){
                                data.setValue(FilmMapper
                                        .reponseForDomain(response.body().getResults()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }
}
