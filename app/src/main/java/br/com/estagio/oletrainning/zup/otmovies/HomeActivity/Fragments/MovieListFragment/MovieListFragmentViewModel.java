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
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class MovieListFragmentViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();

    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<GenreAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private MutableLiveData<Boolean> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> fragmentTellerSessionExpired = new MutableLiveData<>();

    public MutableLiveData<Boolean> getFragmentTellerSessionExpired() {
        return fragmentTellerSessionExpired;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<Boolean> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
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
                            .setInitialLoadSizeHint(40)
                            .setPrefetchDistance(5)
                            .setPageSize(genreAndPageSize.getPageSize())
                            .build();

            itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();


        }
    };

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    GenreAndPageSize genreAndPageSize = new GenreAndPageSize(responseModel.getResponse().getTotal_pages(),
                            SingletonGenreID.INSTANCE.getGenreID());
                    receiverAPageSizeAndGenreIDService.setValue(genreAndPageSize);
                    fragmentTellerThereIsFilmResults.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever(){
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
        filmRepository.getViewModelTellerSession().observeForever(sessionServicesObserver);
    }

    public void executeServiceGetFilmResults(String page) {
        isLoading.setValue(true);
        setupObserversForever();
        Log.d("singletonGenreID",SingletonGenreID.INSTANCE.getGenreID());
        filmsResults = filmRepository.getFilmsResults(page, SingletonGenreID.INSTANCE.getGenreID());
        filmsResults.observeForever(filmsResultsObserver);
    }

    private Observer<Boolean> sessionServicesObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean isSessionExpired) {
            fragmentTellerSessionExpired.setValue(isSessionExpired);
        }
    };


    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if(errorMessage != null){
                isErrorMessageForToast.setValue(errorMessage.getMessage());
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                &&  receiverAPageSizeAndGenreIDService != null
        && filmRepository.getViewModelTellerSession() != null)  {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
            filmRepository.getViewModelTellerSession().removeObserver(sessionServicesObserver);
        }
    }

}
