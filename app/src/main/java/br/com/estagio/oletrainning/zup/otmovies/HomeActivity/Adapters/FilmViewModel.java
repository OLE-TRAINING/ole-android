package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import br.com.estagio.oletrainning.zup.otmovies.Services.Model.GenreAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.Services.Remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmViewModel extends ViewModel {

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private MutableLiveData<GenreAndPageSize> pageSizeAndGenre = new MutableLiveData<>();
    private MutableLiveData<GenreAndPageSize> thereIsAPageSizeAndGenreID = new MutableLiveData<>();

    public MutableLiveData<GenreAndPageSize> getThereIsAPageSizeAndGenreID() {
        return thereIsAPageSizeAndGenreID;
    }

    public MutableLiveData<GenreAndPageSize> getPageSizeAndGenre() {
        return pageSizeAndGenre;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public void startPageSizeObserverViewModel() {
        getPageSizeAndGenre().observeForever(pageSizeAndGenreChangeObserver);
    }


    private Observer<GenreAndPageSize> pageSizeAndGenreChangeObserver = new Observer<GenreAndPageSize>() {
        @Override
        public void onChanged(GenreAndPageSize genreAndPageSize) {
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

    public void executeGetPageSize(){
               RetrofitServiceBuilder.getInstance().getApi().getMovieGenre("genres", SingletonGenreID.INSTANCE.getGenreID(), "20", "1")
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.body() != null) {
                            GenreAndPageSize genreAndPageSize = new GenreAndPageSize(response.body().getTotal_pages(),SingletonGenreID.INSTANCE.getGenreID());
                            thereIsAPageSizeAndGenreID.setValue(genreAndPageSize);
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        thereIsAPageSizeAndGenreID.setValue(null);
                    }
                });
    }


    public void removeObserver() {
        if (getPageSizeAndGenre() != null) {
            getPageSizeAndGenre().removeObserver(pageSizeAndGenreChangeObserver);
        }
    }
}
