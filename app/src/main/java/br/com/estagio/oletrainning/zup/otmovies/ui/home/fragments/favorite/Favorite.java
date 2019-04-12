package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.movieDetailsActivity.MovieDetails;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonFilmID;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonGenreID;

public class Favorite extends CommonFragment {

    private FavoriteViewModel favoriteViewModel;
    private FavoriteViewHolder favoriteViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container,false);
        this.favoriteViewHolder = new FavoriteViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        favoriteViewModel = ViewModelProviders.of(Favorite.this).get(FavoriteViewModel.class);

        favoriteViewModel.getFragmentTellerIsSessionExpired().observe(this, sessionObserver);

        SingletonGenreID.setGenreIDEntered("28");

        favoriteViewModel.executeServiceGetFilmResults("1", SingletonGenreID.INSTANCE.getGenreID(), "genres");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter == null) {
            adapter = new FilmAdapter(getActivity());
        }
        setupObserversAndListeners();
        setupLayoutManager();
    }

    private void setupLayoutManager() {
        favoriteViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        favoriteViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        favoriteViewModel.getIsMessageSuccessForToast().observe(this,isSuccessMessageForToastObserver);
        favoriteViewModel.getIsLoading().observe(this, progressBarObserver);
        favoriteViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        favoriteViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
    }

    private Observer<String> isSuccessMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if (SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() == null) {
                SingletonAlertDialogSession.createAlertDialogBuilder(getActivity());
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().create().setCanceledOnTouchOutside(false);
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().show();
            }
        }
    };

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            favoriteViewModel.getItemPagedList().observe(Favorite.this, pagedListObserver);
            favoriteViewHolder.recyclerView.setAdapter(adapter);
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
            favoriteViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    favoriteViewHolder.progressBar,
                    favoriteViewHolder.frameLayout);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteViewModel.removeObserver();
        SingletonAlertDialogSession.INSTANCE.destroyAlertDialogBuilder();
        SingletonFilmID.setIDEntered(null);
    }

    public static Favorite newInstance() {
        return new Favorite();
    }
}
