package com.yudizsolutions.sliceexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.math.MathUtils;

import static android.app.slice.Slice.EXTRA_TOGGLE_STATE;
import static com.yudizsolutions.sliceexample.MainActivity.sTemperature;
import static com.yudizsolutions.sliceexample.MainActivity.updateTemperature;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG="receiver";

    public static String ACTION_CHANGE_TEMP = "ACTION_CHANGE_TEMP";
    public static String EXTRA_TEMP_VALUE = "EXTRA_TEMP_VALUE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (ACTION_CHANGE_TEMP.equals(action) && intent.getExtras() != null) {
            int newValue = intent.getExtras().getInt(EXTRA_TEMP_VALUE, sTemperature);
            updateTemperature(context, newValue);
        }
    }
}