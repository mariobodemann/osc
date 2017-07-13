package net.karmacoder.osc;

import android.app.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.AdapterView.*;
import android.widget.*;
import android.view.*;
import android.bluetooth.*;
import android.content.*;
import android.bluetooth.le.*;
import java.util.*;

import static android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.net.wifi.*;
import android.net.*;
import okhttp3.*;
import java.io.*;
import android.preference.*;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class MainActivity extends Activity {

  private static final int ACTIVITY_RESULT_BLUETOOTH_REQUESTED = 1;
  private static final int SCAN_PERIOD = 10_000;

  private final LeScanCallback scanCallback = new LeScanCallback(){
    @Override
    public void onLeScan(BluetoothDevice result, int p2, byte[] p3) {
      recyclerViewAdapter.addDevice(result);
    }
  };

  private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
    @Override
    public void onConnectionStateChange(
      BluetoothGatt gatt, 
      int status,
      int newState) {
      info("Bluetooth Status: " + status  + ", new State: " + newState);
      if(newState == BluetoothProfile.STATE_CONNECTED) {
        info("Connected to Bluetooth.");
      } else if(newState == BluetoothProfile.STATE_DISCONNECTED) {
        info("Disconnect from Bluetooth.");
      } else {
        info("Some other Bluetooth connection state changed ...");
      }
    }
  };

  private RecyclerView recyclerView;
  private TextView infoText;
  private ScrollView infoTextScrollView;
  private BluetoothScanningAdapter recyclerViewAdapter;
  private BluetoothAdapter bluetoothAdapter;
  private Handler handler = new Handler();
  private CameraService service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    setupViews();
    setupBluetooth();
    setupRetrofit();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == ACTIVITY_RESULT_BLUETOOTH_REQUESTED) {
      info("Bluetooth enable request resulted in code " + resultCode);
    }
  }

  private void setupViews() {
    recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
    recyclerViewAdapter = new BluetoothScanningAdapter(
      this,
      new BluetoothScanningAdapter.Listener() {
        @Override
        public void onDeviceClicked(BluetoothDevice device) {
          info("Bluetooth device " + device.getName() + "@" + device.getAddress() + ", connecting ...");

          if(device.getBondState() == BluetoothDevice.BOND_BONDED) {
            info("Bluetooth device is already paired. Initiating Wifi Connection.");
          } else {
            device.connectGatt(
              MainActivity.this,
              false,
              gattCallback
            );
          }
        }
      }
    );

    recyclerView.setAdapter(recyclerViewAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    infoText = (TextView)findViewById(R.id.information);
    infoTextScrollView = (ScrollView)findViewById(R.id.information_scroll);

    findViewById(R.id.connect_bluetooth).setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
          startBluetoothScanning();
        }
      });

    findViewById(R.id.connect_wifi).setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
          connectWifi();
        }
      });

    findViewById(R.id.camera_status).setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
          cameraStatus();
        }
      }
    );

    findViewById(R.id.camera_info).setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
          cameraInfo();
        }
      }
    );

    findViewById(R.id.camera_command).setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
          cameraCommand();
        }
      }
    );
  }

  private void setupBluetooth() {
    final BluetoothManager bluetoothManager =
      (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    bluetoothAdapter = bluetoothManager.getAdapter();

    if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
      Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBtIntent, ACTIVITY_RESULT_BLUETOOTH_REQUESTED);
    } else {
      info("Bluetooth activated");      
    }
  }

  private void startBluetoothScanning() {
    info("Bluetooth Scanning started");
    recyclerViewAdapter.reset();

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          bluetoothAdapter.stopLeScan(scanCallback);
          info("Bluetooth Scanning ended");
        }
      }, SCAN_PERIOD);

    bluetoothAdapter.startLeScan(scanCallback);
  }

  private void setupRetrofit() {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("http://192.168.43.1:6624")
      .addConverterFactory(MoshiConverterFactory.create())
      .build();

    service = retrofit.create(CameraService.class);
  }

  private void connectWifi() {
    info("Connecting to WiFi");

    final WifiManager manager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    if(!manager.isWifiEnabled()) {
      info("Enabling WiFi");
      manager.setWifiEnabled(true);
    }

    if(!manager.startScan()) {
      info("Could not start a WiFi scan ...");
    }

    final List<android.net.wifi.ScanResult> list = manager.getScanResults();
    for(final android.net.wifi.ScanResult result : list) {
      if(result.SSID != null 
         && result.SSID.startsWith("LGR105")
         && result.SSID.endsWith(".OSC")
         ) {
        info("Found WiFi candidate: " + result.capabilities); 

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String networkSSID = result.SSID;
        String networkPass = prefs.getString("wifipass", "");

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";      
        conf.preSharedKey = "\"" + networkPass + "\"";
        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

        final int networkId = manager.addNetwork(conf);
        if(networkId != -1) {
          manager.disconnect();
          manager.enableNetwork(networkId, true);
          manager.saveConfiguration();
        }

        info("WiFi: Connected!");
      }
    }
  }

  private void cameraStatus() {
    info("Printing camera status");

    service.state().enqueue(
      new retrofit2.Callback<CameraService.State>(){
        @Override 
        public void onResponse(
          retrofit2.Call<CameraService.State> call, 
          retrofit2.Response<CameraService.State> response) {
          if(!response.isSuccessful()) {
            info("Camera: Unexpected response code " + response);
          } else {
            info("Camera Response: " + response.body());
          }
        }

        @Override 
        public void onFailure(
          retrofit2.Call<CameraService.State> call, 
          Throwable t) {
          info("Could not print status", t);
        }
      }
    );
  }

  private void cameraInfo() {
    info("Printing camera info");

    service.info().enqueue(
      new retrofit2.Callback<CameraService.Info>(){
        @Override 
        public void onResponse(
          retrofit2.Call<CameraService.Info> call, 
          retrofit2.Response<CameraService.Info> response) {
          if(!response.isSuccessful()) {
            info("Camera: Unexpected response code " + response);
          } else {
            info("Camera Response: " + response.body());
          }
        }

        @Override 
        public void onFailure(
          retrofit2.Call<CameraService.Info> call, 
          Throwable t) {
          info("Could not print status", t);
        }
      }
    );
  }

  private void cameraCommand() {
    info("Preparing command usage");

    final Map<String, CameraService.Command> commands = new HashMap<String, CameraService.Command>();   
    CameraService.ListFileCommand.Parameter listParameter = new CameraService.ListFileCommand.Parameter();
    commands.put("list all files", new CameraService.ListFileCommand(listParameter));
    
    final CharSequence[] names = getCommandNames(commands);

    new AlertDialog.Builder(this)
      .setTitle("Execute Commands")
      .setItems(
      names,
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          CameraService.Command c = commands.get(names[id]);
          executeCommand(c);
        }
      }
    )
      .show();
  }
 
  private void executeCommand(CameraService.Command c) {
    service.commandExecute((CameraService.ListFileCommand)c).enqueue(
      new retrofit2.Callback<CameraService.CommandResponse>() {
        @Override 
        public void onResponse(
          retrofit2.Call<CameraService.CommandResponse> call, 
          retrofit2.Response<CameraService.CommandResponse> response) {
          if(!response.isSuccessful()) {
            info("Camera: Unexpected response code " + response);
          } else {
            info("Camera Response: " + response.body());
          }
        }

        @Override 
        public void onFailure(
          retrofit2.Call<CameraService.CommandResponse> call, 
          Throwable t) {
          info("Could not print status", t);
        }
      }
    );
  }

  private CharSequence[] getCommandNames(Map<String, CameraService.Command> commands) {
    final CharSequence[] names = new CharSequence[commands.size()];
    int i = 0;
    for(final String key : commands.keySet()) {
      names[i++] = key;
    }
    return names;
  }

  private void info(CharSequence message, Throwable t) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    info(message + System.lineSeparator() + System.lineSeparator() + sw.toString());
  }

  private void info(final CharSequence message) {
    handler.post(new Runnable() {
        @Override
        public void run() {
          final String previous = infoText.getText().toString();
          final String next = message + "\n";
          infoText.setText(previous + next);
          handler.post(new Runnable() {
              @Override
              public void run() {
                infoTextScrollView.scrollTo(0, infoTextScrollView.getHeight());
              }
            }
          );
        }
      }
    );
  }
}
