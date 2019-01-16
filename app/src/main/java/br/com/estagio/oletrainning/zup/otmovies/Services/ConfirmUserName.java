package br.com.estagio.oletrainning.zup.otmovies.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ConfirmUserName {
    @POST("users/confirm-data")
    Call<Void> userRegister(@Body UserDates newUser, @Query("gw-app-key") String gwkey);
}
