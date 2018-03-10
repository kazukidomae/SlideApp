package jp.doumae.graffiticollection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by doumae.kazuki on 2018/02/28.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context) {
        super(context, "BookCollection",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookTable("+
        "_BookId integer PRIMARY KEY AUTOINCREMENT,"+
        "CreateDateTime varchar(20),"+
        "BookName varchar(20),"+
        "ThumbnailPass varchar(255),"+
        "Remarks varchar(255))");

        db.execSQL("CREATE TABLE UsePhoto("+
        "PhotoPass varchar(255),"+
        "_BookId varchar(255),"+
        "seq integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
