package jp.doumae.graffiticollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doumae.kazuki on 2018/02/24.
 */

public class bookGalleryAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    List<String> item = new ArrayList<String>();

    private static class ViewHolder{
        public ImageView hueImageView;
        public TextView  hueTextView;
    }

    public bookGalleryAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        getBookItem();
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.bookitem, null);
            holder = new ViewHolder();
            holder.hueImageView = (ImageView)convertView.findViewById(R.id.hue_imageview);
            holder.hueTextView = (TextView)convertView.findViewById(R.id.hue_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.hueImageView.setImageResource(R.drawable.photobookicon);
        holder.hueTextView.setText(item.get(position));
        holder.hueImageView.setColorFilter(255);
        return convertView;
    }

    // DBから値取得
    public void getBookItem(){
        DBOpenHelper helper = new DBOpenHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            // メソッド実行
            Cursor c = db.query("BookTable", new String[]{"_BookId", "CreateDateTime", "BookName", "ThumbnailPass", "Remarks"}, null, null, null, null,null);
            boolean mov = c.moveToFirst();
            System.out.println("bbb"+c.getString(c.getColumnIndex("BookName")));
            while (mov) {
                item.add(c.getString(c.getColumnIndex("BookName")));
                mov = c.moveToNext();
            }
            c.close();
        }
        catch (Exception ignored){
        }
    }
}
