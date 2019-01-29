package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import br.com.estagio.oletrainning.zup.otmovies.Services.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.Services.UserDates;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ValidationServices {

    @PUT("tokens/{email}")
    Call<Void> resendToken(@Path("email") String email);

    @POST("users/{email}/register/{code}")
    Call<Void> confirmToken(@Path("email") String email, @Path("code") String code);

    @PUT("users/password")
    Call<Void> validateTokenAndChangePass(@Body BodyChangePassword bodyChangePassword);
}
