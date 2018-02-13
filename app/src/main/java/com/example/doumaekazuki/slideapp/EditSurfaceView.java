package com.example.doumaekazuki.slideapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Random;


//import static com.example.doumaekazuki.slideapp.R.layout.paint;

/**
 * Created by doumae.kazuki on 2018/01/16.
 */

public class EditSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;

    Paint mPaint;
    Path mPath;

    Bitmap testBitmap;
    Canvas testCanvas;

    Canvas drawCanvas;

    String imagePass;
    Uri uri;

    int r,g,b,sw; // 描画線設定

    // 途中経過
    private Deque<Path> mUndoStack = new ArrayDeque<Path>();
    List<Path> pathList;
    List<Paint> paintList;

    // テスト
    Bitmap test;

    int sizeX,sizeY;

    public EditSurfaceView(Context context){
        super(context);
    }

    public EditSurfaceView(Context context, SurfaceView surface, String pass) {
        super(context);
        setZOrderOnTop(true);

        holder = surface.getHolder();
        // コールバック設定
        holder.addCallback(this);

        // 編集画像
        imagePass = pass;
        // 線色初期設定
        r = 0;
        g = 0;
        b = 0;
        // 線幅初期設定
        sw = 26;

        paint();
    }

    // 線色セッター
    public void setR(int r) {
        this.r = r;
    }
    public void setG(int g) {
        this.g = g;
    }
    public void setB(int b) {
        this.b = b;
    }

    // 線幅セッター
    public void setSw(int sw) {
        this.sw = sw;
    }

    // 線色幅設定
    public void paint(){
        setZOrderMediaOverlay(true);
        holder.setFormat(PixelFormat.TRANSPARENT);

        mPaint = new Paint();
        mPaint.setColor(Color.rgb(r,g,b));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(sw);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

        // サイズ取得
        sizeX = width;
        sizeY = height;

        // Path,Paintリスト初期化
        pathList = new ArrayList<Path>();
        paintList = new ArrayList<Paint>();
        clearLastDrawBitmap();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        test.recycle();
    }

    private void clearLastDrawBitmap() {
        // Bitmap変換
        uri = Uri.parse(imagePass);
        try {
            testBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
            test = Bitmap.createScaledBitmap(testBitmap.copy(Bitmap.Config.ARGB_8888,true), sizeX, sizeY, true);
            testBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        testCanvas = new Canvas(test);
        drawCanvas =  holder.lockCanvas();
        drawingImage();
        holder.unlockCanvasAndPost(drawCanvas);
    }

    // タッチ位置取得
    public void draw(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event.getX(), event.getY());
                break;
            default:
        }
    }

    private void onTouchDown(float x, float y) {
        mPath = new Path();
        mPath.moveTo(x, y);
    }
    private void onTouchMove(float x, float y) {
        mPath.lineTo(x, y);
        drawLine(mPath);
    }
    private void onTouchUp(float x, float y) {
        mPath.lineTo(x, y);
        drawLine(mPath);
        testCanvas.drawPath(mPath, mPaint);
        pathList.add(mPath);
        paintList.add(mPaint);
    }


    // 描画処理
    private void drawLine(Path path) {
        drawCanvas = holder.lockCanvas();
        // キャンバスをクリア
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawingImage();
        // パスを描画
        drawCanvas.drawPath(path, mPaint);
        holder.unlockCanvasAndPost(drawCanvas);
    }

    public void undo() {
        if (pathList.size() != 0 && paintList.size() != 0) {

            // 最後のPath,Paintを削除
            pathList.remove(pathList.size() - 1);
            paintList.remove(paintList.size() - 1);
            // Canvas初期化
            clearLastDrawBitmap();
            drawCanvas = holder.lockCanvas();
            drawingImage();
            // パスを描画
            for (int i = 0; i < pathList.size(); i++) {
                drawCanvas.drawPath(pathList.get(i), paintList.get(i));
                testCanvas.drawPath(pathList.get(i), paintList.get(i));
            }
            holder.unlockCanvasAndPost(drawCanvas);
        }
    }

    // 画像描画
    private void drawingImage (){
        // 位置調整
        drawCanvas.translate((drawCanvas.getWidth()-test.getWidth())/2,(drawCanvas.getHeight()-test.getHeight())/2);
        drawCanvas.drawBitmap(test,0,0,null);
    }

    // 画像保存
    public void storage(){

        File dataDir = new File(Environment.getExternalStorageDirectory(), "sampleDir");
        dataDir.mkdirs();
        String fileNumber = String.valueOf(new Random().nextInt(100000000));
        //File filePath = new File(dataDir,"sample"+fileNumber+".jpg");
        File filePath = new File(dataDir,getNowData()+".jpg");
        OutputStream os= null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        test.compress(Bitmap.CompressFormat.JPEG, 100, os);

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, filePath.getAbsolutePath());
            getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNowData() {
        final DateFormat df = new SimpleDateFormat("yyy/mm/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
