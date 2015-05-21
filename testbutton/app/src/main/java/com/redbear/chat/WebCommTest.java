package com.redbear.chat;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by makoto on 5/17/15.
 */
public class WebCommTest extends Activity{
    private TextView tv = null;
    private Button commbtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webcomm);

        tv = (TextView) findViewById(R.id.textView);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

		commbtn = (Button) findViewById(R.id.comm);

        Intent intent = getIntent();

    }

}
