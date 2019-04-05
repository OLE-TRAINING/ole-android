package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapterDetailsList;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.HomeActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class MovieDetailsActivity extends CommonActivity {

    private MovieDetailsViewHolder movieDetailsViewHolder;
    private MovieDetailsViewModel movieDetailsViewModel;
    private FilmAdapterDetailsList adapter;
    private LinearLayoutManager linearLayoutManager;
    private MovieDetailsModel mMovieDetailsModel;

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


        setupLayoutManager();

        if(SingletonFilmID.INSTANCE.getID() != null){
            Integer filmID = SingletonFilmID.INSTANCE.getID();
            movieDetailsViewModel.executeServicegetMovieDetails(filmID);
            movieDetailsViewModel.executeServiceGetFilmResults("1",filmID);
        } else {
            Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
        movieDetailsViewHolder.textViewToobar.setOnClickListener(titleToobarOnClickListener);
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
            SingletonFilmID.setIDEntered(null);
        }
    };

    private View.OnClickListener titleToobarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            SingletonFilmID.setIDEntered(null);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonFilmID.setIDEntered(null);
    }

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            if (adapter == null) {
                if(mMovieDetailsModel != null){
                    adapter = new FilmAdapterDetailsList(MovieDetailsActivity.this,mMovieDetailsModel);
                    adapter.submitList(filmResponses);
                } else {
                    if(SingletonFilmID.INSTANCE.getID() != null){
                        Integer filmID = SingletonFilmID.INSTANCE.getID();
                        movieDetailsViewModel.executeServicegetMovieDetails(filmID);
                        movieDetailsViewModel.executeServiceGetFilmResults("1",filmID);
                    } else {
                        Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            } else {
                adapter.submitList(filmResponses);
            }
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
            mMovieDetailsModel = movieDetailsModel;
            if (adapter == null) {
                adapter = new FilmAdapterDetailsList(MovieDetailsActivity.this,mMovieDetailsModel);
            }
            movieDetailsViewHolder.recyclerViewDetails.setAdapter(adapter);
            adapter.setOnItemClickListener(new FilmAdapterDetailsList.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    movieDetailsViewModel.getIsLoading().setValue(true);
                        SingletonFilmID.setIDEntered(currentList.get(position).getId());
                        if(SingletonFilmID.INSTANCE.getID() != null){
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                            startActivity(intent);
                        movieDetailsViewModel.getIsLoading().setValue(false);
                    }
                }
            });
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
