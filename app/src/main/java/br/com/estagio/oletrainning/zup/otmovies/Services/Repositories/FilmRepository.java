package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;

import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository extends CommonRepository{

    private FilmService filmService;
    private int SUCCESS_CODE = 200;
    private String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private int SESSION_EXPIRED_CODE = 401;
    private static final int FIRST_PAGE = 1;
    private boolean isGetGenreListSessionExpired;
    private boolean isGetFilmsResultsSessionExpired;
    private boolean isGetFilmsResultsLoadInitialSessionExpired;
    private boolean isGetFilmsResultsLoadBeforeSessionExpired;
    private boolean isGetFilmsResultsLoadAfterSessionExpired;
    private boolean sessionExpiredControl;


    private MutableLiveData<ErrorMessage> thereIsPaginationError = new MutableLiveData<>();
    private MutableLiveData<Boolean> viewModelTellerIsSessionExpiredPagination = new MutableLiveData<>();

    public MutableLiveData<Boolean> getViewModelTellerIsSessionExpiredPagination() {
        return viewModelTellerIsSessionExpiredPagination;
    }

    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }

    public FilmRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        sessionExpiredControl = isGetGenreListSessionExpired && isGetFilmsResultsSessionExpired &&
                isGetFilmsResultsLoadInitialSessionExpired && isGetFilmsResultsLoadBeforeSessionExpired
                && isGetFilmsResultsLoadAfterSessionExpired;
    }

    public LiveData<ResponseModel<FilmGenres>> getGenreList() {
        final MutableLiveData<ResponseModel<FilmGenres>> data = new MutableLiveData<>();
        filmService.getGenres()
                .enqueue(new Callback<FilmGenres>() {
                    @Override
                    public void onResponse(Call<FilmGenres> call, Response<FilmGenres> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<FilmGenres> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            if(!sessionExpiredControl){
                                responseModel.setCode(SESSION_EXPIRED_CODE);
                                isGetGenreListSessionExpired = true;
                            }
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
                    public void onFailure(Call<FilmGenres> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<FilmsResults>> getFilmsResults(String page, String genreID) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getMovieGenre("genres",genreID,"20",page)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<FilmsResults> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            if(!sessionExpiredControl){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                                isGetFilmsResultsSessionExpired = true;
                            }
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

    public void getFilmsResultsLoadInitial (
            final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback,
            String firstPage, String genreID) {
        filmService.getMovieGenre("genres",genreID,"20",firstPage)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            if(!sessionExpiredControl){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                                isGetFilmsResultsLoadInitialSessionExpired = true;
                            }
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getFilmsResultsLoadBefore (
            final PageKeyedDataSource.LoadParams<Integer> params, final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String genreID) {
        filmService.getMovieGenre("genres",genreID,"20",String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getResults(),key);
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            if(!sessionExpiredControl){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                                isGetFilmsResultsLoadBeforeSessionExpired = true;
                            }
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getFilmsResultsloadAfter (
            final Integer PAGE_SIZE, final PageKeyedDataSource.LoadParams<Integer> params,
            final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String genreID) {
        filmService.getMovieGenre("genres",genreID,"20",String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            Integer key = (params.key < PAGE_SIZE)? params.key + 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            if(!sessionExpiredControl){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                                isGetFilmsResultsLoadAfterSessionExpired = true;
                            }
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }
}
