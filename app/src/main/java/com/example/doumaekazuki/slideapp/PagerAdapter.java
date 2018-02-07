package com.example.doumaekazuki.slideapp;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{

    public List<String> images = new ArrayList<>();

    public PagerAdapter(FragmentManager fm,List<String> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(images.get(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    // ページ数
    @Override
    public int getCount() {
        return images.size();
    }

    

}
