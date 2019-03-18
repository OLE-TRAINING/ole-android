package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;

public class MovieListFragmentViewModel extends CommonViewModel {

    private int SUCCESS_CODE = 200;
    private int SESSION_EXPIRED_CODE = 401;

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<List<Film>>> getMovieGenre;

    private MutableLiveData<List<Film>> thereIsAMovieGenre = new MutableLiveData<>();

    public MutableLiveData<List<Film>> getThereIsAMovieGenre() {
        return thereIsAMovieGenre;
    }

    private Observer<ResponseModel<List<Film>>> filmMovieGenreObserver = new Observer<ResponseModel<List<Film>>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<List<Film>> responseFilmMovieGenre) {
            isLoading.setValue(false);
            if (responseFilmMovieGenre != null && responseFilmMovieGenre.getCode() == SUCCESS_CODE) {
                thereIsAMovieGenre.setValue(responseFilmMovieGenre.getResponse());
            } else if (responseFilmMovieGenre != null && responseFilmMovieGenre.getCode() == SESSION_EXPIRED_CODE) {
                isSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (getMovieGenre != null) {
            getMovieGenre.removeObserver(filmMovieGenreObserver);
        }
    }
}
