package jp.doumae.graffiticollection;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by doumae.kazuki on 2018/02/19.
 */

public class DialogStationery extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(),R.style.DialogMessage)
                .setMessage(getArguments().getString("dialogMessage"))
                .setPositiveButton("OK", null)
                .show();
    }
}
