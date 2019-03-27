package br.com.estagio.oletrainning.zup.otmovies.Services.Model;

public class GenreAndPageSize {

    private final int pageSize;
    private final String genreID;

    public Integer getPageSize() {
        return pageSize;
    }

    public String getGenreID() {
        return genreID;
    }

    public GenreAndPageSize(int pageSize, String genreID) {
        this.pageSize = pageSize;
        this.genreID = genreID;
    }
}
