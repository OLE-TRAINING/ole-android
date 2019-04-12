package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

public enum SingletonTextSearch {

    INSTANCE;

    private String textToSearch;

    private void setTextToSearch(String textToSearch) {
        this.textToSearch = textToSearch;
    }

    public static void createTextSearch(String textSearch){
        SingletonTextSearch singletonTextSearch = SingletonTextSearch.INSTANCE;
        singletonTextSearch.setTextToSearch(textSearch);
    }

    public String getTextToSearch(){
        if(textToSearch != null && !textToSearch.isEmpty()){
            return textToSearch;
        }
        return null;
    }
}
