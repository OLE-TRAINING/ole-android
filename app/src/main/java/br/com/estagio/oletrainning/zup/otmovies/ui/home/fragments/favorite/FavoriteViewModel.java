package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.favorite;

import br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.movieList.MovieListViewModel;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonGenreID;

public class FavoriteViewModel extends MovieListViewModel {

    public void executeServiceGetFilmResults(String page, String filterID, String filter) {
        isLoading.setValue(true);
        setupObserversForever();
        if(SingletonGenreID.INSTANCE.getGenreID() != null){
            filmsResults = filmRepository.getFilmsResults(page,filterID,filter);
            filmsResults.observeForever(filmsResultsObserver);
        }
    }
}
