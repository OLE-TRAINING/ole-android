package br.com.estagio.oletrainning.zup.otmovies.Services.Repositories;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;


public class FilmDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private int PAGE_SIZE;
    private static final int FIRST_PAGE = 1;
    private String genreID;
    private FilmRepository filmRepository = new FilmRepository();
    private Thread requestDelay = new Thread();

    public FilmDataSource(int pageSize, String genreID) {
        this.PAGE_SIZE = pageSize;
        this.genreID = genreID;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, FilmResponse> callback) {
        filmRepository.getFilmsResultsLoadInitial(callback,String.valueOf(FIRST_PAGE),genreID);
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadBefore(params,callback,genreID);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsloadAfter(PAGE_SIZE,params,callback,genreID);
    }
}
