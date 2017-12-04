package com.example.doumaekazuki.slideapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class Page2Fragment extends Fragment{

    Uri uri = null;
    Bitmap bitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if(bundle!=null) {
            uri = Uri.parse(getArguments().getString("data"));

            //InputStream i = getActivity().getContentResolver().openInputStream(uri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                System.out.println("ビットマップ1"+bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        View view = inflater.inflate(R.layout.page2, container, false);
//        ImageView iv = (ImageView)view.findViewById(R.id.TestImage);
//        iv.setImageResource(R.drawable.test);
//        iv.setImageBitmap(bitmap);
        System.out.println("ビットマップ2"+bitmap);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView iv = (ImageView)view.findViewById(R.id.TestImage);
        //iv.setImageResource(R.drawable.test);
        iv.setImageBitmap(bitmap);
        System.out.println("ビットマップ3"+bitmap);
    }
}
