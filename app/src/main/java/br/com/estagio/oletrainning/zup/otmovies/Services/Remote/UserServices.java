package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServices {

    @GET("users/{email}")
    Call<UserData> getUsersDate(@Path("email") String email);

    @POST("users")
    Call <Void> userRegister(@Body UserData newUser);
}
