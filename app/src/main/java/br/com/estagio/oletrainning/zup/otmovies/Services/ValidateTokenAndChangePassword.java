package br.com.estagio.oletrainning.zup.otmovies.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ValidateTokenAndChangePassword {

    @PUT("users/password")
    Call<Void> validateTokenAndChangePass(@Body BodyChangePassword bodyChangePassword, @Query("gw-app-key") String gwkey);
}
