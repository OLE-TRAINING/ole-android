package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserServices {

    @GET("users/{email}")
    Call<ResponseModel> getUsersDate(@Path("email") String email, @Query("gw-app-key") String gwkey);

    @POST("users")
    Call <Void> userRegister(@Body UserDates newUser, @Query("gw-app-key") String gwkey);

    @POST("users/confirm-data")
    Call<Void> confirmUserName(@Body UserDates newUser, @Query("gw-app-key") String gwkey);
}
