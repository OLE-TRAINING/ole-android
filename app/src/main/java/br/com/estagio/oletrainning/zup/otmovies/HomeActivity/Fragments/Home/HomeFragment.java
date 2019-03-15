package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FragmentStateAdapter;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.GenresResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAccessToken;

public class HomeFragment extends Fragment {

    private HomeFragmentViewHolder viewHolder;
    private FilmGenres genre;
    private HomeFragmentViewModel viewModelHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        this.viewHolder = new HomeFragmentViewHolder(view);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        setupObservers();

        viewModelHome.executeServiceGetGenreList();

        Log.i("TAG", "ciclo: onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("TAG", "ciclo: onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG", "ciclo: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TAG", "ciclo: onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("TAG", "ciclo: onStop");
    }

    private void setupObservers() {
        viewModelHome.getThereIsAGenreList().observe(this, genresObserver);
        viewModelHome.getIsSessionExpired().observe(this, sessionObserver);
    }

    private Observer<FilmGenres> genresObserver = new Observer<FilmGenres>() {
        @Override
        public void onChanged(@Nullable FilmGenres filmGenres) {
            GenresResponse genresResponse = new GenresResponse(-1, "Lançamentos");

            genre = filmGenres;

            genre.getGenres().add(0, genresResponse);
            genre.getGenres().remove(genre.getGenres().size() - 1);

            FragmentStatePagerAdapter fragmentStatePagerAdapter =
                    new FragmentStateAdapter(getFragmentManager(), genre);

            viewHolder.viewPager.setAdapter(fragmentStatePagerAdapter);

            viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager);
        }
    };

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if (isSessionExpired) {
                SingletonAccessToken.setAccessTokenReceived(null);
                DialogSessionExpired dialogSessionExpired = new DialogSessionExpired();
                FragmentManager fragmentManager = getChildFragmentManager();
                dialogSessionExpired.show(fragmentManager, "SessionExpired");
            }
        }
    };

}
