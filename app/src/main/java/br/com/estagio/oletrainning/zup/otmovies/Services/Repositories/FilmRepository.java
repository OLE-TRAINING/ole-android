package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Services.Mapper.FilmMapper;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.APIService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository {

    private FilmService filmService;

    public FilmRepository(){
        filmService = APIService.buildService(FilmService.class);
    }

    public LiveData<FilmGenres> getGenreList() {
        final MutableLiveData<FilmGenres> data = new MutableLiveData<>();
        filmService.getGenres("pt-BR")
                .enqueue(new Callback<FilmGenres>() {
                    @Override
                    public void onResponse(Call<FilmGenres> call, Response<FilmGenres> response) {
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
        filmService.getMovieGenre("pt-BR","popularity.desc","1",genreID)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
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
