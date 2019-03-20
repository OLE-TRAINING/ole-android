package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmDataSourceFactory;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.GenreAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class MovieListFragmentViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();
    private LiveData<ResponseModel<FilmsResults>> getFilmsResults;
    private MutableLiveData<GenreAndPageSize> pageSizeAndGenre = new MutableLiveData<>();
    private MutableLiveData<Integer> thereIsAPageSize = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> thereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<String> isMessageErrorToast = new MutableLiveData<>();
    private MutableLiveData<GenreAndPageSize> thereIsAPageSizeAndGenreID = new MutableLiveData<>();
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private MutableLiveData<Integer> pageSize = new MutableLiveData<>();

    public MutableLiveData<GenreAndPageSize> getPageSizeAndGenre() {
        return pageSizeAndGenre;
    }

    public void setPageSizeAndGenre(MutableLiveData<GenreAndPageSize> pageSizeAndGenre) {
        this.pageSizeAndGenre = pageSizeAndGenre;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<Integer> getPageSize() {
        return pageSize;
    }

    public MutableLiveData<GenreAndPageSize> getThereIsAPageSizeAndGenreID() {
        return thereIsAPageSizeAndGenreID;
    }

    private Observer<Integer> pageSizeChangeObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer pageSize) {
            FilmDataSourceFactory itemDataSourceFactory = new FilmDataSourceFactory(pageSize,
                    SingletonGenreID.INSTANCE.getGenreID());
            liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
            PagedList.Config config =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setPageSize(pageSize)
                            .build();

            itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
        }
    };


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

    public MutableLiveData<String> getIsMessageErrorToast() {
        return isMessageErrorToast;
    }

    private Observer<ResponseModel<FilmsResults>> getFilmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    thereIsFilmResults.setValue(responseModel.getResponse());
                    thereIsAPageSize.setValue(responseModel.getResponse().getTotal_pages());
                } else {
                    String message = responseModel.getErrorMessage().getMessage();
                    getIsMessageErrorToast().setValue(message);
                }
            } else {
                getIsMessageErrorToast().setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    public void executeServiceGetFilmResults(String page) {
        getFilmsResults = filmRepository.getFilmsResults(page, SingletonGenreID.INSTANCE.getGenreID());
        getFilmsResults.observeForever(getFilmsResultsObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        getThereIsAPageSizeAndGenreID().observeForever(pageSizeAndGenreChangeObserver);
    }
    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if(errorMessage != null){
                getIsMessageErrorToast().setValue(errorMessage.getMessage());
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (getFilmsResults != null && filmRepository.getThereIsPaginationError() != null
                && getPageSize() != null) {
            getFilmsResults.removeObserver(getFilmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            getPageSize().removeObserver(pageSizeChangeObserver);
        }
    }

}
