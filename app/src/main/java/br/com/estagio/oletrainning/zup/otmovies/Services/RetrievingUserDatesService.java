package br.com.estagio.oletrainning.zup.otmovies.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrievingUserDatesService {

    @GET("users/{email}")
    Call<UserDates> getUsersDate(@Path("email") String email, @Query("gw-app-key") String gwkey);
}
