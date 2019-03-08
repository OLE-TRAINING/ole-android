package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Services.Mapper.FilmMapper;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDataSource extends PageKeyedDataSource <Integer, Film>{

    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 1;

    private FilmService filmService;

    public FilmDataSource(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
    }



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Film> callback) {


    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Film> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Film> callback) {

    }
}
