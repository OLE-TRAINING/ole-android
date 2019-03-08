package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
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

public class NewListFilmAdapter extends PagedListAdapter<Film,NewListFilmAdapter.FilmViewHolder> {

    private List<Film> films;
    private FilmGenres filmGenres;
    private Context context;


    protected NewListFilmAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    protected NewListFilmAdapter(@NonNull AsyncDifferConfig<Film> config) {
        super(config);
    }


    @NonNull
    @Override
    public NewListFilmAdapter.FilmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_film, viewGroup, false);

        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewListFilmAdapter.FilmViewHolder filmViewHolder, int position) {
        filmViewHolder.bind(films.get(position));
    }

    private static DiffUtil.ItemCallback<Film> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Film>() {
                @Override
                public boolean areItemsTheSame(@NonNull Film oldItem, @NonNull Film newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Film oldItem, @NonNull Film newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class FilmViewHolder extends RecyclerView.ViewHolder{

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

        public FilmViewHolder(@NonNull View itemView) {
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

        private void bind(Film film) {
            textTitleFilm.setText(film.getTitle());
            Picasso.get()
                    .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+film.getPosterId()
                            +"/image/w342?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                    .into(imageView);
            keywords.setText(keywordPhraseBuilder(film.getGenreNames()));
            movieDescription.setText(film.getOverview());
            runtime.setText(film.getRuntime());
            year.setText(String.valueOf(film.getYear()));
            filmNote.setText(String.valueOf(film.getVoteAverage()));
            String priceText = "R$ "+ String.valueOf(film.getPrice());
            price.setText(priceText);
        }

        private String removeLastComma(String phrase) {
            if(phrase !=null && !phrase.isEmpty()){
                phrase.substring(0, phrase.length() - 1);
            }
            return phrase;
        }

        private String keywordPhraseBuilder(@NonNull List<String> filmGenreNamesList) {
            StringBuilder keywordList = new StringBuilder();
            for (int i = 0; i < filmGenreNamesList.size(); i++) {
                if (i < 5) {
                    keywordList.append(filmGenreNamesList.get(i));
                    keywordList.append(", ");
                }
            }
            return removeLastComma(keywordList.toString());
        }
    }

    public void setFilms(List<Film> films) {
        this.films = films;
        notifyDataSetChanged();
    }
}
