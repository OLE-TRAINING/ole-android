package br.com.estagio.oletrainning.zup.otmovies.Services.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.UserResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.APIRequest;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadLineRepository {

    private APIRequest apiService;

    private static class SingletonHelper
    {
        private static final HeadLineRepository INSTANCE = new HeadLineRepository();
    }
    public static HeadLineRepository getInstance()
    {
        return SingletonHelper.INSTANCE;
    }
    public HeadLineRepository()
    {
        apiService= RetrofitRequest.buildService(APIRequest.class);
    }
    public LiveData<UserResponse> getHeadLine(String email,
                                              String gwkey)
    {
        final MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getUsersDate(email, gwkey)
                .enqueue(new Callback<UserResponse>()
                {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response)
                    {
                        if (response.isSuccessful())
                        {
                            data.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }
}
