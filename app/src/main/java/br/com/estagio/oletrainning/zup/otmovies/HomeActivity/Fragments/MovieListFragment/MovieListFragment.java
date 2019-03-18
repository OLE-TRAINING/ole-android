package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.annotation.SuppressLint;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmViewModel;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.ListFilmsAdapter;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

@SuppressLint("ValidFragment")
public class MovieListFragment extends CommonFragment {

    private String genreID;
    private ListFilmsAdapter listFilmsAdapter;
    private MovieListFragmentViewModel movieListFragmentViewModel;
    private FilmGenres filmGenres;
    private MovieListFragmentViewHolder movieListFragmentViewHolder;
    FilmViewModel filmViewModel;

    @SuppressLint("ValidFragment")
    public MovieListFragment(String genreID, FilmGenres filmGenres) {
        this.filmGenres = filmGenres;
        this.genreID = genreID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_movie_list, container, false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        filmViewModel = ViewModelProviders.of(this).get(FilmViewModel.class);

        setsUpAdapter();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setupObservers();
    }

    private void setupObservers() {
        movieListFragmentViewModel.getThereIsAMovieGenre().observe(this, filmGenreObserver);
        movieListFragmentViewModel.getIsSessionExpired().observe(this, sessionObserver);
        movieListFragmentViewModel.getIsLoading().observe(this, progressBarObserver);
    }

    private void setsUpAdapter() {
        RecyclerView recyclerView = movieListFragmentViewHolder.recyclerView;

        final FilmAdapter adapter = new FilmAdapter(getContext());

        recyclerView.setAdapter(adapter);
    }

    private Observer<List<Film>> filmGenreObserver = new Observer<List<Film>>() {
        @Override
        public void onChanged(@Nullable List<Film> films) {
            if (films != null) {
                listFilmsAdapter.setFilms(films);
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
