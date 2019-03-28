package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieDetailsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragmentViewHolder;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class MovieDetails extends Fragment {

    private MovieListFragmentViewHolder movieListFragmentViewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_movie_details, container, false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        return view;
    }

    public static MovieDetails newInstance() {
        MovieDetails fragment = new MovieDetails();
        return fragment;
    }
}
