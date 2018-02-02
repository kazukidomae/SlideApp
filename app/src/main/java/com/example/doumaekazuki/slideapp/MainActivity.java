package com.example.doumaekazuki.slideapp;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class MainActivity extends FragmentActivity {

    ViewPager vp;
    PagerAdapter pa;
    public List<String> images = new ArrayList<String>();
    WallpaperManager wm;
    private static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wm = WallpaperManager.getInstance(this);

        //slideボタン処理
        ImageButton slideBtn = (ImageButton)findViewById(R.id.SlideButton);
        slideBtn.setOnClickListener(slide);

        //画像編集ボタン
        ImageButton editBtn = (ImageButton)findViewById(R.id.EditButton);
        editBtn.setOnClickListener(edit);

        //背景変更ボタン処理
        ImageButton wallpaperBtn = (ImageButton)findViewById(R.id.wallpaperButton);
        wallpaperBtn.setOnClickListener(wallpaper);
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

    // ギャラリー呼び出し
    public View.OnClickListener slide = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE);
        }
    };

    // 画像List作成
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getClipData() != null && requestCode == 1001) {
            // 画像List初期化
            images.clear();
            int itemCount = data.getClipData().getItemCount();
            // 画像List作成
            for (int i = 0; i < itemCount; i++) {
                ClipData.Item url = data.getClipData().getItemAt(i);
                images.add(url.getUri().toString());
            }
            pa = new com.example.doumaekazuki.slideapp.PagerAdapter(getSupportFragmentManager(), images);
            vp = (ViewPager) findViewById(R.id.pager);
            vp.setAdapter(pa);
            vp.setCurrentItem(0);
        }
    }

    // 画像編集
    public View.OnClickListener edit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!images.isEmpty()) {
                Intent intent = new Intent(getApplication(), PaintActivity.class);
                // 編集画像
                intent.putExtra("editImage", images.get(vp.getCurrentItem()));
                startActivity(intent);
            }
        }
    };

    // 壁紙変更
    public View.OnClickListener wallpaper = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!images.isEmpty()) {
                Uri uri = Uri.parse(images.get(vp.getCurrentItem()));
                DialogFragment di = new DialogMessage();
                di.show(getFragmentManager(), "");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Bitmap test = Bitmap.createScaledBitmap(bitmap, 480, 640, true);
                    wm.setBitmap(bitmap);
                    wm.setBitmap(test);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}