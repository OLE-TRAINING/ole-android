package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragment;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends CommonFragment {

    private View v;
    private RecyclerView recyclerView;
    private String genreID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_movie_list, container,false);

        recyclerView= v.findViewById(R.id.recycler_films);



        APIService.getIntance()
                .getMovieGenre("d272326e467344029e68e3c4ff0b4059","pt-BR","popularity.desc","1",genreID)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if(response.isSuccessful()){
                            recyclerView.setLayoutManager(new LinearLayoutManager(MovieListFragment.this.getContext()));

                            recyclerView.setAdapter(new ListFilmsAdapter(response.body().getResults()));
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {

                    }
                });

        return v;
    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }
}
