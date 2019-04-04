package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragment;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class MovieDetailsActivity extends CommonActivity {

    private MovieDetailsViewHolder movieDetailsViewHolder;
    private MovieDetailsViewModel movieDetailsViewModel;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_movie_details, null);
        this.movieDetailsViewHolder = new MovieDetailsViewHolder(view);

        setContentView(view);

        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        SpannableString spannableString = new SpannableString("OT" + "MOVIES");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        movieDetailsViewHolder.textViewToobar.setText(spannableString);

        setupListeners();

        setupObservers();

        linearLayoutManager = new LinearLayoutManager(this);

        if (adapter == null) {
            adapter = new FilmAdapter(this);
        }
        setupLayoutManager();


        if(SingletonFilmID.INSTANCE.getID() != null){
            Integer filmID = SingletonFilmID.INSTANCE.getID();
            movieDetailsViewModel.executeServicegetMovieDetails(filmID);
            movieDetailsViewModel.executeServiceGetFilmResults("1",filmID);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(), R.color.colorPrimary, false);
    }

    private void setupLayoutManager() {
        movieDetailsViewHolder.recyclerViewDetails.setLayoutManager(linearLayoutManager);
        movieDetailsViewHolder.recyclerViewDetails.setHasFixedSize(true);
    }

    private void setupListeners(){
        movieDetailsViewHolder.textViewToobar.setOnClickListener(backArrowListener);
        movieDetailsViewHolder.backArrow.setOnClickListener(backArrowListener);
    }

    private void setupObservers(){
        movieDetailsViewModel.getIsLoading().observe(this,progressBarObserver);
        movieDetailsViewModel.getThereIsMovieDetails().observe(this,thereIsMovieDetailsObserver);
        movieDetailsViewModel.getActivityTellerIsSessionExpired().observe(this,sessionObserver);
        movieDetailsViewModel.getActivityTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieDetailsViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
    }

    private View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(MovieDetailsActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            movieDetailsViewModel.getItemPagedList().observe(MovieDetailsActivity.this, pagedListObserver);
            movieDetailsViewHolder.recyclerViewDetails.setAdapter(adapter);
            adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    Log.d("position",String.valueOf(position));
                    if (filmsResults != null) {
                        movieDetailsViewModel.getIsLoading().setValue(true);
                        SingletonFilmID.setIDEntered(currentList.get(position).getId());
                        if(SingletonFilmID.INSTANCE.getID() != null){
                            startActivity(new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class));
                        }
                        movieDetailsViewModel.getIsLoading().setValue(false);
                    }
                }
            });
            movieDetailsViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieDetailsViewHolder.progressBarFragment,
                    movieDetailsViewHolder.frameLayout);
        }
    };

    private Observer<MovieDetailsModel> thereIsMovieDetailsObserver = new Observer<MovieDetailsModel>() {
        @Override
        public void onChanged(MovieDetailsModel movieDetailsModel) {
            setMovieDetailsInformations(movieDetailsModel);
            movieDetailsViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() == null){
                SingletonAlertDialogSession.createAlertDialogBuilder(MovieDetailsActivity.this);
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().create().setCanceledOnTouchOutside(false);
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().show();
            }
        }
    };

    private void setMovieDetailsInformations(MovieDetailsModel movieDetailsModel){
        movieDetailsViewHolder.textViewOverview.setText(movieDetailsModel.getOverview());
        movieDetailsViewHolder.textViewWriter.setText(sentenceBuilder(movieDetailsModel.getWriters()));
        movieDetailsViewHolder.textViewDirector.setText(sentenceBuilder(movieDetailsModel.getDirectors()));
        movieDetailsViewHolder.textViewKeywords.setText(sentenceBuilder(movieDetailsModel.getGenreNames()));
        movieDetailsViewHolder.textViewPoints.setText(String.valueOf(movieDetailsModel.getVoteAverage()));
        movieDetailsViewHolder.textViewRuntime.setText(movieDetailsModel.getRuntime());
        movieDetailsViewHolder.textViewCountries.setText(sentenceBuilder(movieDetailsModel.getCountries()));
        movieDetailsViewHolder.textViewYear.setText(String.valueOf(movieDetailsModel.getYear()));
        movieDetailsViewHolder.textViewTitle.setText(movieDetailsModel.getTitle());
        Picasso.get()
                .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+movieDetailsModel.getPosterId()
                        +"/image/w342?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                .into(movieDetailsViewHolder.imageViewPoster);
        Picasso.get()
                .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+movieDetailsModel.getBannerId()
                        +"/image/w1280?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                .into(movieDetailsViewHolder.imageViewBanner);
    }

    private String sentenceBuilder(@NonNull List<String> listString) {
        StringBuilder keywordList = new StringBuilder();
        for (int i = 0; i < listString.size(); i++) {
            keywordList.append(listString.get(i));
            if(i<listString.size()-1){
                keywordList.append(", ");
            }
        }
        Log.d("KEYWORDS",keywordList.toString());
        return keywordList.toString();
    }

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout) {
        if (isLoading != null) {
            if (isLoading) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }
}
