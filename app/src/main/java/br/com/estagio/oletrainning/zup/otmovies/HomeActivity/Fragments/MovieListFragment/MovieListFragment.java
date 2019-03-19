package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmViewModel;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.GenreAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;


public class MovieListFragment extends CommonFragment {

    private FilmViewModel filmViewModel;
    private MovieListFragmentViewModel movieListFragmentViewModel;
    private MovieListFragmentViewHolder movieListFragmentViewHolder;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container,false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        filmViewModel = ViewModelProviders.of(MovieListFragment.this).get(FilmViewModel.class);

        movieListFragmentViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieListFragmentViewHolder.recyclerView.setHasFixedSize(true);


        movieListFragmentViewModel.executeServiceGetFilmResults(getString(R.string.first_page),SingletonGenreID.INSTANCE.getGenreID());

        setupObservers();

        return view;
    }

    private void setupObservers(){
        movieListFragmentViewModel.getThereIsAPageSize().observe(this,pageSizeObeserver);
        movieListFragmentViewModel.getIsMessageErrorToast().observe(this,isMessageErrorToastObserver);
        movieListFragmentViewModel.getIsLoading().observe(this,progressBarObserver);
    }

    private Observer<Integer> pageSizeObeserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer pageSize) {

            filmViewModel = ViewModelProviders.of(MovieListFragment.this).get(FilmViewModel.class);
            final FilmAdapter adapter = new FilmAdapter(getActivity());

            filmViewModel.startPageSizeObserverViewModel();

            filmViewModel.getThereIsAPageSizeAndGenreID().setValue(new GenreAndPageSize(pageSize,
                    SingletonGenreID.INSTANCE.getGenreID()));

            filmViewModel.getItemPagedList().observe(MovieListFragment.this, new Observer<PagedList<FilmResponse>>() {
                @Override
                public void onChanged(@Nullable PagedList<FilmResponse> items) {
                    adapter.submitList(items);
                }
            });

            movieListFragmentViewHolder.recyclerView.setAdapter(adapter);
        }
    };

    private Observer<String> isMessageErrorToastObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    };


    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieListFragmentViewHolder.progressBar,
                    movieListFragmentViewHolder.frameLayout);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        filmViewModel.removeObserver();
        movieListFragmentViewModel.removeObserver();
    }
}
