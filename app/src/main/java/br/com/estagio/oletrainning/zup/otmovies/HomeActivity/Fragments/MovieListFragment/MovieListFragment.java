package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.MovieListFragment;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmResponse;

public class MovieListFragment extends CommonFragment {


    private MovieListFragmentViewModel movieListFragmentViewModel;
    private MovieListFragmentViewHolder movieListFragmentViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container,false);
        this.movieListFragmentViewHolder = new MovieListFragmentViewHolder(view);

        movieListFragmentViewModel = ViewModelProviders.of(MovieListFragment.this).get(MovieListFragmentViewModel.class);

        movieListFragmentViewModel.startSessionServiceObserver();

        movieListFragmentViewModel.getFragmentTellerIsSessionExpired().observe(this,sessionObserver);

        adapter = new FilmAdapter(getActivity());

        setupLayoutManager();

        movieListFragmentViewModel.executeServiceGetFilmResults("1");

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupObserversAndListeners();
    }

    private void setupLayoutManager(){
        linearLayoutManager = new LinearLayoutManager(getActivity());
        movieListFragmentViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        movieListFragmentViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        movieListFragmentViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListFragmentViewModel.getFragmentTellerThereIsFilmResults().observe(this,homeTellerThereIsFilmResultsObserver);
        movieListFragmentViewModel.getIsErrorMessageForToast().observe(this, isMessageForToastObserver);
        movieListFragmentViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
    }

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,700);
        }
    };

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Sua sessão expirou, favor fazer login novamente!")
                    .setTitle("Aviso:")
                    .setCancelable(false)
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }).create().setCanceledOnTouchOutside(false);
            builder.show();
        }
    };

    private Observer <Boolean> homeTellerThereIsFilmResultsObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            movieListFragmentViewModel.getItemPagedList().observe(MovieListFragment.this, new Observer<PagedList<FilmResponse>>() {
                @Override
                public void onChanged(@Nullable PagedList<FilmResponse> items) {
                    adapter.submitList(items);
                }
            });

            movieListFragmentViewHolder.recyclerView.setAdapter(adapter);
            movieListFragmentViewModel.getIsLoading().setValue(false);

        }
    };

    private Observer<String> isMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieListFragmentViewModel.removeObserver();
    }
}