package br.com.estagio.oletrainning.zup.otmovies.Services;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ResendTokenToEmail {

    @PUT("tokens/{email}")
    Call<Void> resendToken(@Path("email") String email, @Query("gw-app-key") String gwkey);
}
