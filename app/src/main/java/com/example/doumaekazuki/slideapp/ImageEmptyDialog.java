package com.example.doumaekazuki.slideapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by doumae.kazuki on 2018/02/10.
 */

public class ImageEmptyDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(),R.style.DialogMessage)
                .setMessage("写真がセットされていません")
                .setPositiveButton("OK", null)
                .show();
    }
}
