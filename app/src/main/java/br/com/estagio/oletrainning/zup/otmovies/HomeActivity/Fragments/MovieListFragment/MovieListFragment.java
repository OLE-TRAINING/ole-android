package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class MovieListFragment extends CommonFragment {


    private MovieListFragmentViewModel movieListFragmentViewModel;
    private MovieListFragmentViewHolder movieListFragmentViewHolder;
    private FilmAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container,false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        movieListFragmentViewModel = ViewModelProviders.of(MovieListFragment.this).get(MovieListFragmentViewModel.class);

        adapter = new FilmAdapter(getActivity());

        setupLayoutManager();

        movieListFragmentViewModel.executeServiceGetFilmResults("1");

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupObservers();
    }

    private void setupLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        movieListFragmentViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        movieListFragmentViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObservers() {
        movieListFragmentViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListFragmentViewModel.getHomeTellerThereIsFilmResults().observe(this,homeTellerThereIsFilmResultsObserver);
        movieListFragmentViewModel.getIsErrorMessageForToast().observe(this, isMessageForToastObserver);

    }

    private Observer <Boolean> homeTellerThereIsFilmResultsObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            movieListFragmentViewModel.getItemPagedList().observe(MovieListFragment.this, new Observer<PagedList<FilmResponse>>() {
                @Override
                public void onChanged(@Nullable PagedList<FilmResponse> items) {
                    adapter.submitList(items);

                }
            });

            movieListFragmentViewHolder.recyclerView.setAdapter(adapter);
        }
    };

    Observer<String> isMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
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
        movieListFragmentViewModel.removeObserver();
    }
}