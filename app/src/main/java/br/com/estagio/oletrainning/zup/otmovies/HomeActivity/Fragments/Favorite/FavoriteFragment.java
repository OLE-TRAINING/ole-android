package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite;

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

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails.MovieDetailsActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class FavoriteFragment extends CommonFragment {

    private FavoriteFragmentViewModel favoriteFragmentViewModel;
    private FavoriteFragmentViewHolder fragmentViewHolder;
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

        favoriteFragmentViewModel.executeServiceGetFilmResults("1", "-1", "28");

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
        favoriteFragmentViewModel.getFragmentTellerThereIsFilmResults().observe(this,thereIsFilmResultsObserver);
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
                favoriteFragmentViewModel.getItemPagedList().observe(FavoriteFragment.this, pagedListObserver);
                fragmentViewHolder.recyclerView.setAdapter(adapter);
                adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                    @Override
                    public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                        SingletonFilmID.setIDEntered(currentList.get(position).getId());
                        if(isChecked){
                            favoriteFragmentViewModel.executeAddFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                    String.valueOf(SingletonFilmID.INSTANCE.getID()));
                        } else {
                            favoriteFragmentViewModel.executeRemoveFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                    String.valueOf(SingletonFilmID.INSTANCE.getID()));
                        }
                    }
                });
                adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                        Log.d("position",String.valueOf(position));
                        if (filmsResults != null) {
                            favoriteFragmentViewModel.getIsLoading().setValue(true);
                            SingletonFilmID.setIDEntered(currentList.get(position).getId());
                            if(SingletonFilmID.INSTANCE.getID() != null){
                                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                                startActivity(intent);
                            }
                            favoriteFragmentViewModel.getIsLoading().setValue(false);
                        }
                    }
                });
                favoriteFragmentViewModel.itemConfig(10, "-1", "28");
                favoriteFragmentViewModel.getIsLoading().setValue(false);
            }

        }
    };

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }
}
