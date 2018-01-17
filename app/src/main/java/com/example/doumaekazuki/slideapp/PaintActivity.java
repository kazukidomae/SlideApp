package com.example.doumaekazuki.slideapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by doumae.kazuki on 2017/12/14.
 */

public class PaintActivity extends AppCompatActivity {

    EditSurfaceView esf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paint);

        //戻るボタン
        ImageButton returnBtn = (ImageButton)findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(returnActivity);

        //SurfaceView
        SurfaceView surface;
        surface = (SurfaceView)findViewById(R.id.editArea);
        esf = new EditSurfaceView(this, surface);
    }

    public View.OnClickListener returnActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        esf.draw(event);
        return true;
    }
}
