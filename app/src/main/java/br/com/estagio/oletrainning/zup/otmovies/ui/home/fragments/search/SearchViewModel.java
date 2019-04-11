package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.ui.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonGenreID;

public class SearchViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();
    private MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao buscar filmes. Verifique a conexão e tente novamente.";

    public MutableLiveData<Boolean> getFragmentTellerIsSessionExpired() {
        return fragmentTellerIsSessionExpired;
    }

    private Observer<Boolean> isSessionExpiredPaginationObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(isSessionExpired){
                fragmentTellerIsSessionExpired.setValue(true);
            }
        }
    };

    public void executeServiceGetFilmResults(String page, String filterID, String filter) {
        isLoading.setValue(true);
        setupObserversForever();
        if(SingletonGenreID.INSTANCE.getGenreID() != null){
            filmsResults = filmRepository.getFilmsResults(page, filterID,filter);
            filmsResults.observeForever(filmsResultsObserver);
        }
    }

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {

                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever(){
        favoriteListRepository.getViewModelTellerIsSessionExpired().observeForever(isSessionExpiredPaginationObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (favoriteListRepository.getViewModelTellerIsSessionExpired() != null)  {
            favoriteListRepository.getViewModelTellerIsSessionExpired().removeObserver(isSessionExpiredPaginationObserver);
        }
    }
}
