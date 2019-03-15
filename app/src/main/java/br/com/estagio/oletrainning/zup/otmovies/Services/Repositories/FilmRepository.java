package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Mapper.FilmMapper;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;

import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository extends CommonRepository{

    private FilmService filmService;

    public FilmRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
    }

    public LiveData<ResponseModel<FilmGenres>> getGenreList() {
        final MutableLiveData<ResponseModel<FilmGenres>> data = new MutableLiveData<>();
        filmService.getGenres()
                .enqueue(new Callback<FilmGenres>() {
                    @Override
                    public void onResponse(Call<FilmGenres> call, Response<FilmGenres> response) {
                        SingletonAccessToken.saveAccessToken(response.headers().get("x-access-token"));
                        ResponseModel<FilmGenres> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            responseModel.setCode(SESSION_EXPIRED_CODE);
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

    public LiveData<ResponseModel<List<Film>>> getMovieGenre(String genreID) {
        final MutableLiveData<ResponseModel<List<Film>>> data = new MutableLiveData<>();
        filmService.getMovieGenre("genres",genreID,"20","1")
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.saveAccessToken(response.headers().get("x-access-token"));
                        ResponseModel<List<Film>> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(FilmMapper
                                    .reponseForDomain(response.body().getResults()));
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            responseModel.setCode(SESSION_EXPIRED_CODE);
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
}
