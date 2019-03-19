package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.GenreAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class FilmViewModel extends ViewModel {

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private MutableLiveData<GenreAndPageSize> pageSizeAndGenre = new MutableLiveData<>();
    private MutableLiveData<GenreAndPageSize> thereIsAPageSizeAndGenreID = new MutableLiveData<>();

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<GenreAndPageSize> getThereIsAPageSizeAndGenreID() {
        return thereIsAPageSizeAndGenreID;
    }
    public MutableLiveData<GenreAndPageSize> getPageSizeAndGenre() {
        return pageSizeAndGenre;
    }

    public LiveData<PageKeyedDataSource<Integer, FilmResponse>> getLiveDataSource() {
        return liveDataSource;
    }

    public void startPageSizeObserverViewModel() {
        getThereIsAPageSizeAndGenreID().observeForever(pageSizeAndGenreChangeObserver);
    }

    private Observer<GenreAndPageSize> pageSizeAndGenreChangeObserver = new Observer<GenreAndPageSize>() {
        @Override
        public void onChanged(GenreAndPageSize genreAndPageSize) {
            thereIsAPageSizeAndGenreID.setValue(genreAndPageSize);
            FilmDataSourceFactory itemDataSourceFactory =
                    new FilmDataSourceFactory(genreAndPageSize.getPageSize(),
                            genreAndPageSize.getGenreID());
            liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
            PagedList.Config config =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setPageSize(genreAndPageSize.getPageSize())
                            .build();

            itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
        }
    };

    public void removeObserver() {
        if (getThereIsAPageSizeAndGenreID() != null) {
            getThereIsAPageSizeAndGenreID().removeObserver(pageSizeAndGenreChangeObserver);
        }
    }
}
