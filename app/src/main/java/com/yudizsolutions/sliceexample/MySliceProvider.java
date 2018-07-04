package com.yudizsolutions.sliceexample;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;

import static com.yudizsolutions.sliceexample.MainActivity.getTemperatureString;
import static com.yudizsolutions.sliceexample.MainActivity.sTemperature;
import static com.yudizsolutions.sliceexample.MyBroadcastReceiver.ACTION_CHANGE_TEMP;
import static com.yudizsolutions.sliceexample.MyBroadcastReceiver.EXTRA_TEMP_VALUE;


public class MySliceProvider extends SliceProvider {
    private Context context;
    private static int sReqCode = 0;

    @Override
    public boolean onCreateSliceProvider() {
        context = getContext();
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Slice onBindSlice(Uri sliceUri) {

        switch (sliceUri.getPath()) {
            case "/temperature":

                return createTemperatureSlice(sliceUri);

        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Slice createTemperatureSlice(Uri sliceUri) {
        // Define the actions used in this slice
        SliceAction tempUp = new SliceAction(getChangeTempIntent(sTemperature + 1),
                IconCompat.createWithResource(context, R.drawable.up_arrow).toIcon(),
                "Increase temperature");
        SliceAction tempDown = new SliceAction(getChangeTempIntent(sTemperature - 1),
                IconCompat.createWithResource(context, R.drawable.down_arrow).toIcon(),
                "Decrease temperature");

        // Construct our parent builder
        ListBuilder listBuilder = new ListBuilder(context, sliceUri);

        // Construct the builder for the row
        ListBuilder.RowBuilder temperatureRow = new ListBuilder.RowBuilder(listBuilder);

        // Set title
        temperatureRow.setTitle(getTemperatureString(context));

        // Add the actions to appear at the end of the row
        temperatureRow.addEndItem(tempDown);
        temperatureRow.addEndItem(tempUp);

        // Set the primary action; this will activate when the row is tapped
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), sliceUri.hashCode(),
                intent, 0);
        SliceAction openTempActivity = new SliceAction(pendingIntent,
                IconCompat.createWithResource(context, R.mipmap.ic_launcher).toIcon(),
                "Temperature controls");
        temperatureRow.setPrimaryAction(openTempActivity);

        // Add the row to the parent builder
        listBuilder.addRow(temperatureRow);

        // Build the slice
        return listBuilder.build();


    }

    private PendingIntent getChangeTempIntent(int value) {
        Intent intent = new Intent(ACTION_CHANGE_TEMP);
        intent.setClass(context, MyBroadcastReceiver.class);
        intent.putExtra(EXTRA_TEMP_VALUE, value);
        return PendingIntent.getBroadcast(getContext(), sReqCode++, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public static Uri getUri(Context context, String path) {
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(context.getPackageName())
                .appendPath(path)
                .build();
    }
}