package com.example.jumpstonik.blueshare;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by JUMPSTONIK on 07/03/2018.
 */

public class BlueShareConnection extends Activity implements AdapterView.OnItemClickListener {
    private  static final  String TAG = "Principal";
    BluetoothAdapter mBluetooth;
    Button btnVisible;
    Button btnON_OFF;
    public ArrayList<BluetoothDevice> BTDevicesList = new ArrayList<>();
    public DeviceListAdapter Adaptador;
    ListView lvNewDevices ;
    // Create a BroadcastReceiver for ACTION_FOUND

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blue_share_connection);
        Button btnON_OFF = (Button) findViewById(R.id.btnON_OFF);
        btnVisible = (Button) findViewById(R.id.btnVisible);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        BTDevicesList = new ArrayList<>();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);
        mBluetooth = BluetoothAdapter.getDefaultAdapter();
        lvNewDevices.setOnItemClickListener((AdapterView.OnItemClickListener) BlueShareConnection.this);

    }

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver(){
        /**
         * esta es la funcion encargada de gestionar los cambios del bluetooth y si el mismo
         * se esta o no encendido mostrando los diferentes estados
         * @param context
         * @param intent
         */
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetooth.ACTION_CONNECTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetooth.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReiceve: STATE OFF");
                        break;
                    case  BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReciever1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReiceiver: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReiceiver: STATE TURNING ON");
                        break;

                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver(){
        /**
         * esta funcion es la encargada de gestionar lo que ocurre al momento de permitir la visibi-
         * lidad del dispostivo y los estados que este tenga en el proceso durante y despues de ser
         * visible
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetooth.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, mBluetooth.ERROR);
                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Visivilidad activada");
                        break;
                    case  BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: visibilidad activada. se permite recibir conecciones");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReiceiver2: visibilidad desactivada. no se permite recibir conecciones");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReiceiver2: conectando");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReiceiver2: conectado.");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTDevicesList.add(device);
                System.out.println(BTDevicesList);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                Adaptador = new DeviceListAdapter(context, R.layout.device_adapter_view, BTDevicesList);
                lvNewDevices.setAdapter(Adaptador);
            }
        }
    };
    private  final  BroadcastReceiver mBroadcastReceiver4 =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONED.");
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG, "BroadcastReciver: BOND_BONDING.");
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG, "BroadcastReciever: BOND_NONE");
                }
            }
        }
    };
    /**
    @Override
    protected void  onDestroy(){
        Log.d(TAG, "onDestroy called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }
    */
    public void enableDisableBT(View view){
        if (mBluetooth == null){
            Log.d(TAG, "enableDisableBT: no es compatible el BT");
        }
        if(!mBluetooth.isEnabled()){
            Log.d(TAG, "enabling BT");
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetooth.isEnabled()){
            Log.d(TAG, "enableDisableBT: Desactivar Bluetooth");
            mBluetooth.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }

    public void btnVisible(View view) {
        Log.d(TAG, "btnVisible: hacer el dispositivo visible por 300 segundos");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetooth.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDescubre(View view){
        Log.d(TAG, "btnDescubrir: Buscando dispositivos no conectados");

        if (mBluetooth.isDiscovering()){
            mBluetooth.cancelDiscovery();
            Log.d(TAG, "btnDescubrir: cancelando Busqueda");
            //chequeando los permisos del Bluetooth en manifest
            checkBTPermissions();

            mBluetooth.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!mBluetooth.isDiscovering()){
            checkBTPermissions();
            mBluetooth.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetooth.cancelDiscovery();

        Log.d(TAG, "OnItemClick: has precionado sobre un dispositivo");
        String deviceName = BTDevicesList.get(i).getName();
        String deviceAddres = BTDevicesList.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddres);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Intenta conectar con " + deviceName);
            BTDevicesList.get(i).createBond();
        }

    }

}
