package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.ListFilmsAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home.HomeFragmentViewModel;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class MovieListFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private String genreID;
    private ListFilmsAdapter listFilmsAdapter;
    private HomeFragmentViewModel viewModelHome;
    private FilmGenres filmGenres;

    public MovieListFragment() {
    }

    @SuppressLint("ValidFragment")
    public MovieListFragment(String genreID, FilmGenres filmGenres) {
        this.filmGenres = filmGenres;
        this.genreID = genreID;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_movie_list, container, false);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        setsUpAdapter();

        setupObservers();

        viewModelHome.executeServiceGetMovieGenre(genreID);

        return v;
    }

    private void setupObservers() {
        viewModelHome.getThereIsAMovieGenre().observe(this, filmGenreObserver);
    }

    private void setsUpAdapter() {
        recyclerView = v.findViewById(R.id.recycler_films);

        listFilmsAdapter = new ListFilmsAdapter(filmGenres);

        recyclerView.setLayoutManager(new LinearLayoutManager(MovieListFragment.this.getContext()));

        recyclerView.setAdapter(listFilmsAdapter);

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
}
