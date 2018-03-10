package jp.doumae.graffiticollection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

/**
 * Created by doumae.kazuki on 2018/02/24.
 */

public class BookMenuActivity extends Activity {

    private static final int REQUEST_CODE_GALLERY = 1001;
    private static final int REQUEST_BOOKMENU = 1003;
    public static List<String> images = new ArrayList<String>();

    SQLiteDatabase db;
    GridView bookGallery;
    ArrayList<String> item = new ArrayList<String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photobookmenu);

        // ブック一覧
        bookGallery = (GridView)findViewById(R.id.bookGallery);
        bookGallery.setAdapter(new bookGalleryAdapter(this));
        bookGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DBOpenHelper helper = new DBOpenHelper(getApplicationContext());
                db = helper.getWritableDatabase();
                try {
                    Cursor c = db.rawQuery("SELECT PhotoPass FROM UsePhoto WHERE _BookId = ?;", new String[] {String.valueOf(position+1)});
                    boolean mov = c.moveToFirst();
                    while (mov) {
                        item.add(c.getString(c.getColumnIndex("PhotoPass")));
                        mov = c.moveToNext();
                    }
                    c.close();
                    images.clear();
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.putStringArrayListExtra("images", item);
                    setResult(REQUEST_BOOKMENU,intent);

                }catch (Exception ignored){
                }
                finish();

            }
        });

        // 新規作成ボタン
        ImageButton newCreateBtn = (ImageButton)findViewById(R.id.newCreateButton);
        newCreateBtn.setOnClickListener(galleryOpen);
        // 戻るボタン
        ImageButton returnBtn = (ImageButton)findViewById(R.id.returnButton_pbm);
        returnBtn.setOnClickListener(returnActivity);
    }

    // 端末内画像ギャラリー
    public View.OnClickListener galleryOpen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        }
    };
    // MainActivityへ戻る
    public View.OnClickListener returnActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode ==RESULT_OK){
            //テキスト入力を受け付けるビューを作成します。
            final EditText bookTitle = new EditText(BookMenuActivity.this);
            new AlertDialog.Builder(BookMenuActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("タイトルを入力して下さい")
                    .setView(bookTitle)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            insert(data, bookTitle.getText().toString());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();

        }

        bookGallery.setAdapter(new bookGalleryAdapter(getApplicationContext()));
    }


    private void insert(Intent data , String bookTitle){
        images.clear();
        // 複数選択
        if (data.getClipData() != null) {
            int itemCount = data.getClipData().getItemCount();
            // 画像List作成
            for (int i = 0; i < itemCount; i++) {
                ClipData.Item cUri = data.getClipData().getItemAt(i);
                images.add(cUri.getUri().toString());
            }
        }
        // 一枚選択
        else {
            Uri uri = data.getData();
            images.add(uri.toString());
        }
        DBOpenHelper helper = new DBOpenHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        // BookTableInsert
        ContentValues bookTableItem = new ContentValues();
        bookTableItem.put("CreateDateTime", new Tools().getNowData());
        bookTableItem.put("BookName", bookTitle);
        bookTableItem.put("ThumbnailPass", images.get(0));
        bookTableItem.put("Remarks", "none");

        try {
            db.insert("BookTable", null, bookTableItem);
        } catch (Exception ignored) {
        }
        Cursor c = db.query("BookTable", new String[]{"_BookId"}, null, null, null, null, null);
        int bookId = 0;
        if (c.moveToLast()) {
            bookId = c.getInt(c.getColumnIndex("_BookId"));
        }

        // UsePhoto
        ContentValues usePhotoItem = new ContentValues();
        for (int i = 0; i < images.size(); i++) {
            usePhotoItem.put("PhotoPass", images.get(i));
            usePhotoItem.put("_BookId", String.valueOf(bookId));
            usePhotoItem.put("seq", i + 1);

            try {
                db.insert("UsePhoto", null, usePhotoItem);
            } catch (Exception ignored) {
            }
            usePhotoItem.clear();
        }
        db.close();
        bookGallery.setAdapter(new bookGalleryAdapter(getApplicationContext()));

    }
}
