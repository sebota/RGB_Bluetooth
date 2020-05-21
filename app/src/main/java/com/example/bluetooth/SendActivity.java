package com.example.bluetooth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;

public class SendActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        final GlobalClass global = ((GlobalClass)getApplicationContext());

        OutputStream send = global.getOutputStream();

        try {
            send.write('2'); //test
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
