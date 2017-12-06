package com.example.doumaekazuki.slideapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;


/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{

    private Fragment[] fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new Fragment[2];

        // フラグメント生成
        for(int i=0 ; i<2 ; i++){
            fragments[i] = new PageFragment();
        }
    }

    @Override
    public Fragment getItem(int id) {
        return fragments[id];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    // ページ数
    @Override
    public int getCount() {
        return fragments.length;
    }
}
