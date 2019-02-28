package br.com.estagio.oletrainning.zup.otmovies.Services.Response;

import java.util.List;

public class FilmsResults {

    private final List<FilmResponse> results;

    public FilmsResults(List<FilmResponse> results) {
        this.results = results;
    }

    public List<FilmResponse> getResults() {
        return results;
    }
}
