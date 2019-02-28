package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.Film;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class ListFilmsAdapter extends RecyclerView.Adapter<ListFilmsAdapter.ListFilmsViewHolder> {

    private List<Film> films;
    private FilmGenres filmGenres;

    public FilmGenres getFilmGenres() {
        return filmGenres;
    }

    public ListFilmsAdapter(FilmGenres filmGenres) {
        this.filmGenres = filmGenres;
        films = new ArrayList<>();


    }

    @NonNull
    @Override
    public ListFilmsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_film,viewGroup,false);

        return new ListFilmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListFilmsViewHolder listFilmsViewHolder, int position) {
        listFilmsViewHolder.bind(films.get(position),filmGenres);
    }

    @Override
    public int getItemCount() {
        return (films != null && films.size() >0) ? films.size() : 0;
    }

    static class ListFilmsViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitleFilm;
        private ProgressBar progressBar;
        private ImageView imageView;
        private TextView keywords;
        private TextView movieDescription;
        private LinearLayout informations;
        private TextView runtime;
        private TextView year;
        private FrameLayout filmNoteFrameLayout;
        private TextView filmNote;
        private CheckBox checkBox;
        private TextView price;


        public ListFilmsViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.movie_progress);
            imageView = itemView.findViewById(R.id.movie_poster);
            keywords = itemView.findViewById(R.id.textView_keywords);
            movieDescription = itemView.findViewById(R.id.movie_description);
            informations = itemView.findViewById(R.id.linearLayout_runtimeAndYear);
            runtime = informations.findViewById(R.id.textView_runtime);
            year = informations.findViewById(R.id.textView_year);
            filmNoteFrameLayout = itemView.findViewById(R.id.frameLayout_filmNote);
            filmNote = filmNoteFrameLayout.findViewById(R.id.textView_filmNote);
            textTitleFilm = itemView.findViewById(R.id.text_title_film);
            checkBox = itemView.findViewById(R.id.checkbox_favorite);
            price = itemView.findViewById(R.id.textView_price);

        }



        private void bind(Film film, FilmGenres allGenresList){
            textTitleFilm.setText(film.getTitle());
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/" + film.getPosterPath())
                    .into(imageView);
            List<Integer> filmGenreIdsList = film.getGenre_ids();
            if(filmGenreIdsList != null && allGenresList != null){
                keywords.setText(keywordPhraseBuilder(filmGenreIdsList,allGenresList));
            }
            movieDescription.setText(film.getOverview());
            runtime.setText("2h 35min");
            year.setText(film.getReleaseDate());
            filmNote.setText(String.valueOf(film.getVoteAverage()));
            price.setText("R$ 16,90");
        }

        private String removeLastComma(String phrase){
            return phrase.substring(0, phrase.length()-2);
        }

        private String keywordPhraseBuilder(@NonNull List<Integer> filmGenreIdsList, @NonNull FilmGenres allGenresList){
            StringBuilder keywordList = new StringBuilder();
            for(int i= 0; i<filmGenreIdsList.size(); i++){
                if(i<5){
                    for(int j=0; j<allGenresList.getGenres().size(); j++){
                        if(filmGenreIdsList.get(i).equals(allGenresList.getGenres().get(j).getId())){
                            keywordList.append(allGenresList.getGenres().get(j).getName());
                            keywordList.append(", ");
                        }
                    }
                }
            }
            return removeLastComma(keywordList.toString());
        }
    }

    public void setFilms(List<Film> films){
        this.films = films;
        notifyDataSetChanged();
    }

}
