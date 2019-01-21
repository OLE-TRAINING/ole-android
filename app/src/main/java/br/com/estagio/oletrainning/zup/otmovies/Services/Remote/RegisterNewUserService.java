package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterNewUserService {

    @POST ("users")
    Call <Void> userRegister(@Body UserDates newUser, @Query("gw-app-key") String gwkey);
}
