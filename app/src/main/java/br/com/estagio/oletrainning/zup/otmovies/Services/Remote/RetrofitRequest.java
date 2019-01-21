package br.com.estagio.oletrainning.zup.otmovies.Services.Remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static final String URL = "https://ole.dev.gateway.zup.me/client-training/v1/";

    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .connectTimeout(60,TimeUnit.SECONDS)
                    .readTimeout(60,TimeUnit.SECONDS)
                    .writeTimeout(60,TimeUnit.SECONDS)
                    .callTimeout(60,TimeUnit.SECONDS);

    private static Retrofit getRetroInstance() {
        return new Retrofit.Builder().baseUrl(URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.build()).build();
    }

    private static Retrofit retrofit = getRetroInstance();

    public static <S> S buildService(Class<S> serviceType){
        return retrofit.create(serviceType);
    }
}
