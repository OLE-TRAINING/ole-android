package br.com.estagio.oletrainning.zup.otmovies.HomeActivity.Fragments.Home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class HomeFragmentViewHolder {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;



    public HomeFragmentViewHolder(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        toolbar = view.findViewById(R.id.toolbar);


    }
}
