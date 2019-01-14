package br.com.estagio.oletrainning.zup.otmovies.Services;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SendTokenToValidade {
    @POST("users/{email}/register/{code}")
    Call<Void> confirmToken(@Path("email") String email, @Path("code") String code, @Query("gw-app-key") String gwkey);
}
