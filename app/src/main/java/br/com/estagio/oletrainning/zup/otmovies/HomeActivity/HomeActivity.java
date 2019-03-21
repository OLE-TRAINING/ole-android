package br.com.estagio.oletrainning.zup.otmovies.HomeActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;

import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite.FavoriteFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home.DialogConfirmLogout;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home.HomeFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Search.SearchFragment;
import br.com.estagio.oletrainning.zup.otmovies.PreLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.Services.Singleton.SingletonName;


public class HomeActivity extends CommonActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private HomeActivityViewHolder homeActivityViewHolder;
    private static final String TAG_FRAGMENT_HOME = "fragment_home";
    private static final String TAG_FRAGMENT_FAVORITE = "fragment_favorite";
    private static final String TAG_FRAGMENT_SEARCH = "fragment_search";

    private Fragment home = new HomeFragment();
    public Fragment getHome() {
        return home;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_home, null);
        this.homeActivityViewHolder = new HomeActivityViewHolder(view);
        setContentView(view);

        setSupportActionBar(homeActivityViewHolder.toolbar);

        if(SingletonEmail.INSTANCE.getEmail() == null || SingletonName.INSTANCE.getUsername() == null){
            Intent intent = new Intent(this, PreLoginActivity.class);
            startActivity(intent);
        }

        String emailAdd = SingletonEmail.INSTANCE.getEmail();
        homeActivityViewHolder.textView_navView_email.setText(emailAdd);
        homeActivityViewHolder.textView_navView_name.setText(SingletonName.INSTANCE.getUsername());

        setupListener();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, homeActivityViewHolder.drawerLayout, homeActivityViewHolder.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        homeActivityViewHolder.drawerLayout.addDrawerListener(toggle);

        SpannableString spannableString = new SpannableString("OT" + "MOVIES");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        homeActivityViewHolder.titleToobar.setText(spannableString);

        toggle.syncState();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(), R.color.colorPrimary, false);
        pushFragments(TAG_FRAGMENT_HOME, getHome());
    }

    private void setupListener() {
        homeActivityViewHolder.bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        homeActivityViewHolder.navigationView.setNavigationItemSelectedListener(navigationViewListener);

    }

    NavigationView.OnNavigationItemSelectedListener navigationViewListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_logout:
                            logoutConfirmation();
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public void onBackPressed() {
        if (homeActivityViewHolder.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeActivityViewHolder.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            logoutConfirmation();
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
                pushFragments(TAG_FRAGMENT_FAVORITE, new FavoriteFragment());
                break;
            case R.id.navigation_search:
                pushFragments(TAG_FRAGMENT_SEARCH, new SearchFragment());
                break;
        }
        return true;
    }

    private void pushFragments(String tag, Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.content_home_drawer, fragment, tag);
        }

        Fragment fragmentHome = manager.findFragmentByTag(TAG_FRAGMENT_HOME);
        Fragment fragmentFavorite = manager.findFragmentByTag(TAG_FRAGMENT_FAVORITE);
        Fragment fragmentSearch = manager.findFragmentByTag(TAG_FRAGMENT_SEARCH);

        if (fragmentHome != null) {
            ft.hide(fragmentHome);
        }
        if (fragmentFavorite != null) {
            ft.hide(fragmentFavorite);
        }
        if (fragmentSearch != null) {
            ft.hide(fragmentSearch);
        }

        if (tag == TAG_FRAGMENT_HOME) {
            if (fragmentHome != null) {
                ft.show(fragmentHome);
            }
        }
        if (tag == TAG_FRAGMENT_FAVORITE) {
            if (fragmentFavorite != null) {
                ft.show(fragmentFavorite);
            }
        }

        if (tag == TAG_FRAGMENT_SEARCH) {
            if (fragmentSearch != null) {
                ft.show(fragmentSearch);
            }
        }
        ft.commitAllowingStateLoss();
    }

    private void logoutConfirmation() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogConfirmLogout dialogConfirmLogout = new DialogConfirmLogout();
        dialogConfirmLogout.show(fragmentManager, "LogoutConfirmation");
    }
}
