package com.example.bluetooth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;

public class SendActivity extends AppCompatActivity {
    ImageView rgb_image;
    View rgb_view;
    SeekBar rgb_seekbar;
    Switch pulse_switch;
    Switch cyclic_switch;
    Switch random_switch;
    int r=0, g=0, b=0, intensity=100;
    @SuppressLint("ClickableViewAccessibility")



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        final GlobalClass global = ((GlobalClass)getApplicationContext());
        final OutputStream send = global.getOutputStream();

        rgb_image=findViewById(R.id.rgb_image);
        rgb_view=findViewById(R.id.rgb_view);
        rgb_seekbar=findViewById(R.id.rgb_seekBar);
        random_switch=findViewById(R.id.switch_random);
        pulse_switch=findViewById(R.id.switch_pulse);
        cyclic_switch=findViewById(R.id.switch_cyclic);

        rgb_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensity=progress;
                float r_temp=(float)r*(float)intensity/100;
                float g_temp=(float)g*(float)intensity/100;
                float b_temp=(float)b*(float)intensity/100;
                rgb_view.setBackgroundColor(Color.rgb((int)r_temp, (int)g_temp, (int)b_temp));
                String hex = String.format("#COLOR%02X%02X%02X\n", (int)r_temp, (int)g_temp, (int)b_temp);
                cyclic_switch.setChecked(false);
                random_switch.setChecked(false);
                pulse_switch.setChecked(false);
                try {
                    send.write(hex.getBytes("UTF-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        pulse_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    cyclic_switch.setChecked(false);
                    random_switch.setChecked(false);
                    String hex = "#PULSEON\n";
                    try {
                        send.write(hex.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    String hex = "#PULSEOF\n";
                    try {
                        send.write(hex.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        random_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    cyclic_switch.setChecked(false);
                    pulse_switch.setChecked(false);
                    String hex = "#RNDCLRON\n";
                    try {
                        send.write(hex.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    String hex = "#RNDCLROF\n";
                    try {
                        send.write(hex.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cyclic_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    pulse_switch.setChecked(false);
                    random_switch.setChecked(false);
                    String hex = "#CYCLION\n";
                    try {
                        send.write(hex.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    String hex = "#CYCLIOF\n";
                    try {
                        send.write(hex.getBytes("UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        final Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.colors);
        rgb_image.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    int x_pos=(int)event.getX();
                    int y_pos=(int)event.getY();
                    if (y_pos>=0 && y_pos<bitmap.getHeight() && x_pos>=0 && x_pos<bitmap.getWidth())
                    {
                        int pixel = bitmap.getPixel(x_pos, y_pos);

                        int r_test = Color.red(pixel);
                        int g_test = Color.green(pixel);
                        int b_test = Color.blue(pixel);
                        if (r_test>0 || g_test>0 || b_test>0){
                            r=r_test;
                            g=g_test;
                            b=b_test;
                            float r_temp=(float)r*(float)intensity/100;
                            float g_temp=(float)g*(float)intensity/100;
                            float b_temp=(float)b*(float)intensity/100;
                            rgb_view.setBackgroundColor(Color.rgb((int)r_temp, (int)g_temp, (int)b_temp));
                            String hex = String.format("#COLOR%02X%02X%02X\n", (int)r_temp, (int)g_temp, (int)b_temp);
                            cyclic_switch.setChecked(false);
                            random_switch.setChecked(false);
                            pulse_switch.setChecked(false);
                            try {
                                send.write(hex.getBytes("UTF-8"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return true;
            }
        });
    }
}

