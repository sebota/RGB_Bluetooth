package com.example.bluetooth;

import android.app.Application;

import java.io.InputStream;
import java.io.OutputStream;

public class GlobalClass extends Application {
    private OutputStream outputStream;
    private InputStream inStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream str) {
        outputStream = str;
    }

    public InputStream getInStream() {
        return inStream;
    }

    public void setInStream(InputStream str) {
        inStream = str;
    }

}
