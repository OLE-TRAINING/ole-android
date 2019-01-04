package br.com.estagio.oletrainning.zup.otmovies.Services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {

    private static final String URL = "https://ole.dev.gateway.zup.me/client-training/v1/";

    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder().addInterceptor(logger).connectTimeout(4000,TimeUnit.SECONDS);


    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp.build());

    private static Retrofit retrofit = builder.build();

    public static <S> S buildService(Class<S> serviceType){
        return retrofit.create(serviceType);
    }
}
