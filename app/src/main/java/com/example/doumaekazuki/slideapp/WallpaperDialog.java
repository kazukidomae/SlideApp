package com.example.doumaekazuki.slideapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import java.io.IOException;

/**
 * Created by doumae.kazuki on 2017/12/12.
 */

public class WallpaperDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(),R.style.DialogMessage)
                .setMessage("壁紙に設定しました")
                .setPositiveButton("OK", null)
                .show();
    }
}
