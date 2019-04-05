package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails.MovieDetailsActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonGenreID;

public class MovieListFragment extends CommonFragment {

    private MovieListFragmentViewModel movieListFragmentViewModel;
    private MovieListFragmentViewHolder movieListFragmentViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        movieListFragmentViewModel = ViewModelProviders.of(MovieListFragment.this).get(MovieListFragmentViewModel.class);
        movieListFragmentViewModel.getFragmentTellerIsSessionExpired().observe(this, sessionObserver);

        movieListFragmentViewModel.executeServiceGetFilmResults("1");

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
        movieListFragmentViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        movieListFragmentViewHolder.recyclerView.setHasFixedSize(true);
    }



    private void setupObserversAndListeners() {
        movieListFragmentViewModel.getFragmentTellerIsDoubleClickHome().observe(this,fragmentTellerdoubleClickObserver);
        movieListFragmentViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListFragmentViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieListFragmentViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);

    }

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<Boolean> fragmentTellerdoubleClickObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isDoubleClick) {
            if(isDoubleClick){
                if(movieListFragmentViewHolder.recyclerView.getLayoutManager() != null){
                    movieListFragmentViewHolder.recyclerView.getLayoutManager().scrollToPosition(0);
                }

            }
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
            movieListFragmentViewModel.getItemPagedList().observe(MovieListFragment.this, pagedListObserver);
            movieListFragmentViewHolder.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    Log.d("position",String.valueOf(position));
                    if (filmsResults != null) {
                        movieListFragmentViewModel.getIsLoading().setValue(true);
                        SingletonFilmID.setIDEntered(currentList.get(position).getId());
                        if(SingletonFilmID.INSTANCE.getID() != null){
                            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                            startActivity(intent);
                        }
                        movieListFragmentViewModel.getIsLoading().setValue(false);
                    }
                }
            });
            movieListFragmentViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieListFragmentViewHolder.progressBar,
                    movieListFragmentViewHolder.frameLayout);
        }
    };

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        movieListFragmentViewModel.removeObserver();
        SingletonAlertDialogSession.INSTANCE.destroyAlertDialogBuilder();
        SingletonFilmID.setIDEntered(null);
    }
}