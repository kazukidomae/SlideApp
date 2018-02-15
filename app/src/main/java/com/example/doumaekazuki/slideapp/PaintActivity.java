package com.example.doumaekazuki.slideapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

/**
 * Created by doumae.kazuki on 2017/12/14.
 */

public class PaintActivity extends Activity {

    EditSurfaceView esf;
    RadioGroup rgColor;
    View menuView;

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

        // 隠しメニュー
        // 写真差し替えボタン
        Button changeImageBtn = (Button)findViewById(R.id.changeImage);
        changeImageBtn.setOnClickListener(storageImage);
        // 保存ボタン
        Button saveBtn = (Button)findViewById(R.id.saveImage);
        saveBtn.setOnClickListener(storageImage);

        // 戻るボタン
        ImageButton returnBtn = (ImageButton)findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(returnActivity);

        // 取り消しボタン
        ImageButton undoBtn = (ImageButton)findViewById(R.id.undoButton);
        undoBtn.setOnClickListener(undo);

        // 線カスタマイズメニューボタン
        ImageButton paintBtn = (ImageButton)findViewById(R.id.paintButton);
        paintBtn.setOnClickListener(paintChange);

        // 保存ボタン
        ImageButton storageBtn = (ImageButton)findViewById(R.id.storageButton);
        storageBtn.setOnClickListener(storageMenu);

        // 線色変更
        rgColor = (RadioGroup)findViewById(R.id.colorPicker);
        rgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                // 線色変更
                RadioButton rb = (RadioButton)findViewById(rgColor.getCheckedRadioButtonId());
                String color;
                switch (rb.getId()){
                    case R.id.color1:
                        color = "000000";
                        break;
                    case R.id.color2:
                        color = "ffffff";
                        break;
                    case R.id.color3:
                        color = "808080";
                        break;
                    case R.id.color4:
                        color = "ff0000";
                        break;
                    case R.id.color5:
                        color = "ff7f00";
                        break;
                    case R.id.color6:
                        color = "ffff00";
                        break;
                    case R.id.color7:
                        color = "7fff00";
                        break;
                    case R.id.color8:
                        color = "00ff00";
                        break;
                    case R.id.color9:
                        color = "00ff7f";
                        break;
                    case R.id.color10:
                        color = "00ffff";
                        break;
                    case R.id.color11:
                        color = "007fff";
                        break;
                    case R.id.color12:
                        color = "0000ff";
                        break;
                    case R.id.color13:
                        color = "7f00ff";
                        break;
                    case R.id.color14:
                        color = "ff00ff";
                        break;
                    case R.id.color15:
                        color = "ff007f";
                        break;
                    default:
                        color = "000000";
                        break;
                }
                esf.setR(hex2int(color.substring(0,2)));
                esf.setG(hex2int(color.substring(2,4)));
                esf.setB(hex2int(color.substring(4,6)));
                esf.paint();
            }
        });


        // 線幅変更
        SeekBar sb = (SeekBar)findViewById(R.id.strokeWidthBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                esf.setSw(seekBar.getProgress());
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

    // MainActivityへ戻る
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
            menuOpen(findViewById(R.id.paintCustomer));
        }
    });

    // 線カスタマイズメニューアニメーション
    public View.OnClickListener storageMenu = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuOpen(findViewById(R.id.storage));
        }
    });

    public View.OnClickListener undo = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            esf.undo();
        }
    });

    // 写真保存
    public View.OnClickListener storageImage = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            esf.storage();
            DialogFragment sd = new StorageDialog();
            sd.show(getFragmentManager(),"");
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        esf.draw(event);
        return true;
    }

    // メニューアニメーション
    private void menuOpen(View view){
        MenuAnimation mAnim = new MenuAnimation(getApplicationContext());
        // メニューが開いていない
        if(menuView == null){
            mAnim.openAnimation(view);
            menuView = view;
        }
        // 違うメニューを開く
        else if(menuView != view){
            mAnim.closeAnimation(menuView);
            mAnim.openAnimation(view);
            menuView = view;
        }
        // メニューを全て閉じる
        else if(menuView == view){
            mAnim.closeAnimation(view);
            menuView = null;
        }
    }

}
