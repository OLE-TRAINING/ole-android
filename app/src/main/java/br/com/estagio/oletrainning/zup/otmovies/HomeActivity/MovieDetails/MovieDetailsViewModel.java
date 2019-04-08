package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails;

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
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FavoriteListRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class MovieDetailsViewModel extends CommonViewModel {

    private FilmRepository filmRepository = new FilmRepository();
    private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();

    private LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails;

    private LiveData<ResponseModel<Void>> addFavoriteFilm;

    private LiveData<ResponseModel<Void>> removeFavoriteFilm;

    private MutableLiveData<MovieDetailsModel> thereIsMovieDetails = new MutableLiveData<>();

    private String SUCCESS_MESSAGE_ADD = "Filme adicionado aos favoritos com sucesso";
    private String SUCCESS_MESSAGE_DELETE = "Filme removido dos favoritos com sucesso";
    private String SERVICE_OR_CONNECTION_ERROR_ADD = "Falha ao adicionar aos favoritos. Verifique a conexão e tente novamente.";
    private String SERVICE_OR_CONNECTION_ERROR_DELETE = "Falha ao remover dos favoritos. Verifique a conexão e tente novamente.";
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber detalhes do filme. Verifique a conexão e tente novamente.";
    private String FILTER_SIMILARITY = "similarity";

    private MutableLiveData<Boolean> activityTellerIsSessionExpired = new MutableLiveData<>();

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private MutableLiveData<Integer> receiverPageSizeService = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> activityTellerThereIsFilmResults = new MutableLiveData<>();
    private LiveData<ResponseModel<FilmsResults>> filmsResults;

    public LiveData<ResponseModel<Void>> getAddFavoriteFilm() {
        return addFavoriteFilm;
    }

    public LiveData<ResponseModel<Void>> getRemoveFavoriteFilm() {
        return removeFavoriteFilm;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getActivityTellerThereIsFilmResults() {
        return activityTellerThereIsFilmResults;
    }

    public MutableLiveData<Boolean> getActivityTellerIsSessionExpired() {
        return activityTellerIsSessionExpired;
    }

    public MutableLiveData<MovieDetailsModel> getThereIsMovieDetails() {
        return thereIsMovieDetails;
    }

    public void executeServicegetMovieDetails(int id) {
        isLoading.setValue(true);
        getMovieDetails = filmRepository.getMovieDetails(id);
        getMovieDetails.observeForever(getMovieDetailsObserver);
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

    private Observer<ResponseModel<MovieDetailsModel>> getMovieDetailsObserver = new Observer<ResponseModel<MovieDetailsModel>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<MovieDetailsModel> movieDetails) {
            isLoading.setValue(false);
            if (movieDetails != null) {
                if (movieDetails.getCode() == SUCCESS_CODE) {
                    thereIsMovieDetails.setValue(movieDetails.getResponse());
                } else if (movieDetails.getCode() == SESSION_EXPIRED_CODE) {
                    activityTellerIsSessionExpired.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private Observer<Integer> receiverPageSizeServiceObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer pageSize) {
            FilmDataSourceFactory itemDataSourceFactory =
                    new FilmDataSourceFactory(pageSize,
                            String.valueOf(SingletonFilmID.INSTANCE.getID()),FILTER_SIMILARITY);
            liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
            if(pageSize <= 0){
                pageSize=1;
            }
            PagedList.Config config =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(5)
                            .setPrefetchDistance(5)
                            .setPageSize(pageSize)
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
                    receiverPageSizeService.setValue(responseModel.getResponse().getTotal_results());
                    activityTellerThereIsFilmResults.setValue(responseModel.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever(){
        filmRepository.getViewModelTellerIsSessionExpiredPagination().observeForever(isSessionExpiredPaginationObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverPageSizeService.observeForever(receiverPageSizeServiceObserver);
    }

    public void executeServiceGetFilmResults(String page, Integer filmID) {
        isLoading.setValue(true);
        setupObserversForever();

            filmsResults = filmRepository.getFilmsResults(page, String.valueOf(filmID),FILTER_SIMILARITY);

        filmsResults.observeForever(filmsResultsObserver);
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
                activityTellerIsSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                &&  receiverPageSizeService != null
                && filmRepository.getViewModelTellerIsSessionExpiredPagination() != null
        && addFavoriteFilm != null && removeFavoriteFilm != null)  {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverPageSizeService.removeObserver(receiverPageSizeServiceObserver);
            filmRepository.getViewModelTellerIsSessionExpiredPagination().removeObserver(isSessionExpiredPaginationObserver);
            addFavoriteFilm.removeObserver(addFavoriteFilmObserver);
            removeFavoriteFilm.removeObserver(addFavoriteFilmObserver);
        }
    }
}
