package com.example.doumaekazuki.slideapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;


//import static com.example.doumaekazuki.slideapp.R.layout.paint;

/**
 * Created by doumae.kazuki on 2018/01/16.
 */

public class EditSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;

    Paint mPaint;
    Path mPath = new Path();

    Bitmap testBitmap;
    Canvas testCanvas;

    String imagePass;
    Uri uri;
    // 描画線設定
    int r,g,b,sw;

    // 途中経過
    private Deque<Path> mUndoStack = new ArrayDeque<Path>();
    private Deque<Path> mRedoStack = new ArrayDeque<Path>();

    // テスト
    Bitmap test;

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
        r = 255;
        g = 255;
        b = 255;
        // 線幅初期設定
        sw = 26;

        paint();
    }

    // RGB値セッター
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

    //ペイント設定
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
        clearLastDrawBitmap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        testBitmap.recycle();
    }

    private void clearLastDrawBitmap() {

        if (testBitmap == null) {

            test = Bitmap.createBitmap(72,72, Bitmap.Config.ARGB_8888);

            // Bitmap変換
            uri = Uri.parse(imagePass);
            try {
                testBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (testCanvas == null) {
            testCanvas = new Canvas(test);
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            // 編集画像セット
            testCanvas = holder.lockCanvas();
            testCanvas.drawBitmap(testBitmap,0,0,paint);
            holder.unlockCanvasAndPost(testCanvas);
        }
    }

    // 描画
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
//        mPath = new Path();
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

        mUndoStack.addLast(mPath);
        mRedoStack.clear();
    }



    // 線描画
    private void drawLine(Path path) {
        // ロックしてキャンバスを取得
        Canvas drawCanvas = holder.lockCanvas();
        //testCanvas= holder.lockCanvas();
        // キャンバスをクリア
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // 前回描画したビットマップをキャンバスに描画
        drawCanvas.drawBitmap(testBitmap, 0, 0, null);
        // パスを描画
        drawCanvas.drawPath(path, mPaint);
        // ロックを外す
        holder.unlockCanvasAndPost(drawCanvas);
    }

    public void undo() {
        if (mUndoStack.isEmpty()) {
            return;
        }

        // undoスタックからパスを取り出し、redoスタックに格納します。
        Path lastUndoPath = mUndoStack.removeLast();
        mRedoStack.addLast(lastUndoPath);

        // ロックしてキャンバスを取得します。
        Canvas canvas = holder.lockCanvas();

        // キャンバスをクリアします。
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // 描画状態を保持するBitmapをクリアします。
        clearLastDrawBitmap();

        // パスを描画します。
        for (Path path : mUndoStack) {
            canvas.drawPath(path, mPaint);
            testCanvas.drawPath(path, mPaint);
        }
        // ロックを外します。
        holder.unlockCanvasAndPost(canvas);
    }

    public void redo() {
        if (mRedoStack.isEmpty()) {
            return;
        }

        // redoスタックからパスを取り出し、undoスタックに格納します。
        Path lastRedoPath = mRedoStack.removeLast();
        mUndoStack.addLast(lastRedoPath);
        // パスを描画します。
        drawLine(lastRedoPath);
        testCanvas.drawPath(lastRedoPath, mPaint);
    }

    public void reset() {
        mUndoStack.clear();
        mRedoStack.clear();

        clearLastDrawBitmap();

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        holder.unlockCanvasAndPost(canvas);
    }


    // 画像保存
    public void storage(){
        File dataDir;
        dataDir = new File(Environment.getExternalStorageDirectory(), "sampleDir");
        dataDir.mkdirs();
        String a = String.valueOf(new Random().nextInt(100000000));
        File filePath = new File(dataDir,"sample"+a+".jpg");
        OutputStream os= null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        testBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

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
}
