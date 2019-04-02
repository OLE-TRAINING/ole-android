package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieDetailsFragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;

public class MovieDetailsViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails;

    private MutableLiveData<MovieDetailsModel> thereIsMovieDetails = new MutableLiveData<>();

    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber detalhes do filme. Verifique a conexão e tente novamente.";

    private MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();

    public MutableLiveData<Boolean> getFragmentTellerIsSessionExpired() {
        return fragmentTellerIsSessionExpired;
    }

    public MutableLiveData<MovieDetailsModel> getThereIsMovieDetails() {
        return thereIsMovieDetails;
    }

    public void executeServicegetMovieDetails(int id) {
        isLoading.setValue(true);
        getMovieDetails = filmRepository.getMovieDetails(id);
        getMovieDetails.observeForever(getMovieDetailsObserver);
    }

    private Observer<ResponseModel<MovieDetailsModel>> getMovieDetailsObserver = new Observer<ResponseModel<MovieDetailsModel>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<MovieDetailsModel> movieDetails) {
            isLoading.setValue(false);
            if (movieDetails != null) {
                if (movieDetails.getCode() == SUCCESS_CODE) {
                    thereIsMovieDetails.setValue(movieDetails.getResponse());
                } else if (movieDetails.getCode() == SESSION_EXPIRED_CODE) {
                    fragmentTellerIsSessionExpired.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };
}
