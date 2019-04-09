package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment.MovieListFragmentViewModel;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.MovieDetails.MovieDetailsActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class SearchFragment extends CommonFragment {

    private MovieListFragmentViewModel movieListFragmentViewModel;
    private SearchFragmentViewHolder searchFragmentViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.searchFragmentViewHolder = new SearchFragmentViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        movieListFragmentViewModel = ViewModelProviders.of(SearchFragment.this).get(MovieListFragmentViewModel.class);
        movieListFragmentViewModel.getFragmentTellerIsSessionExpired().observe(this, sessionObserver);

        movieListFragmentViewModel.executeServiceGetFilmResults("1");

        searchFragmentViewHolder.searchView.setOnQueryTextListener(searchViewListener);

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
        searchFragmentViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        searchFragmentViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        movieListFragmentViewModel.getIsMessageSuccessForToast().observe(this,isSuccessMessageForToastObserver);
        movieListFragmentViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListFragmentViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieListFragmentViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);

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
            movieListFragmentViewModel.getItemPagedList().observe(SearchFragment.this, pagedListObserver);
            searchFragmentViewHolder.recyclerView.setAdapter(adapter);
            adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                @Override
                public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if(isChecked){
                        movieListFragmentViewModel.executeAddFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                String.valueOf(SingletonFilmID.INSTANCE.getID()));
                    } else {
                        movieListFragmentViewModel.executeRemoveFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                String.valueOf(SingletonFilmID.INSTANCE.getID()));
                    }
                }
            });
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
                    searchFragmentViewHolder.progressBar,
                    searchFragmentViewHolder.frameLayout);
        }
    };

    private SearchView.OnQueryTextListener searchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieListFragmentViewModel.removeObserver();
        SingletonAlertDialogSession.INSTANCE.destroyAlertDialogBuilder();
        SingletonFilmID.setIDEntered(null);
    }

    public static SearchFragment newInstance() {
        SearchFragment myFragment = new SearchFragment();
        return myFragment;
    }
}
