package com.example.doumaekazuki.slideapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * Created by doumae.kazuki on 2018/02/07.
 */

public class MenuAnimation extends RelativeLayout{

    Context context;
    Animation inAnimation;
    Animation outAnimation;

    public MenuAnimation(Context context) {
        super(context);
        this.context = context;

        // メニューアニメーション
        inAnimation = (Animation) AnimationUtils.loadAnimation(context, R.anim.in_animation);
        outAnimation= (Animation) AnimationUtils.loadAnimation(context, R.anim.out_animation);
    }

    // メニューオープン
    public void openAnimation(View view){
        view.startAnimation(inAnimation);
        view.setVisibility(View.VISIBLE);
    }

    // メニュークローズ
    public void closeAnimation(View view){
        view.startAnimation(outAnimation);
        view.setVisibility(View.GONE);
    }
}
