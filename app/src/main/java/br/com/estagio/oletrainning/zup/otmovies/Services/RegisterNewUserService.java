package br.com.estagio.oletrainning.zup.otmovies.Services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterNewUserService {

    @POST ("users")
    Call <Void> userRegister(@Body UserData newUser, @Query("gw-app-key") String gwkey);
}
