package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class HomeFragmentViewModel extends CommonViewModel {

    private int SUCCESS_CODE = 200;
    private int SESSION_EXPIRED_CODE = 401;

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<FilmGenres>> getGenreList;

    private MutableLiveData<FilmGenres> thereIsAGenreList = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSessionExpired = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsSessionExpired() {
        return isSessionExpired;
    }

    public MutableLiveData<FilmGenres> getThereIsAGenreList() {
        return thereIsAGenreList;
    }

    public void executeServiceGetGenreList() {
        isLoading.setValue(true);
        getGenreList = filmRepository.getGenreList();
        getGenreList.observeForever(filmGenresObserver);
    }

    private Observer<ResponseModel<FilmGenres>> filmGenresObserver = new Observer<ResponseModel<FilmGenres>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmGenres> responseFilmGenres) {
            isLoading.setValue(false);
            if(responseFilmGenres != null && responseFilmGenres.getCode() == SUCCESS_CODE){
                thereIsAGenreList.setValue(responseFilmGenres.getResponse());
            } else if (responseFilmGenres != null && responseFilmGenres.getCode() == SESSION_EXPIRED_CODE){
                isSessionExpired.setValue(true);
            }
        }
    };
}
