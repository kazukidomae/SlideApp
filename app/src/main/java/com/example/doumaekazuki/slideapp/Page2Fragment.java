package com.example.doumaekazuki.slideapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class Page2Fragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page2, container, false);
        ImageView iv = (ImageView)view.findViewById(R.id.TestImage);
        iv.setImageResource(R.drawable.test);
        return view;
    }
}
