package com.example.doumaekazuki.slideapp;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class MainActivity extends FragmentActivity {

    private static final int SHOW_RESULT = 1001;
    ViewPager vp;
    PagerAdapter pa;
    private static final int CHOOSE_PHOTO_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager)findViewById(R.id.pager);
        pa = new com.example.doumaekazuki.slideapp.PagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pa);

        //slideボタン処理
        ImageButton slideBtn = (ImageButton)findViewById(R.id.SlideButton);
        slideBtn.setOnClickListener(slide);
    }

    public void setItem(int item){
        vp.setCurrentItem(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // slide登録
    public View.OnClickListener slide = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //ギャラリー呼び出し
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(intent,"画像を選択"), REQUEST_GALLERY);
            startActivityForResult(Intent.createChooser(intent, "Choose Photo"), CHOOSE_PHOTO_REQUEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = null;
        uri = data.getData();
        String u = data.getData().toString();
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

            Page2Fragment p = new Page2Fragment();
            pa = new com.example.doumaekazuki.slideapp.PagerAdapter(getSupportFragmentManager());
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("data",u);
            p.setArguments(bundle);

            ft.add(R.id.Test,p);
            ft.commit();

            vp.setAdapter(pa);

            findViewById(R.id.Test).setVisibility(View.GONE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}