package br.com.estagio.oletrainning.zup.otmovies.HomeActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;

import br.com.estagio.oletrainning.zup.otmovies.Common.CommonActivity;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Favorite.FavoriteFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home.HomeFragment;
import br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Search.SearchFragment;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class HomeActivity extends CommonActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private HomeActivityViewHolder homeActivityViewHolder;
    private static final String TAG_FRAGMENT_ONE = "fragment_one";
    private static final String TAG_FRAGMENT_TWO = "fragment_two";
    private static final String TAG_FRAGMENT_THREE = "fragment_three";

    private FragmentManager fragmentManager;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_home, null);
        this.homeActivityViewHolder = new HomeActivityViewHolder(view);
        setContentView(view);

        setSupportActionBar(homeActivityViewHolder.toolbar);

        String emailAdd = getIntent().getStringExtra(getString(R.string.EmailPreLogin));
        homeActivityViewHolder.textView_navView_email.setText(emailAdd);

        setupListener();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, homeActivityViewHolder.drawerLayout, homeActivityViewHolder.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        homeActivityViewHolder.drawerLayout.addDrawerListener(toggle);

        SpannableString spannableString = new SpannableString("OT"+"MOVIES");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        homeActivityViewHolder.titleToobar.setText(spannableString);

        toggle.syncState();


        fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_ONE);
        if (fragment == null) {
            fragment = HomeFragment.newInstance();
        }
        replaceFragment(fragment, TAG_FRAGMENT_ONE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorPrimary,false);
    }

    private void setupListener(){
        homeActivityViewHolder.bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        homeActivityViewHolder.navigationView.setNavigationItemSelectedListener(navigationViewListener);
    }

    NavigationView.OnNavigationItemSelectedListener navigationViewListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    return false;
                }
            };

    @Override
    public void onBackPressed() {
        if (homeActivityViewHolder.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            homeActivityViewHolder.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_ONE);
                if (fragment == null) {
                    fragment = HomeFragment.newInstance();
                }
                replaceFragment(fragment, TAG_FRAGMENT_ONE);
                break;
            case R.id.navigation_favorite:
                fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_TWO);
                if (fragment == null) {
                    fragment = new FavoriteFragment();
                }
                replaceFragment(fragment, TAG_FRAGMENT_TWO);
                break;
            case R.id.navigation_search:
                fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_THREE);
                if (fragment == null) {
                    fragment = new SearchFragment();
                }
                replaceFragment(fragment, TAG_FRAGMENT_THREE);
                break;
        }
        return true;
    }

    private void replaceFragment(@NonNull Fragment fragment, @NonNull String tag) {
        if (!fragment.equals(currentFragment)) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_home_drawer, fragment, tag)
                    .commit();
            currentFragment = fragment;
        }
    }
}
