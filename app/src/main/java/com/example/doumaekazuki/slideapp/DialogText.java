package com.example.doumaekazuki.slideapp;

import android.os.Bundle;

/**
 * Created by doumae.kazuki on 2018/02/19.
 */

public class DialogText {

    public Bundle dialogMessage (String Message){
        Bundle args = new Bundle();
        args.putString("dialogMessage",Message);
        return args;
    }
}
