package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import android.util.Log;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmDataSourceFactory;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.GenreAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmDataSource;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class MovieListFragmentViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();
    private FilmDataSource filmDataSource = new FilmDataSource();

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<GenreAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private MutableLiveData<Boolean> homeTellerThereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<String> isMessageErrorToast = new MutableLiveData<>();

    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";


    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<Boolean> getHomeTellerThereIsFilmResults() {
        return homeTellerThereIsFilmResults;
    }

    private Observer<GenreAndPageSize> receiverAPageSizeAndGenreIDServiceObserver = new Observer<GenreAndPageSize>() {
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

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == 200) {
                    GenreAndPageSize genreAndPageSize = new GenreAndPageSize(responseModel.getResponse().getTotal_pages(),
                            SingletonGenreID.INSTANCE.getGenreID());
                    receiverAPageSizeAndGenreIDService.setValue(genreAndPageSize);
                    homeTellerThereIsFilmResults.setValue(true);
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
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        filmDataSource.getIsLoadingPaginationControl().observeForever(isLoadingPaginationControlObserver);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
        Log.d("singletonGenreID",SingletonGenreID.INSTANCE.getGenreID());
        filmsResults = filmRepository.getFilmsResults(page, SingletonGenreID.INSTANCE.getGenreID());
        filmsResults.observeForever(filmsResultsObserver);

    }

    private Observer<Boolean> isLoadingPaginationControlObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoadingPagination) {
            if(isLoadingPagination){
                isLoading.setValue(true);
            } else {
                isLoading.setValue(false);
            }
        }
    };

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
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                &&  receiverAPageSizeAndGenreIDService != null
        && filmDataSource.getIsLoadingPaginationControl() != null)  {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
            filmDataSource.getIsLoadingPaginationControl().removeObserver(isLoadingPaginationControlObserver);
        }
    }

}
