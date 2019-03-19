package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;

public class MovieListFragmentViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();
    private LiveData<ResponseModel<FilmsResults>> getFilmsResults;

    private MutableLiveData<Integer> thereIsAPageSize = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> thereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<String> isMessageErrorToast = new MutableLiveData<>();
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";


    public MutableLiveData<Integer> getThereIsAPageSize() {
        return thereIsAPageSize;
    }

    public MutableLiveData<String> getIsMessageErrorToast() {
        return isMessageErrorToast;
    }

    private Observer<ResponseModel<FilmsResults>> getFilmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    thereIsFilmResults.setValue(responseModel.getResponse());
                    thereIsAPageSize.setValue(responseModel.getResponse().getTotal_pages());
                } else {
                    String message = responseModel.getErrorMessage().getMessage();
                    getIsMessageErrorToast().setValue(message);
                }
            } else {
                getIsMessageErrorToast().setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    public void executeServiceGetFilmResults(String page, String genreID) {
        isLoading.setValue(true);
        getFilmsResults = filmRepository.getFilmsResults(page,genreID);
        getFilmsResults.observeForever(getFilmsResultsObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
    }
    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if(errorMessage != null){
                getIsMessageErrorToast().setValue(errorMessage.getMessage());
            }
        }
    };

    public void removeObserver() {
        if (getFilmsResults != null && filmRepository.getThereIsPaginationError() != null) {
            getFilmsResults.removeObserver(getFilmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
        }
    }
}
