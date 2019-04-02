package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieDetailsFragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;

@SuppressLint("ValidFragment")
public class MovieDetails extends CommonFragment {

    private MovieDetailsViewHolder movieDetailsViewHolder;
    private MovieDetailsViewModel movieDetailsViewModel;

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_movie_details, container, false);
        this.movieDetailsViewHolder = new MovieDetailsViewHolder(view);

        movieDetailsViewModel = ViewModelProviders.of(MovieDetails.this).get(MovieDetailsViewModel.class);

        setupObservers();

        movieDetailsViewModel.executeServicegetMovieDetails(id);

        return view;
    }

    private void setupObservers(){
        movieDetailsViewModel.getIsLoading().observe(this,progressBarObserver);
        movieDetailsViewModel.getThereIsMovieDetails().observe(this,thereIsMovieDetailsObserver);
        movieDetailsViewModel.getFragmentTellerIsSessionExpired().observe(this,sessionObserver);
    }

    public MovieDetails(int id) {
        this.id = id;
    }

    public MovieDetails() {
    }

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
            movieDetailsViewModel.getIsLoading().setValue(false);
            setMovieDetailsInformations(movieDetailsModel);
        }
    };

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() == null){
                SingletonAlertDialogSession.createAlertDialogBuilder(getActivity());
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
                        +"/image/w780?gw-app-key=593c3280aedd01364c73000d3ac06d76")
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

    public static MovieDetails newInstance(int id) {
        MovieDetails fragment = new MovieDetails(id);
        return fragment;
    }


}
