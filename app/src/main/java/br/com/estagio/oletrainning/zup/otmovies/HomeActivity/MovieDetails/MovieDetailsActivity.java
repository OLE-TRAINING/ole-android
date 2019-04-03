package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails;

import android.app.ActionBar;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class MovieDetailsActivity extends CommonActivity {

    private MovieDetailsViewHolder movieDetailsViewHolder;
    private MovieDetailsViewModel movieDetailsViewModel;

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

        if(SingletonFilmID.INSTANCE.getID() != null){
            movieDetailsViewModel.executeServicegetMovieDetails(SingletonFilmID.INSTANCE.getID());
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(), R.color.colorPrimary, false);
    }

    private void setupListeners(){
        movieDetailsViewHolder.textViewToobar.setOnClickListener(backArrowListener);
        movieDetailsViewHolder.backArrow.setOnClickListener(backArrowListener);
    }

    private void setupObservers(){
        movieDetailsViewModel.getIsLoading().observe(this,progressBarObserver);
        movieDetailsViewModel.getThereIsMovieDetails().observe(this,thereIsMovieDetailsObserver);
        movieDetailsViewModel.getFragmentTellerIsSessionExpired().observe(this,sessionObserver);
    }

    private View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
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
                        +"/image/original?gw-app-key=593c3280aedd01364c73000d3ac06d76")
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
