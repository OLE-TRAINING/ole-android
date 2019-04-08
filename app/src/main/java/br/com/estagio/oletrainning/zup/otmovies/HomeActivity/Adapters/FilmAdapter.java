package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class FilmAdapter extends PagedListAdapter<FilmResponse, FilmAdapter.ItemViewHolder> {

    private Context mCtx;
    private FilmAdapter.OnItemClickListener onItemClickListener;
    private FilmAdapter.OnCheckBoxClickListener onCheckBoxClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position, PagedList<FilmResponse> currentList);
    }

    public void setOnItemClickListener(FilmAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnCheckBoxClickListener {
        void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked);
    }

    public void setOnCheckBoxClickListener(FilmAdapter.OnCheckBoxClickListener onCheckBoxClickListener) {
        this.onCheckBoxClickListener = onCheckBoxClickListener;
    }

    public FilmAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_film, parent, false);
        return new ItemViewHolder(view,this.onItemClickListener,this.onCheckBoxClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        FilmResponse film = getItem(position);

        if (film != null) {

            holder.textTitleFilm.setText(film.getTitle());
            if(film.getPosterId() == null || film.getPosterId().isEmpty()){
                holder.cardViewPoster.setVisibility(View.INVISIBLE);
            } else {
                holder.cardViewPoster.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+film.getPosterId()
                                +"/image/w342?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                        .into(holder.imageView);
            }
            film.getGenreNames();
            holder.keywords.setText(holder.sentenceBuilder(film.getGenreNames()));
            holder.movieDescription.setText(film.getOverview());
            holder.runtime.setText(film.getRuntime());
            holder.year.setText(String.valueOf(film.getYear()));
            holder.filmNote.setText(String.valueOf(film.getVoteAverage()));
            Float filmPrice = film.getPrice();
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String priceText = "R$ "+ String.valueOf(decimalFormat.format(filmPrice));
            holder.price.setText(priceText);
            if(film.isFavorit()){
                holder.checkBox.isChecked();
            }

        } else {
            TastyToast.makeText(mCtx,"Não foi possível carregar este filme.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,700);
        }
    }


    private static DiffUtil.ItemCallback<FilmResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<FilmResponse>() {
                @Override
                public boolean areItemsTheSame(FilmResponse oldItem, FilmResponse newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(FilmResponse oldItem, FilmResponse newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class ItemViewHolder extends RecyclerView.ViewHolder {

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
        private CardView cardViewPoster;

        public ItemViewHolder(View itemView, final OnItemClickListener onItemClickListener,
                              final OnCheckBoxClickListener onCheckBoxClickListener) {
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
            cardViewPoster = itemView.findViewById(R.id.cardview_poster_item);
            checkBox = itemView.findViewById(R.id.checkbox_favorite);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCheckBoxClickListener != null) {
                        int position = getAdapterPosition();
                        if(checkBox.isChecked()){
                            onCheckBoxClickListener.OnCheckBoxClick(position, getCurrentList(),true);
                        } else {
                            onCheckBoxClickListener.OnCheckBoxClick(position, getCurrentList(),false);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        PagedList<FilmResponse> currentList = getCurrentList();
                        int positon = getAdapterPosition();
                        if (positon != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(positon, currentList);
                        }
                    }
                }
            });
        }

        private String sentenceBuilder(@NonNull List<String> listString) {
            StringBuilder keywordList = new StringBuilder();
            for (int i = 0; i < listString.size(); i++) {
                keywordList.append(listString.get(i));
                if (i < listString.size() - 1) {
                    keywordList.append(", ");
                }
            }
            Log.d("KEYWORDS", keywordList.toString());
            return keywordList.toString();
        }
    }
}
