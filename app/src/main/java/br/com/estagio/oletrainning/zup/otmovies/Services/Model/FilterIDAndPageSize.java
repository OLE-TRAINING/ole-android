package br.com.estagio.oletrainning.zup.otmovies.Services.Model;

public class FilterIDAndPageSize {

    private final int pageSize;
    private final String filterID;

    public Integer getPageSize() {
        return pageSize;
    }

    public String getFilterID() {
        return filterID;
    }

    public FilterIDAndPageSize(int pageSize, String filterID) {
        this.pageSize = pageSize;
        this.filterID = filterID;
    }
}
