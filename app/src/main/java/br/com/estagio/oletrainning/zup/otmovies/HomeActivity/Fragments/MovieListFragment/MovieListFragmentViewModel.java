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
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.FilterIDAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FavoriteListRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class MovieListFragmentViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();
    private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    private String SUCCESS_MESSAGE_ADD = "Filme adicionado aos favoritos com sucesso";
    private String SUCCESS_MESSAGE_DELETE = "Filme removido dos favoritos com sucesso";
    private String SERVICE_OR_CONNECTION_ERROR_ADD = "Falha ao adicionar aos favoritos. Verifique a conexão e tente novamente.";
    private String SERVICE_OR_CONNECTION_ERROR_DELETE = "Falha ao remover dos favoritos. Verifique a conexão e tente novamente.";
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    private String FILTER_GENRES = "genres";

    private LiveData<ResponseModel<Void>> addFavoriteFilm;

    private LiveData<ResponseModel<Void>> removeFavoriteFilm;


    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<FilterIDAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();
    private MutableLiveData<Boolean> fragmentTellerIsLoadingPagination = new MutableLiveData<>();

    public LiveData<ResponseModel<Void>> getAddFavoriteFilm() {
        return addFavoriteFilm;
    }

    public LiveData<ResponseModel<Void>> getRemoveFavoriteFilm() {
        return removeFavoriteFilm;
    }

    public MutableLiveData<Boolean> getFragmentTellerIsLoadingPagination() {
        return fragmentTellerIsLoadingPagination;
    }

    public MutableLiveData<Boolean> getFragmentTellerIsSessionExpired() {
        return fragmentTellerIsSessionExpired;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    public void executeAddFavoriteFilm(String email, String movieID) {
        addFavoriteFilm = favoriteListRepository.addFavotiteFilm(email,movieID);
        addFavoriteFilm.observeForever(addFavoriteFilmObserver);
    }

    private Observer<ResponseModel<Void>> addFavoriteFilmObserver = new Observer<ResponseModel<Void>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<Void> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    isMessageSuccessForToast.setValue(SUCCESS_MESSAGE_ADD);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_ADD);
            }
        }
    };

    public void executeRemoveFavoriteFilm(String email, String movieID) {
        addFavoriteFilm = favoriteListRepository.removeFavotiteFilm(email,movieID);
        addFavoriteFilm.observeForever(removeFavoriteFilmObserver);
    }

    private Observer<ResponseModel<Void>> removeFavoriteFilmObserver = new Observer<ResponseModel<Void>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<Void> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    isMessageSuccessForToast.setValue(SUCCESS_MESSAGE_DELETE);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_DELETE);
            }
        }
    };

    private Observer<FilterIDAndPageSize> receiverAPageSizeAndGenreIDServiceObserver = new Observer<FilterIDAndPageSize>() {
        @Override
        public void onChanged(FilterIDAndPageSize filterIDAndPageSize) {
            FilmDataSourceFactory itemDataSourceFactory =
                    new FilmDataSourceFactory(filterIDAndPageSize.getPageSize(),
                            filterIDAndPageSize.getFilterID(),FILTER_GENRES);
            liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
            PagedList.Config config =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(20)
                            .setPrefetchDistance(5)
                            .setPageSize(filterIDAndPageSize.getPageSize())
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
                    FilterIDAndPageSize filterIDAndPageSize = new FilterIDAndPageSize(responseModel.getResponse().getTotal_pages(),
                            SingletonGenreID.INSTANCE.getGenreID());
                    receiverAPageSizeAndGenreIDService.setValue(filterIDAndPageSize);
                    fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever(){
        filmRepository.getViewModelTellerIsSessionExpiredPagination().observeForever(isSessionExpiredPaginationObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
    }

    public void executeServiceGetFilmResults(String page) {
        isLoading.setValue(true);
        setupObserversForever();
        if(SingletonGenreID.INSTANCE.getGenreID() != null){
            filmsResults = filmRepository.getFilmsResults(page, SingletonGenreID.INSTANCE.getGenreID(),FILTER_GENRES);
            filmsResults.observeForever(filmsResultsObserver);
        }
    }

    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if(errorMessage != null){
                isErrorMessageForToast.setValue(errorMessage.getMessage());
            }
        }
    };

    private Observer<Boolean> isSessionExpiredPaginationObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(isSessionExpired){
                fragmentTellerIsSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                &&  receiverAPageSizeAndGenreIDService != null
        && filmRepository.getViewModelTellerIsSessionExpiredPagination() != null
        && addFavoriteFilm != null && removeFavoriteFilm != null)  {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
            filmRepository.getViewModelTellerIsSessionExpiredPagination().removeObserver(isSessionExpiredPaginationObserver);
            addFavoriteFilm.removeObserver(addFavoriteFilmObserver);
            removeFavoriteFilm.removeObserver(addFavoriteFilmObserver);
        }
    }

}
