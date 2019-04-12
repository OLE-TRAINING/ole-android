package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class SearchViewHolder {

    ProgressBar progressBar;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    SearchView searchView;
    ViewGroup movieListLayoutOnSearch;


    public SearchViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
        searchView = view.findViewById(R.id.search_view);
        movieListLayoutOnSearch = view.findViewById(R.id.fragment_movie_list_search);
    }
}
