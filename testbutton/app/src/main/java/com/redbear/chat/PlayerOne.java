package com.redbear.chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlayerOne extends Activity {
	private final static String TAG = "Player 1";

	public static final String EXTRAS_DEVICE = "EXTRAS_DEVICE";
	private TextView tv = null;
	private EditText et = null;
	private Button lbtn = null;
	private Button rbtn = null;
	private Button cbtn = null;
	private Button sbtn = null;
	private Button mbtn = null;
	private String mDeviceName;
	private String mDeviceAddress;
	private RBLService mBluetoothLeService;
	private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((RBLService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
			} else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				getGattService(mBluetoothLeService.getSupportedGattService());
			} else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
				displayData(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
			}
		}
	};

	private String readTextFromResource(int resourceID)
	{
		InputStream raw = getResources().openRawResource(resourceID);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int i;
		try
		{
			i = raw.read();
			while (i != -1)
			{
				stream.write(i);
				i = raw.read();
			}
			raw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return stream.toString();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.second);

//		tv = (TextView) findViewById(R.id.textView);
//		tv.setMovementMethod(ScrollingMovementMethod.getInstance());
//		tv.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				String str = s.toString();
//				int score = Integer.parseInt(str.substring(start));
//
////				if (count > 1){
////					byte b = 0x64;
////					BluetoothGattCharacteristic characteristic = map
////							.get(RBLService.UUID_BLE_SHIELD_TX);
////					byte[] tx = new byte[1];
////					tx[0] = b;
////					characteristic.setValue(tx);
////					mBluetoothLeService.writeCharacteristic(characteristic);
////				}
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
//		et = (EditText) findViewById(R.id.editText);
		lbtn = (Button) findViewById(R.id.left);
		rbtn = (Button) findViewById(R.id.right);
		cbtn = (Button) findViewById(R.id.start);
		sbtn = (Button) findViewById(R.id.single);
		mbtn = (Button) findViewById(R.id.multi);
		lbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				byte b = 0x61;
				BluetoothGattCharacteristic characteristic = map
						.get(RBLService.UUID_BLE_SHIELD_TX);
				byte[] tx = new byte[1];
				tx[0] = b;
				characteristic.setValue(tx);
				mBluetoothLeService.writeCharacteristic(characteristic);
//				BluetoothGattCharacteristic characteristic = map
//						.get(RBLService.UUID_BLE_SHIELD_TX);
//
//				String str = et.getText().toString();
//				byte b = 0x00;
//				byte[] tmp = str.getBytes();
//				byte[] tx = new byte[tmp.length + 1];
//				tx[0] = b;
//				for (int i = 1; i < tmp.length + 1; i++) {
//					tx[i] = tmp[i - 1];
//				}
//
//				characteristic.setValue(tx);
//				mBluetoothLeService.writeCharacteristic(characteristic);
//
//				et.setText("");
			}
		});

		rbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				byte b = 0x62;
				BluetoothGattCharacteristic characteristic = map
						.get(RBLService.UUID_BLE_SHIELD_TX);
				byte[] tx = new byte[1];
				tx[0] = b;
				characteristic.setValue(tx);
				mBluetoothLeService.writeCharacteristic(characteristic);

			}
		});

		cbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				byte b = 0x63;
				BluetoothGattCharacteristic characteristic = map
						.get(RBLService.UUID_BLE_SHIELD_TX);
				byte[] tx = new byte[1];
				tx[0] = b;
				characteristic.setValue(tx);
				mBluetoothLeService.writeCharacteristic(characteristic);

			}
		});


		sbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				byte b = 0x70;
				BluetoothGattCharacteristic characteristic = map
						.get(RBLService.UUID_BLE_SHIELD_TX);
				byte[] tx = new byte[1];
				tx[0] = b;
				characteristic.setValue(tx);
				mBluetoothLeService.writeCharacteristic(characteristic);

			}
		});


		mbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				byte b = 0x71;
				BluetoothGattCharacteristic characteristic = map
						.get(RBLService.UUID_BLE_SHIELD_TX);
				byte[] tx = new byte[1];
				tx[0] = b;
				characteristic.setValue(tx);
				mBluetoothLeService.writeCharacteristic(characteristic);

			}
		});

//		Button webTest = (Button)findViewById(R.id.webTest);
//		webTest.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent webIntent = new Intent(getApplicationContext(), WebApp.class);
//				startActivity(webIntent);
//			}
//		});
//
//		WebView myWebView = (WebView) findViewById(R.id.third);
//		myWebView.loadDataWithBaseURL("file:///android_res/raw/", null, "raw/index",
//				"UTF-8", null);

		Intent intent = getIntent();

		mDeviceAddress = intent.getStringExtra(Device1.EXTRA_DEVICE_ADDRESS);
		mDeviceName = "Player 1";

		getActionBar().setTitle(mDeviceName);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent gattServiceIntent = new Intent(this, RBLService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService.close();

			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		super.onStop();

		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mBluetoothLeService.disconnect();
		mBluetoothLeService.close();

		System.exit(0);
	}

	private void displayData(byte[] byteArray) {
		if (byteArray != null) {
			String data = new String(byteArray);
			tv.append(data);
			// find the amount we need to scroll. This works by
			// asking the TextView's internal layout for the position
			// of the final line and then subtracting the TextView's height
			final int scrollAmount = tv.getLayout().getLineTop(
					tv.getLineCount())
					- tv.getHeight();
			// if there is no need to scroll, scrollAmount will be <=0
			if (scrollAmount > 0)
				tv.scrollTo(0, scrollAmount);
			else
				tv.scrollTo(0, 0);
		}
	}

	private void getGattService(BluetoothGattService gattService) {
		if (gattService == null)
			return;

		BluetoothGattCharacteristic characteristic = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
		map.put(characteristic.getUuid(), characteristic);

		BluetoothGattCharacteristic characteristicRx = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
		mBluetoothLeService.setCharacteristicNotification(characteristicRx,
				true);
		mBluetoothLeService.readCharacteristic(characteristicRx);
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

		return intentFilter;
	}
}
