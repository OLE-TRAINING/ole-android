package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class FilmAdapterDetailsList extends PagedListAdapter<FilmResponse, RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 0;
    private Context mCtx;
    private FilmAdapterDetailsList.OnItemClickListener onItemClickListener;
    private MovieDetailsModel movieDetailsModel;

    public interface OnItemClickListener{
        void onItemClick(int position, PagedList<FilmResponse> currentList);
    }

    public void setOnItemClickListener(FilmAdapterDetailsList.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public FilmAdapterDetailsList(Context mCtx, MovieDetailsModel movieDetailsModel) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.movieDetailsModel = movieDetailsModel;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.item_film, parent, false);
            return new ItemViewHolder(view,this.onItemClickListener, getCurrentList());
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.item_movie_details, parent, false);
            return new DetailsViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FilmResponse film = getItem(position);
        if (viewHolder instanceof ItemViewHolder) {
            if(film != null){
                ((ItemViewHolder) viewHolder).setFilmeResponseInformations(film);
            }
        } else if (viewHolder instanceof DetailsViewHolder){
            if(movieDetailsModel != null){
                ((DetailsViewHolder) viewHolder).setMovieDetailsInformations(this.movieDetailsModel);
            }
        } else {
            TastyToast.makeText(mCtx,"Não foi possível carregar os detalhes deste filme.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
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
}
