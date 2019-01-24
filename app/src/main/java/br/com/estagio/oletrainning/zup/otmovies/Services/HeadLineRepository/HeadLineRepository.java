package br.com.estagio.oletrainning.zup.otmovies.Services.HeadLineRepository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import br.com.estagio.oletrainning.zup.otmovies.Services.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.UserServices;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadLineRepository {

    private UserServices userServices;

    public HeadLineRepository(){
        userServices= RetrofitServiceBuilder.buildService(UserServices.class);
    }

    public LiveData<UserResponse> getUserDate(String email,
                                              String gwkey) {
        final MutableLiveData<UserResponse> data = new MutableLiveData<>();
        userServices.getUsersDate(email, gwkey)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.code() == 200){
                            data.setValue(response.body());
                        } else {
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                UserResponse userResponse = new UserResponse();
                                userResponse.setKey(errorMessage.getKey());
                                userResponse.setMessage(errorMessage.getMessage());
                                data.setValue(userResponse);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<UserResponse> postUserRegister (UserDates userDates,
                                                    String gwkey) {
        final MutableLiveData<UserResponse> data = new MutableLiveData<>();
        userServices.userRegister(userDates,gwkey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if((response.code() == 200)) {
                            int code = response.code();
                            UserResponse userResponse = new UserResponse();
                            userResponse.setCode(code);
                            data.setValue(userResponse);
                        } else{
                            if(response.errorBody() != null){
                                Gson gson = new Gson();
                                Type type = new TypeToken<ErrorMessage>() {
                                }.getType();
                                ErrorMessage errorMessage = gson.fromJson(response.errorBody().charStream(), type);
                                UserResponse userResponse = new UserResponse();
                                userResponse.setKey(errorMessage.getKey());
                                userResponse.setMessage(errorMessage.getMessage());
                                data.setValue(userResponse);
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
