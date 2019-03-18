package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class FilmViewModel extends ViewModel {

    public LiveData<PagedList<FilmResponse>> filmPagedList;
    LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    Integer PAGE_SIZE;

    public FilmViewModel() {
        this.PAGE_SIZE = 500;
        FilmDataSourceFactory filmDataSourceFactory = new FilmDataSourceFactory();
        liveDataSource = filmDataSourceFactory.getItemLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE)
                        .build();

        filmPagedList = (new LivePagedListBuilder(filmDataSourceFactory, config)).build();
    }
}
