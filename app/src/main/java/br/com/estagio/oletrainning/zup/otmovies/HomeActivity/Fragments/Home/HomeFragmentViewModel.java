package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class HomeFragmentViewModel extends CommonViewModel {

    private int SUCCESS_CODE = 200;
    private int SESSION_EXPIRED_CODE = 401;

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<FilmGenres>> getGenreList;

    private LiveData<ResponseModel<List<Film>>> getMovieGenre;

    private MutableLiveData<FilmGenres> thereIsAGenreList = new MutableLiveData<>();

    private MutableLiveData<List<Film>> thereIsAMovieGenre = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSessionExpired = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsSessionExpired() {
        return isSessionExpired;
    }

    public MutableLiveData<FilmGenres> getThereIsAGenreList() {
        return thereIsAGenreList;
    }

    public MutableLiveData<List<Film>> getThereIsAMovieGenre() {
        return thereIsAMovieGenre;
    }

    public void executeServiceGetGenreList() {
        getGenreList = filmRepository.getGenreList();
        getGenreList.observeForever(filmGenresObserver);
    }

    private Observer<ResponseModel<FilmGenres>> filmGenresObserver = new Observer<ResponseModel<FilmGenres>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmGenres> responseFilmGenres) {
            if(responseFilmGenres != null && responseFilmGenres.getCode() == SUCCESS_CODE){
                thereIsAGenreList.setValue(responseFilmGenres.getResponse());
            } else if (responseFilmGenres != null && responseFilmGenres.getCode() == SESSION_EXPIRED_CODE){
                isSessionExpired.setValue(true);
            }
        }
    };

    public void executeServiceGetMovieGenre(String genreID) {
        getMovieGenre = filmRepository.getMovieGenre(genreID);
        getMovieGenre.observeForever(filmMovieGenreObserver);
    }

    private Observer<ResponseModel<List<Film>>> filmMovieGenreObserver = new Observer<ResponseModel<List<Film>>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<List<Film>> responseFilmMovieGenre) {
            if(responseFilmMovieGenre != null && responseFilmMovieGenre.getCode() == SUCCESS_CODE){
                thereIsAMovieGenre.setValue(responseFilmMovieGenre.getResponse());
            } else if (responseFilmMovieGenre != null && responseFilmMovieGenre.getCode() == SESSION_EXPIRED_CODE){
                isSessionExpired.setValue(true);
            }
        }
    };
}
