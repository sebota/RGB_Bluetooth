package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    Button connect_to_esp;
    BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GlobalClass global = ((GlobalClass)getApplicationContext());
        connect_to_esp = findViewById(R.id.display_devices_button);
        adapter = BluetoothAdapter.getDefaultAdapter();

        //czy dostepny jest interfejs Bluetooth
        if (adapter != null) {
            //czy włączony jest ten interfejs
            if (adapter.isEnabled()) {
                //obsługa interfejsu Bluetooth
            } else {
                //włączenie interfejsu
                Intent BTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(BTintent, 1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Brak interfejsu Bluetooth!",
                    Toast.LENGTH_LONG).show();
        }

        connect_to_esp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(adapter.isEnabled()){
                    Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();

                    if(bondedDevices.size() > 0) {
                        Object[] devices = bondedDevices.toArray();
                        BluetoothDevice device = (BluetoothDevice) devices[1]; //index
                        ParcelUuid[] uuids = device.getUuids();
                        BluetoothSocket socket = null;
                        try {
                            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            global.setOutputStream(socket.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            global.setInStream(socket.getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (socket.isConnected()) {
                            Toast.makeText(getApplicationContext(), "Połączono z ESP",
                                    Toast.LENGTH_LONG).show();
                            run();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Nie połączono z ESP",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Bluetooth jest wyłączony",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void run() {
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }
}

