package br.com.estagio.oletrainning.zup.otmovies.Services.Singleton;

import android.util.Log;

import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public enum SingletonFilmGenres {

    INSTANCE;

    private FilmGenres filmGenres;

    private void setFilmGenres(FilmGenres filmGenres) {
        this.filmGenres = filmGenres;
    }

    public static void setFilmGenresEntered(FilmGenres filmGenres){
        SingletonFilmGenres singletonFilmGenres = SingletonFilmGenres.INSTANCE;
        singletonFilmGenres.setFilmGenres(filmGenres);
        if (singletonFilmGenres.filmGenres != null){
            Log.d("SINGLETON_FILM_GENRE", "Salvo, film genres arquivo");
        }

    }

    public FilmGenres getFilmGenres(){
        if(filmGenres != null){
            return filmGenres;
        }
        return null;
    }
}
