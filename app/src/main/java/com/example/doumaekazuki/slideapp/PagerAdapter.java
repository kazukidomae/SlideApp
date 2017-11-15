package com.example.doumaekazuki.slideapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{

    private Fragment[] fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new Fragment[3];

        for(int i=0 ; i<3 ; i++){
            fragments[i] = new Page1Fragment();
        }
//        fragments[0] = new Page1Fragment();
//        fragments[1] = new Page2Fragment();
//        fragments[2] = new Page3Fragment();
    }

    @Override
    public Fragment getItem(int id) {
        return fragments[id];
    }

    // ページ数
    @Override
    public int getCount() {
        return 2;
    }
}
