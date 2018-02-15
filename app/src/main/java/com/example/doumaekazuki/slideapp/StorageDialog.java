package com.example.doumaekazuki.slideapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by doumae.kazuki on 2018/02/12.
 */

public class StorageDialog extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(),R.style.DialogMessage)
                .setMessage("保存しました")
                .setPositiveButton("OK", null)
                .show();
    }
}
