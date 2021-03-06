package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragment;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

import static java.lang.String.valueOf;

public class FragmentStateAdapter extends FragmentStatePagerAdapter {

    private FilmGenres genre;

    public FragmentStateAdapter(FragmentManager fm, FilmGenres genre) {
        super(fm);
        this.genre = genre;
    }

    @Override
    public Fragment getItem(int position) {
        SingletonGenreID.setGenreIDEntered(findGenreID(position));
        return new MovieListFragment();
    }

    @Override
    public int getCount() {
        return this.genre.getGenres().size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return genre.getGenres().get(position).getName();
    }

    private String findGenreID (int position){
        return valueOf(genre.getGenres().get(position).getId());
    }
}
