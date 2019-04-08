package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FavoriteListRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;

public class FavoriteFragmentViewModel extends CommonViewModel {

    private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    private LiveData<ResponseModel<FilmsResults>> getFavoriteList;
    private MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();
    private String SERVICE_OR_CONNECTION_ERROR_LIST ="Falha ao receber lista de favoritos. Verifique a conexão e tente novamente.";
    private MutableLiveData<FilmsResults> fragmentTellerThereFavoriteList = new MutableLiveData<>();

    public MutableLiveData<Boolean> getFragmentTellerIsSessionExpired() {
        return fragmentTellerIsSessionExpired;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereFavoriteList() {
        return fragmentTellerThereFavoriteList;
    }

    public void executeAddFavoriteFilm(String email) {
        getFavoriteList = favoriteListRepository.getFavoriteList(email);
        getFavoriteList.observeForever(getFavoriteListObserver);
    }

    private Observer<ResponseModel<FilmsResults>> getFavoriteListObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    fragmentTellerThereFavoriteList.setValue(responseModel.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_LIST);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (getFavoriteList != null)  {
            getFavoriteList.removeObserver(getFavoriteListObserver);
        }
    }
}
