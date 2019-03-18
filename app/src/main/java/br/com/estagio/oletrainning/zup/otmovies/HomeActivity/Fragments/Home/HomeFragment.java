package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

        import android.arch.lifecycle.Observer;
        import android.arch.lifecycle.ViewModelProviders;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.FragmentStatePagerAdapter;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import br.com.estagio.oletrainning.zup.otmovies.Common.CommonFragment;
        import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Adapters.FragmentStateAdapter;

        import br.com.estagio.oletrainning.zup.otmovies.R;
        import br.com.estagio.oletrainning.zup.otmovies.Services.Response.FilmGenres;
        import br.com.estagio.oletrainning.zup.otmovies.Services.Response.GenresResponse;


public class HomeFragment extends CommonFragment {

    private HomeFragmentViewHolder viewHolder;
    private FilmGenres genre;
    private HomeFragmentViewModel viewModelHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        this.viewHolder = new HomeFragmentViewHolder(view);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        viewModelHome.executeServiceGetGenreList();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
    }

    private void setupObservers() {
        viewModelHome.getThereIsAGenreList().observe(this, genresObserver);
        viewModelHome.getIsLoading().observe(this, progressBarObserver);
        viewModelHome.getIsSessionExpired().observe(this, sessionObserver);
    }

    private Observer<FilmGenres> genresObserver = new Observer<FilmGenres>() {
        @Override
        public void onChanged(@Nullable FilmGenres filmGenres) {
            GenresResponse genresResponse = new GenresResponse(-1, "Lançamentos");

            genre = filmGenres;
            if(genre != null) {
                genre.getGenres().add(0, genresResponse);
                genre.getGenres().remove(genre.getGenres().size() - 1);
            }
            FragmentStatePagerAdapter fragmentStatePagerAdapter =
                    new FragmentStateAdapter(getFragmentManager(), genre);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelHome.removeObserver();
    }
}