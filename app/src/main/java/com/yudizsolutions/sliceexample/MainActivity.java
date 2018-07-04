package com.yudizsolutions.sliceexample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.math.MathUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static int sTemperature = 16; // Celsius
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.temperature_title);

        findViewById(R.id.increase_temp).setOnClickListener(this);
        findViewById(R.id.decrease_temp).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTextView.setText(getTemperatureString(getApplicationContext()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.increase_temp:
                updateTemperature(getApplicationContext(), sTemperature + 1);
                break;
            case R.id.decrease_temp:
                updateTemperature(getApplicationContext(), sTemperature - 1);
                break;
        }
        mTextView.setText(getTemperatureString(getApplicationContext()));
    }

    public static String getTemperatureString(Context context) {
        return context.getString(R.string.temp_string, sTemperature);
    }

    public static void updateTemperature(Context context, int newValue) {
        newValue = MathUtils.clamp(newValue, 10, 30); // Lets keep temperatures reasonable

        if (newValue != sTemperature) {
            sTemperature = newValue;

            // Should notify the URI to let any slices that might be displaying know to update.
            Uri uri = MySliceProvider.getUri(context, "temperature");
            context.getContentResolver().notifyChange(uri, null);
        }
    }
}

