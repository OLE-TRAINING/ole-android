package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home.HomeFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragment;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment myFragment = new FavoriteFragment();
        return myFragment;
    }
}
