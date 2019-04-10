package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonFilmID;

public class SearchFragment extends CommonFragment {

    private SearchFragmentViewHolder searchFragmentViewHolder;
    private SearchFragmentViewModel searchFragmentViewModel;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.searchFragmentViewHolder = new SearchFragmentViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        searchFragmentViewModel = ViewModelProviders.of(SearchFragment.this).get(SearchFragmentViewModel.class);
        searchFragmentViewModel.getFragmentTellerIsSessionExpired().observe(this, sessionObserver);

        searchFragmentViewModel.executeServiceGetFilmResults("1","avengers","name");

        searchFragmentViewHolder.searchView.setOnQueryTextListener(searchViewListener);

        return view;
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

    private SearchView.OnQueryTextListener searchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.i("TAG", "search: "+newText);
            return false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchFragmentViewModel.removeObserver();
        SingletonAlertDialogSession.INSTANCE.destroyAlertDialogBuilder();
        SingletonFilmID.setIDEntered(null);
    }

    public static SearchFragment newInstance() {
        SearchFragment myFragment = new SearchFragment();
        return myFragment;
    }
}
