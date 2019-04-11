package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonViewModel;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmDataSourceFactory;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class FavoriteFragmentViewModel extends CommonViewModel {

    FilmRepository filmRepository = new FilmRepository();
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    public void executeServiceGetFilmResults(String page, String filterID, String filter) {
        isLoading.setValue(true);
        if(SingletonGenreID.INSTANCE.getGenreID() != null){
            filmsResults = filmRepository.getFilmsResults(page,filterID,filter);
            filmsResults.observeForever(filmsResultsObserver);
        }
    }

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    public void itemConfig(Integer pageSize, String ID, String filter){
        FilmDataSourceFactory itemDataSourceFactory =
                new FilmDataSourceFactory(pageSize,ID,filter);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(20)
                        .setPrefetchDistance(5)
                        .setPageSize(pageSize)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null)  {
            filmsResults.removeObserver(filmsResultsObserver);
        }
    }
}
