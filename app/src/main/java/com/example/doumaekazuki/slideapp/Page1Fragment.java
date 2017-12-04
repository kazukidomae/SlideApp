package com.example.doumaekazuki.slideapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class Page1Fragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page1, container, false);
        TextView tv = (TextView)view.findViewById(R.id.textView1);
        tv.setText("テスト");
        return view;
    }
}
