package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class MovieListFragmentViewHolder {

    ProgressBar progressBar;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    LinearLayout linearLayoutPagination;

    public MovieListFragmentViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
        linearLayoutPagination = view.findViewById(R.id.linearLayout_pagination);
    }
}
