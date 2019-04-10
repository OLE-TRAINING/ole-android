package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class FavoriteFragmentViewModel extends CommonViewModel {

    FilmRepository filmRepository = new FilmRepository();
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    public void executeServiceGetFilmResults(String page, String filterID, String filter) {
        isLoading.setValue(true);
        if(SingletonGenreID.INSTANCE.getGenreID() != null){
            filmsResults = filmRepository.getFilmsResults(page,filterID,filter);
            filmsResults.observeForever(filmsResultsObserver);
        }
    }

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null)  {
            filmsResults.removeObserver(filmsResultsObserver);
        }
    }
}
