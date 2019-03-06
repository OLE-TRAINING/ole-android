package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FragmentStateAdapter;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class HomeFragment extends Fragment {

    private HomeFragmentViewHolder viewHolder;
    private FilmGenres genre;
    private HomeFragmentViewModel viewModelHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_home,container, false);
        this.viewHolder = new HomeFragmentViewHolder(view);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        setupObservers();

        viewModelHome.executeServiceGetGenreList();

        return view;
    }

    private void setupObservers(){
        viewModelHome.getThereIsAGenreList().observe(this,genresObserver);
    }

    private Observer<FilmGenres> genresObserver = new Observer<FilmGenres>() {
        @Override
        public void onChanged(@Nullable FilmGenres filmGenres) {
            genre = filmGenres;

            viewHolder.viewPager.setAdapter(new FragmentStateAdapter(getFragmentManager(),
                    genre));

            viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager);
        }
    };

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
