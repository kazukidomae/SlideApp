package com.example.doumaekazuki.slideapp;

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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//import static com.example.doumaekazuki.slideapp.R.layout.paint;

/**
 * Created by doumae.kazuki on 2018/01/16.
 */

public class EditSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    Paint mPaint;
    Path mPath;

    Resources res = this.getContext().getResources();
    Bitmap testBitmap = BitmapFactory.decodeResource(res, R.drawable.test);
    Canvas testCanvas;

    //描画用Bitmap
    Bitmap drawBitmap;
    Canvas drawCanvas;


    public EditSurfaceView(Context context){
        super(context);
    }

    public EditSurfaceView(Context context, SurfaceView surface) {
        super(context);
        holder = surface.getHolder();
        holder.addCallback(this);
        paint();
    }

    //ペイント設定
    public void paint(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //描画処理
//        Canvas canvas = holder.lockCanvas();
//        canvas.drawBitmap(testBitmap,0,0,null);
//        holder.unlockCanvasAndPost(canvas);
        testCanvas = holder.lockCanvas();
        testCanvas.drawBitmap(testBitmap,0,0,null);
        holder.unlockCanvasAndPost(testCanvas);
        clearLastDrawBitmap();


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        drawBitmap.recycle();
    }

    private void clearLastDrawBitmap() {

        if (drawBitmap == null) {
            drawBitmap = Bitmap.createBitmap(380, 640, Bitmap.Config.ARGB_8888);
        }

        if (drawBitmap == null) {
            drawCanvas = new Canvas(drawBitmap);
        }

        drawCanvas = holder.lockCanvas();

        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        holder.unlockCanvasAndPost(drawCanvas);
    }

    //描画
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
    }

    //線描画
    private void drawLine(Path path) {
        // ロックしてキャンバスを取得
        drawCanvas = holder.lockCanvas();
        // キャンバスをクリア
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // 前回描画したビットマップをキャンバスに描画
        drawCanvas.drawBitmap(drawBitmap, 0, 0, null);
        // パスを描画
        drawCanvas.drawPath(path, mPaint);
        // ロックを外す
        holder.unlockCanvasAndPost(drawCanvas);
    }
}
