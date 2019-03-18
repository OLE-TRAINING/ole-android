package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmDataSource;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class FilmDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        FilmDataSource filmDataSource = new FilmDataSource();
        itemLiveDataSource.postValue(filmDataSource);
        return filmDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}