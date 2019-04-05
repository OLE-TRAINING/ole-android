package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class MovieDetailsViewHolder {

    RecyclerView recyclerViewDetails;
    ProgressBar progressBarFragment;
    FrameLayout frameLayout;
    TextView textViewToobar;
    ImageView backArrow;

    public MovieDetailsViewHolder(View view) {
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerViewDetails = view.findViewById(R.id.recycler_films);
        progressBarFragment = view.findViewById(R.id.progress_bar);
        textViewToobar = view.findViewById(R.id.textview_toobar_details);
        backArrow = view.findViewById(R.id.imageView_iconBackArrow);
    }
}
