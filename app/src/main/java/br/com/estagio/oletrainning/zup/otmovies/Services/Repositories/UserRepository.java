package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.UserServices;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private UserServices userServices;

    private String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private int SUCCESS_CODE = 200;


    public UserRepository(){
        userServices= RetrofitServiceBuilder.buildService(UserServices.class);
    }

    public LiveData<ResponseModel<UserData>> getUserData(String email) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        userServices.getUsersDate(email)
                .enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE){
                            responseModel.setResponse(response.body());
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
                    public void onFailure(Call<UserData> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<UserData>> postUserRegister (UserData userData) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        userServices.userRegister(userData)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if((response.code() == SUCCESS_CODE)) {
                            responseModel.setCode(response.code());
                        } else{
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

    protected ErrorMessage serializeErrorBody(ResponseBody response){
        Gson gson = new Gson();
        Type type = new TypeToken<ErrorMessage>() {
        }.getType();
        return gson.fromJson(response.charStream(), type);
    }
}
