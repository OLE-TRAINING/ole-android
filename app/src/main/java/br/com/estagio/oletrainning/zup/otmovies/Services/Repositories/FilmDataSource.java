package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;


import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilmDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    public Integer PAGE_SIZE;
    private static final int FIRST_PAGE = 1;
    private String genreID;

    public FilmDataSource(Integer pageSize, String genreID) {
        this.PAGE_SIZE = pageSize;
        this.genreID = genreID;
    }

    public FilmDataSource() {

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, FilmResponse> callback) {

        RetrofitServiceBuilder.getInsance().getApi().getMovieGenre(genreID, "genre", "20", "1")
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {

        RetrofitServiceBuilder.getInsance().getApi().getMovieGenre(genreID, "genre", "20", String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.body() != null) {
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {

        RetrofitServiceBuilder.getInsance().getApi().getMovieGenre(genreID, "genre", "20", String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.body() != null) {
                            Integer key = (params.key < PAGE_SIZE) ? params.key + 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {

                    }
                });

    }
}
