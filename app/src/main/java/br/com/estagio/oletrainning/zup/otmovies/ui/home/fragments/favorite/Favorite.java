package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.favorite;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.estagio.oletrainning.zup.otmovies.ui.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.movieDetailsActivity.MovieDetails;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonFilmID;

public class Favorite extends CommonFragment {

    private FavoriteViewModel favoriteViewModel;
    private FavoriteViewHolder fragmentViewHolder;
    private RecyclerView recyclerView;
    private String genreID;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container,false);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView= view.findViewById(R.id.recycler_films);

        setupObservers();

        favoriteViewModel.executeServiceGetFilmResults("1", "-1", "28");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter == null) {
            adapter = new FilmAdapter(getActivity());
        }
        setupObservers();
        setupLayoutManager();
    }

    private void setupLayoutManager() {
        fragmentViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        fragmentViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObservers(){
        favoriteViewModel.getFragmentTellerThereIsFilmResults().observe(this,thereIsFilmResultsObserver);
    }

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
        }
    };

    private Observer<FilmsResults> thereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            if(filmsResults.getResults() != null){
                favoriteViewModel.getItemPagedList().observe(Favorite.this, pagedListObserver);
                fragmentViewHolder.recyclerView.setAdapter(adapter);
                adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                    @Override
                    public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                        SingletonFilmID.setIDEntered(currentList.get(position).getId());
                        if(isChecked){
                            favoriteViewModel.executeAddFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                    String.valueOf(SingletonFilmID.INSTANCE.getID()));
                        } else {
                            favoriteViewModel.executeRemoveFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                    String.valueOf(SingletonFilmID.INSTANCE.getID()));
                        }
                    }
                });
                adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                        Log.d("position",String.valueOf(position));
                        if (filmsResults != null) {
                            favoriteViewModel.getIsLoading().setValue(true);
                            SingletonFilmID.setIDEntered(currentList.get(position).getId());
                            if(SingletonFilmID.INSTANCE.getID() != null){
                                Intent intent = new Intent(getActivity(), MovieDetails.class);
                                startActivity(intent);
                            }
                            favoriteViewModel.getIsLoading().setValue(false);
                        }
                    }
                });
                favoriteViewModel.itemConfig(10, "-1", "28");
                favoriteViewModel.getIsLoading().setValue(false);
            }

        }
    };

    public static Favorite newInstance() {
        return new Favorite();
    }
}
