package br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.Services.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.UserServices;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.ValidationServices;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadLineRepository {

    private UserServices userServices;

    private ValidationServices validationServices;

    public HeadLineRepository(){
        userServices= RetrofitServiceBuilder.buildService(UserServices.class);
        validationServices = RetrofitServiceBuilder.buildService(ValidationServices.class);
    }

    public LiveData<ResponseModel> getUserDate(String email,
                                               String gwkey) {
        final MutableLiveData<ResponseModel> data = new MutableLiveData<>();
        userServices.getUsersDate(email, gwkey)
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if(response.code() == 200){
                            data.setValue(response.body());
                        } else {
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                ResponseModel responseModel = new ResponseModel();
                                responseModel.setKey(errorMessage.getKey());
                                responseModel.setMessage(errorMessage.getMessage());
                                data.setValue(responseModel);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel> postUserRegister (UserDates userDates,
                                                     String gwkey) {
        final MutableLiveData<ResponseModel> data = new MutableLiveData<>();
        userServices.userRegister(userDates,gwkey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if((response.code() == 200)) {
                            int code = response.code();
                            ResponseModel responseModel = new ResponseModel();
                            responseModel.setCode(code);
                            data.setValue(responseModel);
                        } else{
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                ResponseModel responseModel = new ResponseModel();
                                responseModel.setKey(errorMessage.getKey());
                                responseModel.setMessage(errorMessage.getMessage());
                                data.setValue(responseModel);
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

    public LiveData<ResponseModel> confirmUserName (UserDates userDates,
                                                    String gwkey) {
        final MutableLiveData<ResponseModel> data = new MutableLiveData<>();
        userServices.confirmUserName(userDates,gwkey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if((response.code() == 200)) {
                            int code = response.code();
                            ResponseModel responseModel = new ResponseModel();
                            responseModel.setCode(code);
                            data.setValue(responseModel);
                        } else{
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                ResponseModel responseModel = new ResponseModel();
                                responseModel.setKey(errorMessage.getKey());
                                responseModel.setMessage(errorMessage.getMessage());
                                data.setValue(responseModel);
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

    public LiveData<ResponseModel> resendtoken (String email,
                                                String gwkey) {
        final MutableLiveData<ResponseModel> data = new MutableLiveData<>();
        validationServices.resendToken(email,gwkey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if((response.code() == 200)) {
                            int code = response.code();
                            ResponseModel responseModel = new ResponseModel();
                            responseModel.setCode(code);
                            data.setValue(responseModel);
                        } else{
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                ResponseModel responseModel = new ResponseModel();
                                responseModel.setKey(errorMessage.getKey());
                                responseModel.setMessage(errorMessage.getMessage());
                                data.setValue(responseModel);
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

    public LiveData<ResponseModel> confirmToken (String email, String code,
                                                 String gwkey) {
        final MutableLiveData<ResponseModel> data = new MutableLiveData<>();
        validationServices.confirmToken(email, code, gwkey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if((response.code() == 200)) {
                            int code = response.code();
                            ResponseModel responseModel = new ResponseModel();
                            responseModel.setCode(code);
                            data.setValue(responseModel);
                        } else{
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                ResponseModel responseModel = new ResponseModel();
                                responseModel.setKey(errorMessage.getKey());
                                responseModel.setMessage(errorMessage.getMessage());
                                data.setValue(responseModel);
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

    public LiveData<ResponseModel> validateTokenAndChangePass (BodyChangePassword bodyChangePassword,
                                                               String gwkey) {
        final MutableLiveData<ResponseModel> data = new MutableLiveData<>();
        validationServices.validateTokenAndChangePass(bodyChangePassword, gwkey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if((response.code() == 200)) {
                            int code = response.code();
                            ResponseModel responseModel = new ResponseModel();
                            responseModel.setCode(code);
                            data.setValue(responseModel);
                        } else{
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                ResponseModel responseModel = new ResponseModel();
                                responseModel.setKey(errorMessage.getKey());
                                responseModel.setMessage(errorMessage.getMessage());
                                data.setValue(responseModel);
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
