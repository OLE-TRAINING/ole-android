package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteListRepository extends CommonRepository {

    private FilmService filmService;
    private MutableLiveData<ErrorMessage> thereIsPaginationError;
    private MutableLiveData<Boolean> viewModelTellerIsSessionExpiredPagination;

    public FavoriteListRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        thereIsPaginationError = new MutableLiveData<>();
        viewModelTellerIsSessionExpiredPagination = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getViewModelTellerIsSessionExpiredPagination() {
        return viewModelTellerIsSessionExpiredPagination;
    }

    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }

    public LiveData<ResponseModel<FilmsResults>> getFavoriteList(String email) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getFavoriteList(email)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<FilmsResults> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<Void>> addFavotiteFilm (String email,String movieID) {
        final MutableLiveData<ResponseModel<Void>> data = new MutableLiveData<>();
        filmService.addFavotiteFilm(email,movieID)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<Void> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<Void>> removeFavotiteFilm (String email,String movieID) {
        final MutableLiveData<ResponseModel<Void>> data = new MutableLiveData<>();
        filmService.removeFavotiteFilm(email,movieID)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<Void> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }


}
