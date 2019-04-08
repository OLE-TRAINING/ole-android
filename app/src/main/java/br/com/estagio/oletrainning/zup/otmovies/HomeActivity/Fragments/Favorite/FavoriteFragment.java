package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home.HomeFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragmentViewHolder;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragmentViewModel;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;

public class FavoriteFragment extends CommonFragment {

    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private FavoriteFragmentViewModel favoriteFragmentViewModel;
    private MovieListFragmentViewHolder movieListFragmentViewHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        favoriteFragmentViewModel = ViewModelProviders.of(FavoriteFragment.this).get(FavoriteFragmentViewModel.class);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        favoriteFragmentViewModel.getFragmentTellerIsSessionExpired().observe(this, sessionObserver);

        return view;
    }


    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if (SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() == null) {
                SingletonAlertDialogSession.createAlertDialogBuilder(getActivity());
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().create().setCanceledOnTouchOutside(false);
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().show();
            }
        }
    };

    public static FavoriteFragment newInstance() {
        FavoriteFragment myFragment = new FavoriteFragment();
        return myFragment;
    }
}
