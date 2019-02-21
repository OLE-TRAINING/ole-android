package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.ValidationServices;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidationRepository  extends UserRepository{

    private ValidationServices validationServices;

    private String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private int SUCCESS_CODE = 200;


    public ValidationRepository(){
        validationServices = RetrofitServiceBuilder.buildService(ValidationServices.class);
    }

    public LiveData<ResponseModel<UserData>> resendToken (String email) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.resendToken(email)
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

    public LiveData<ResponseModel<UserData>> confirmToken (String email, String code) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.confirmToken(email, code)
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

    public LiveData<ResponseModel<UserData>> validateTokenAndChangePass (BodyChangePassword bodyChangePassword) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.validateTokenAndChangePass(bodyChangePassword)
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

    public LiveData<ResponseModel<UserData>> passwordValidate (UserData userData) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.passwordValidate(userData)
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
}
