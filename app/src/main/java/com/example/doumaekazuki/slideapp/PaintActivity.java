package com.example.doumaekazuki.slideapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by doumae.kazuki on 2017/12/14.
 */

public class PaintActivity extends Activity {

    EditSurfaceView esf;

    View paintCustomer;
    Animation inAnimation;
    Animation outAnimation;

    RadioGroup rgColor;
    RadioGroup rgStroke;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paint);

        // SurfaceView
        SurfaceView surface;
        surface = (SurfaceView)findViewById(R.id.editArea);
        Intent intent = getIntent();
        esf = new EditSurfaceView(this, surface,intent.getStringExtra("editImage"));

        // 戻るボタン
        ImageButton returnBtn = (ImageButton)findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(returnActivity);

        // 線切れ変えボタン
        ImageButton paintBtn = (ImageButton)findViewById(R.id.paintButton);
        paintBtn.setOnClickListener(paintChange);

        // 線カスタマイズ
        paintCustomer = findViewById(R.id.paintCustomer);

        //アニメーション
        inAnimation = (Animation) AnimationUtils.loadAnimation(this, R.anim.in_animation);
        outAnimation= (Animation) AnimationUtils.loadAnimation(this, R.anim.out_animation);

        // 選択色取得
        rgColor = (RadioGroup)findViewById(R.id.colorPicker);

        rgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                // 線色変更
                RadioButton rb = (RadioButton)findViewById(rgColor.getCheckedRadioButtonId());
                ColorDrawable color = (ColorDrawable)rb.getBackground();
                esf.setR(hex2int(java.lang.Integer.toHexString(color.getColor()).substring(2,4)));
                esf.setG(hex2int(java.lang.Integer.toHexString(color.getColor()).substring(4,6)));
                esf.setB(hex2int(java.lang.Integer.toHexString(color.getColor()).substring(6,8)));
                esf.paint();
            }
        });

        rgStroke = (RadioGroup)findViewById(R.id.strokeWidth);

        rgStroke.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                // 線幅変更
                RadioButton rb = (RadioButton)findViewById(rgStroke.getCheckedRadioButtonId());
                switch (rb.getId()){
                    case R.id.strokeWidthSS:
                        esf.setSw(5);
                        break;
                    case R.id.strokeWidthS:
                        esf.setSw(10);
                        break;
                    case R.id.strokeWidthM:
                        esf.setSw(20);
                        break;
                    case R.id.strokeWidthL:
                        esf.setSw(40);
                        break;
                    case R.id.strokeWidthLL:
                        esf.setSw(60);
                        break;
                }
                esf.paint();
            }
        });
    }

    // RGB変換
    public int hex2int(String s){
        int v=0;
        try {
            v=Integer.parseInt(s,16);
        }catch (Exception e){
            v=0;
        }
        return v;
    }

    //Activity切り替え
    public View.OnClickListener returnActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    // 線カスタマイズメニューアニメーション
    public View.OnClickListener paintChange = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(paintCustomer.getVisibility() == View.GONE){
                // 開く
                paintCustomer.startAnimation(inAnimation);
                paintCustomer.setVisibility(View.VISIBLE);
            }
            else {
                // 閉じる
                paintCustomer.startAnimation(outAnimation);
                paintCustomer.setVisibility(View.GONE);
            }
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        esf.draw(event);
        return true;
    }
}
