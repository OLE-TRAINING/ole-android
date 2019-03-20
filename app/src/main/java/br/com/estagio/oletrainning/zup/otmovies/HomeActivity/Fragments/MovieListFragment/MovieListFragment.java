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
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.GenreAndPageSize;
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

        setupAdapter();

        movieListFragmentViewModel.executeServiceGetFilmResults("1");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupObservers();
    }

    private void setupAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieListFragmentViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        movieListFragmentViewHolder.recyclerView.setHasFixedSize(true);

    }

    private void setupObservers() {
        movieListFragmentViewModel.getIsSessionExpired().observe(this, sessionObserver);
        movieListFragmentViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListFragmentViewModel.getThereIsAPageSizeAndGenreID().observe(this,pageSizeAndGenreIdsObserver);
    }

    private Observer<PagedList<FilmResponse>>filmPagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            if(filmResponses !=null){
                adapter.submitList(filmResponses);
                movieListFragmentViewHolder.recyclerView.setAdapter(adapter);
            }
        }
    };

    private Observer<GenreAndPageSize> pageSizeAndGenreIdsObserver = new Observer<GenreAndPageSize>() {
        @Override
        public void onChanged(GenreAndPageSize genreAndPageSize) {
            if(genreAndPageSize !=null){
                movieListFragmentViewModel.getPageSizeAndGenre().setValue(genreAndPageSize);
                movieListFragmentViewModel.getItemPagedList().observe(getViewLifecycleOwner(),filmPagedListObserver);
            } else {
                showError();
            }
        }
    };

    private void showError() {
        Toast.makeText(getContext(), "Erro ao obter lista de filmes", Toast.LENGTH_SHORT).show();
    }

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