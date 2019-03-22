package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FragmentStateAdapter;
import br.com.estagio.oletrainning.zup.otmovies.LoginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;

public class HomeFragment extends CommonFragment {

    private HomeFragmentViewHolder viewHolder;
    private HomeFragmentViewModel viewModelHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        this.viewHolder = new HomeFragmentViewHolder(view);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        setupObservers();

        viewModelHome.executeServiceGetGenreList();

        setRetainInstance(true);

        return view;
    }

    private void setupObservers() {
        viewModelHome.getThereIsAGenreList().observe(this, genresObserver);
        viewModelHome.getIsLoading().observe(this,progressBarObserver);
        viewModelHome.getFragmentTellerIsSessionExpired().observe(this,sessionObserver);
        viewModelHome.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
    }

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

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,700);
        }
    };

    private Observer<FilmGenres> genresObserver = new Observer<FilmGenres>() {
        @Override
        public void onChanged(@Nullable FilmGenres filmGenres) {
            FragmentStatePagerAdapter fragmentStatePagerAdapter =
                    new FragmentStateAdapter(getFragmentManager(),
                            viewModelHome.changeOrderGenres(filmGenres));

            viewHolder.viewPager.setAdapter(fragmentStatePagerAdapter);

            viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager);
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    viewHolder.progressBar,
                    viewHolder.frameLayout);
        }
    };
}
