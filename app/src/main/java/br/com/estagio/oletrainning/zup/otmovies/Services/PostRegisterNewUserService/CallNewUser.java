package br.com.estagio.oletrainning.zup.otmovies.Services.PostRegisterNewUserService;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallNewUser implements Call<Void> {
    @Override
    public Response execute() throws IOException {
        return null;
    }

    @Override
    public void enqueue(Callback callback) {

    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }
}
