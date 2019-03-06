package br.com.estagio.oletrainning.zup.otmovies.HomeActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
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
    private HomeFragment homeFragment;

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

        toggle.syncState();

        homeFragment = new HomeFragment();
        openFragment(homeFragment);
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
        switch (item.getItemId()){
            case  R.id.navigation_home:
                if(homeFragment != null && !homeFragment.isVisible()){
                    homeFragment = new HomeFragment();
                    openFragment(homeFragment);
                }
                break;

            case R.id.navigation_favorite:
                openFragment(new FavoriteFragment());
                break;

            case R.id.navigation_search:
                openFragment(new SearchFragment());
                break;

        }
        return true;
    }

    private boolean openFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home_drawer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
