package jp.doumae.graffiticollection;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.pm.ActivityInfoCompat;
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
import java.util.jar.Manifest;


//import static jp.doumae.graffiticollection.R.layout.paint;

/**
 * Created by doumae.kazuki on 2018/01/16.
 */

public class EditSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;

    Paint mPaint;
    Path mPath;

    Bitmap originBitmap;
    Canvas originCanvas;

    Bitmap editBitmap;
    Canvas drawCanvas;

    String imagePass;
    String afterChangeImagePass;

    Uri uri;

    int sizeX,sizeY;
    int r,g,b,sw; // 描画線設定

    // 途中経過
    private Deque<Path> mUndoStack = new ArrayDeque<Path>();
    List<Path> pathList;
    List<Paint> paintList;

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
        editBitmap.recycle();
    }

    private void clearLastDrawBitmap() {
        // Bitmap変換

        System.out.println("aaa"+imagePass);

        uri = Uri.parse(imagePass);
        try {
            originBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
            editBitmap = Bitmap.createScaledBitmap(originBitmap.copy(Bitmap.Config.ARGB_8888,true), sizeX, sizeY, true);
            originBitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        originCanvas = new Canvas(editBitmap);
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
        originCanvas.drawPath(mPath, mPaint);
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
                originCanvas.drawPath(pathList.get(i), paintList.get(i));
            }
            holder.unlockCanvasAndPost(drawCanvas);
        }
    }

    // 画像描画
    private void drawingImage (){
        // 位置調整
        drawCanvas.translate((drawCanvas.getWidth()-editBitmap.getWidth())/2,(drawCanvas.getHeight()-editBitmap.getHeight())/2);
        drawCanvas.drawBitmap(editBitmap,0,0,null);
    }

    // 画像保存
    public String storage(){

        String message;

        if(ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // ストレージ許可
            File dataDir = new File(Environment.getExternalStorageDirectory(), "GraffitiCollection");
            dataDir.mkdirs();
            afterChangeImagePass = new Tools().getNowData()+"jpg";
            File filePath = new File(dataDir,afterChangeImagePass);
            OutputStream os= null;
            try {
                os = new FileOutputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            editBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA, filePath.getAbsolutePath());
                getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
            message = "保存しました";
        }
        else {
            message =  "アプリを許可して下さい";
        }

        return message;
    }

    // 編集後画像取得
    public String[] justBeforeImage(){
        String re[] = new String[2];

        // 写真保存
        String message = storage();
        if(message.equals("保存しました")){
            // 直前に保存した写真取得
            File file = new File("storage/emulated/0/GraffitiCollection/"+afterChangeImagePass);
            re[0] = "写真を差し替えました。";
            re[1]= Uri.fromFile(file).toString();
        }
        else {
            re[0] = "アプリを許可して下さい" ;
            re[1] = imagePass;
        }
        return re;
    }

}
