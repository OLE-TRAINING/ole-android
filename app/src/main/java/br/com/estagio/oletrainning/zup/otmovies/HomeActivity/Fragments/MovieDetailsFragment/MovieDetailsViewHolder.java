package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieDetailsFragment;

import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class MovieDetailsViewHolder {

    CollapsingToolbarLayout collapsingToolbar;

    public MovieDetailsViewHolder(View view) {
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
    }
}
