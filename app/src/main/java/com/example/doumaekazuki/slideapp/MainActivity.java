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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

        // 写真ボタン処理
        ImageButton slideBtn = (ImageButton)findViewById(R.id.SlideButton);
        slideBtn.setOnClickListener(imageSetter);

        // 写真List新規作成ボタン
        Button newBtn = (Button)findViewById(R.id.newImageSet);
        newBtn.setOnClickListener(galleryOpen);

        // 写真追加ボタン
        Button addBtn = (Button)findViewById(R.id.imageAdd);

        // 写真編集ボタン
        ImageButton editBtn = (ImageButton)findViewById(R.id.EditButton);
        editBtn.setOnClickListener(edit);

        // 背景変更ボタン
        ImageButton wallpaperBtn = (ImageButton)findViewById(R.id.wallpaperButton);
        wallpaperBtn.setOnClickListener(wallpaper);

        // 削除ボタン
        ImageButton deleteBtn = (ImageButton)findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(deleteImage);

        wm = WallpaperManager.getInstance(this);
    }

    public void setItem(int item){
        vp.setCurrentItem(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // メニューアニメーション
    public View.OnClickListener imageSetter = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MenuAnimation mAnim = new MenuAnimation(getApplicationContext());
            mAnim.animation(findViewById(R.id.imageSet));
        }
    });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ギャラリー呼び出し
    public View.OnClickListener galleryOpen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent,REQUEST_CODE);
        }
    };

    // 写真List作成
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode ==RESULT_OK){
            // 画像List初期化
            images.clear();
            int itemCount = data.getClipData().getItemCount();
            // 画像List作成
            for (int i = 0; i < itemCount; i++) {
                ClipData.Item url = data.getClipData().getItemAt(i);
                images.add(url.getUri().toString());
            }
            adapterSet();
            vp.setCurrentItem(0);
        }
    }

    // 写真編集
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
                    wm.setBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), wm.getDesiredMinimumWidth(), wm.getDesiredMinimumHeight(), true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // 写真削除
    public View.OnClickListener deleteImage = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(images.size()>1) {
                int fragmentPosition = vp.getCurrentItem();
                // 画像削除
                images.remove(fragmentPosition);
                adapterSet();
                vp.setCurrentItem(fragmentPosition-1);

//                images.clear();
//                adapterSet();
            }
        }
    });

    // アダプターセット
    public void adapterSet(){
        pa = new com.example.doumaekazuki.slideapp.PagerAdapter(getSupportFragmentManager(), images);
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(pa);
    }
}