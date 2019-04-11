package br.com.estagio.oletrainning.zup.otmovies.ui.home.homeActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.ui.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.home.DialogConfirmLogout;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.home.HomeFragment;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.movieList.MovieList;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.search.Search;
import br.com.estagio.oletrainning.zup.otmovies.ui.preLoginActivity.PreLogin;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonGenreID;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonName;


public class HomeActivity extends CommonActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private HomeActivityViewHolder homeActivityViewHolder;


    private Fragment home = HomeFragment.newInstance();
    private Fragment favorite = new MovieList();
    private Fragment search = new Search();

    public Fragment getHome() {
        return home;
    }

    public Fragment getFavorite() {
        return favorite;
    }

    public Fragment getSearch() {
        return search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();

        View view = this.getLayoutInflater().inflate(R.layout.activity_home, null);
        this.homeActivityViewHolder = new HomeActivityViewHolder(view);
        setContentView(view);

        setupListener();

        setSupportActionBar(homeActivityViewHolder.toolbar);

        if(SingletonEmail.INSTANCE.getEmail() == null || SingletonName.INSTANCE.getUsername() == null){
            Intent intent = new Intent(this, PreLogin.class);
            startActivity(intent);
        }

        String emailAdd = SingletonEmail.INSTANCE.getEmail();
        homeActivityViewHolder.textView_navView_email.setText(emailAdd);
        homeActivityViewHolder.textView_navView_name.setText(SingletonName.INSTANCE.getUsername());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, homeActivityViewHolder.drawerLayout, homeActivityViewHolder.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        homeActivityViewHolder.drawerLayout.addDrawerListener(toggle);

        SpannableString spannableString = new SpannableString("OT" + "MOVIES");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        homeActivityViewHolder.titleToobar.setText(spannableString);

        toggle.syncState();

        if (savedInstanceState == null) {
            homeActivityViewHolder.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(), R.color.colorPrimary, false);
    }

    private void setupListener() {
        homeActivityViewHolder.titleToobar.setOnClickListener(backArrowListener);
        homeActivityViewHolder.bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        homeActivityViewHolder.logoutButton.setOnClickListener(logoutOnClickListener);
    }

    private View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(homeActivityViewHolder.bottomNavigationView.getSelectedItemId() != R.id.navigation_home){
                homeActivityViewHolder.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        }
    };

    private View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logoutConfirmation();
        }
    };

    @Override
    public void onBackPressed() {
        if (homeActivityViewHolder.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeActivityViewHolder.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            } else {
                logoutConfirmation();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                pushFragments(TAG_FRAGMENT_HOME, getHome());

                break;
            case R.id.navigation_favorite:
                SingletonGenreID.setGenreIDEntered("-1");
                pushFragments(TAG_FRAGMENT_FAVORITE,getFavorite());
                break;
            case R.id.navigation_search:
                pushFragments(TAG_FRAGMENT_SEARCH, getSearch());
                break;
        }
        return true;
    }

    private void logoutConfirmation() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogConfirmLogout dialogConfirmLogout = new DialogConfirmLogout();
        dialogConfirmLogout.show(fragmentManager, "LogoutConfirmation");
    }
}
