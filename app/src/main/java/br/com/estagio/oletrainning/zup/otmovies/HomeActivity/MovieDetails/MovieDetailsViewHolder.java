package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class MovieDetailsViewHolder {

    TextView textViewTitle;
    TextView textViewKeywords;
    TextView textViewYear;
    TextView textViewCountries;
    TextView textViewRuntime;
    TextView textViewPoints;
    TextView textViewDirector;
    TextView textViewWriter;
    TextView textViewOverview;
    RecyclerView recyclerViewDetails;
    ProgressBar progressBarFragment;
    ImageView imageViewBanner;
    ImageView imageViewPoster;
    FrameLayout frameLayout;
    ProgressBar progressBarRecycler;
    TextView textViewToobar;
    ImageView backArrow;

    public MovieDetailsViewHolder(View view) {
        frameLayout = view.findViewById(R.id.loading_layout);
        textViewTitle = view.findViewById(R.id.text_title_details);
        textViewKeywords = view.findViewById(R.id.textView_keywords_details);
        textViewYear = view.findViewById(R.id.textView_year_details);
        textViewCountries = view.findViewById(R.id.textView_countries_details);
        textViewRuntime = view.findViewById(R.id.textView_runtime_details);
        textViewPoints = view.findViewById(R.id.textView_points_details);
        textViewDirector = view.findViewById(R.id.textView_diretor_details);
        textViewWriter = view.findViewById(R.id.textView_writer_details);
        textViewOverview = view.findViewById(R.id.textView_overview_details);
        recyclerViewDetails = view.findViewById(R.id.recycler_films);
        progressBarFragment = view.findViewById(R.id.progress_bar_fragment);
        imageViewBanner = view.findViewById(R.id.imageView_banner_details);
        imageViewPoster = view.findViewById(R.id.imageView_poster_details);
        progressBarRecycler = view.findViewById(R.id.progress_bar_recycler);
        textViewToobar = view.findViewById(R.id.textview_toobar_details);
        backArrow = view.findViewById(R.id.imageView_iconBackArrow);
    }
}
