package jp.doumae.graffiticollection;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by doumae.kazuki on 2017/11/08.
 */

public class PageFragment extends Fragment{

    String mValue;
    Uri uri = null;
    Bitmap bitmap = null;

    static PageFragment newInstance(String value) {
        PageFragment pf = new PageFragment();
        Bundle args = new Bundle();
        args.putString("image", value);
        pf.setArguments(args);
        return pf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValue = getArguments() != null ? getArguments().getString("image") : "test";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page,container,false);
        ImageView iv = view.findViewById(R.id.slidearea);

        uri = Uri.parse(mValue);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            //画像サイズ変更
            Matrix matrix = new Matrix();
            matrix.setScale(0.5f,0.5f);

            iv.setImageBitmap(Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
}
