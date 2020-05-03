package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button display_devices_button;
    BluetoothAdapter adapter;
    TextView devices_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display_devices_button = findViewById(R.id.display_devices_button);
        adapter = BluetoothAdapter.getDefaultAdapter();
        devices_text = findViewById(R.id.devices_text);

        //czy dostepny jest interfejs Bluetooth
        if (adapter != null) {
            //czy włączony jest ten interfejs
            if (adapter.isEnabled()) {
                //obsługa interfejsu Bluetooth
            } else {
                //włączenie interfejsu
                Intent BTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(BTintent, 1);
                //obsługa interfejsu Bluetooth
            }
        } else {
            Toast.makeText(getApplicationContext(), "Brak interfejsu Bluetooth!",
                    Toast.LENGTH_LONG).show();
        }

        display_devices_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.isEnabled()){
                    devices_text.setText("Dostępne urządzenia");
                    Set<BluetoothDevice> devices = adapter.getBondedDevices();
                    for(BluetoothDevice device: devices){
                        devices_text.append("\nUrządzenie " + device.getName());
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Bluetooth jest wyłączony",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

class watek extends Thread
{
    public void run()
    {
    //zakładamy inicjalizację urządzenia Bluetooth
        BluetoothDevice urzadzenie = null; //!!!
        BluetoothSocket gniazdoBT = null;
        try {
    //pobranie gniazda klienta Bluetooth
            gniazdoBT =
                    urzadzenie.createRfcommSocketToServiceRecord(UUID.fromString("usluga"));
        } catch (IOException e) {}
        try {
    //próba połączenia z serwerem Bluetooth
            gniazdoBT.connect();
        } catch (IOException e) {}
    //obsługa połączenia
        try {
    //zamknięcie gniazda Bluetooth
            gniazdoBT.close();
        } catch (IOException e) {}
    }
}
