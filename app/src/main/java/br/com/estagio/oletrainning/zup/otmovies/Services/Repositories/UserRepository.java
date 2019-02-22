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
                .enqueue(new Callback<ResponseModel<UserData>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> response) {
                        if(response.code() == SUCCESS_CODE && response.body().getResponse().getRegistrationStatus()!= null){
                            UserData userData = new UserData();
                            userData.setRegistrationStatus(response.body().getResponse().getRegistrationStatus());
                            ResponseModel<UserData> responseModel = new ResponseModel<>();
                            responseModel.setResponse(userData);
                            data.setValue(responseModel);
                        } else {
                            if(response.errorBody() != null){
                                data.setValue(serializeErrorBody(response));
                            } else {
                                data.setValue(setMessage(UNEXPECTED_ERROR_KEY,UNEXPECTED_ERROR_MESSAGE));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<UserData>> call, Throwable t) {
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
                        if((response.code() == SUCCESS_CODE)) {
                            data.setValue(setCode(response.code()));
                        } else{
                            if(response.errorBody() != null){
                                data.setValue(serializeErrorBody(response));
                            } else {
                                data.setValue(setMessage(UNEXPECTED_ERROR_KEY,UNEXPECTED_ERROR_MESSAGE));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    protected ResponseModel<UserData> setMessage(String key, String message){
        ResponseModel<UserData> responseModel = new ResponseModel();
        responseModel.getErrorMessage().setKey(key);
        responseModel.getErrorMessage().setMessage(message);
        return responseModel;
    }

    protected ResponseModel<UserData> serializeErrorBody(Response response){
        Gson gson = new Gson();
        Type type = new TypeToken<ErrorMessage>() {
        }.getType();
        ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
        ResponseModel<UserData> responseModel = new ResponseModel();
        responseModel.getErrorMessage().setKey(errorMessage.getKey());
        responseModel.getErrorMessage().setMessage(errorMessage.getMessage());
        return responseModel;
    }

    protected ResponseModel<UserData> setCode(int code){
        ResponseModel<UserData> responseModel= new ResponseModel<>();
        responseModel.setCode(code);
        return responseModel;
    }
}
