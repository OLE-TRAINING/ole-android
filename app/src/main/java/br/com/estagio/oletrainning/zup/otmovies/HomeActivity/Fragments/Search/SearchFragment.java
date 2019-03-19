package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite.FavoriteFragment;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public static SearchFragment newInstance() {
        SearchFragment myFragment = new SearchFragment();
        return myFragment;
    }
}
