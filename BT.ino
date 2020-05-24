#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

int red = 32;
int green = 33;
int blue = 25;
String data = "";

int red_value = 255;
int green_value = 255;
int blue_value = 255;

int red_value_p = 255;
int green_value_p = 255;
int blue_value_p = 255;

int red_value_c = 0;
int green_value_c = 0;
int blue_value_c = 0;

int map_red_value_c = 0;
int map_green_value_c = 0;
int map_blue_value_c = 0;

float intensity = 0;

bool pulse_flag = false;
bool rand_flag = false;
bool cyclic_flag = false;


//int red_tab[] = {0, 255, 255, 255, 0 , 0};
//int green_tab[] = {0, 0, 0, 255, 255, 255};
//int blue_tab[] = {255, 255, 0, 0, 0, 255};

int *rgb[] = {&green_value_c, &red_value_c, &blue_value_c};
bool sign = true;
int idx = 0;

const int freq = 5000;
const int ledChannel_red = 0;
const int ledChannel_green = 1;
const int ledChannel_blue = 2;
const int resolution = 8;

void setup() {
  
  Serial.begin(115200);
  SerialBT.begin("RGB Control");
  //Serial.println("The device started, now you can pair it with bluetooth!");
  
  ledcSetup(ledChannel_red, freq, resolution);
  ledcSetup(ledChannel_green, freq, resolution);
  ledcSetup(ledChannel_blue, freq, resolution);
  
  ledcAttachPin(red, ledChannel_red);
  ledcAttachPin(green, ledChannel_green);
  ledcAttachPin(blue, ledChannel_blue);

  ledcWrite(ledChannel_red, red_value);
  ledcWrite(ledChannel_green, green_value);
  ledcWrite(ledChannel_blue, blue_value);
}

void loop() {

  if (SerialBT.available()) {
    data = SerialBT.readString();
    Serial.println(data);
    check_data();
  }
  
  if(cyclic_flag){
    multicolor(*rgb[idx]);

    map_red_value_c = map(red_value_c, 0, 255, 255, 0);
    map_green_value_c = map(green_value_c, 0, 255, 255, 0);
    map_blue_value_c = map(blue_value_c, 0, 255, 255, 0);
    
    ledcWrite(ledChannel_red, map_red_value_c);
    ledcWrite(ledChannel_green, map_green_value_c);
    ledcWrite(ledChannel_blue, map_blue_value_c);
    delay(20);
  }

  if(pulse_flag){
    red_value_p = map(red_value, 255, 0, 0, 255);
    green_value_p = map(green_value, 255, 0, 0, 255);
    blue_value_p = map(blue_value, 255, 0, 0, 255);

    red_value_p *= intensity;
    green_value_p *= intensity;
    blue_value_p *= intensity;

    red_value_p = map(red_value_p, 0, 255, 255, 0);
    green_value_p = map(green_value_p, 0, 255, 255, 0);
    blue_value_p = map(blue_value_p, 0, 255, 255, 0);
    
    ledcWrite(ledChannel_red, red_value_p);
    ledcWrite(ledChannel_green, green_value_p);
    ledcWrite(ledChannel_blue, blue_value_p);
    delay(20); 
    intensity += 0.006; 
    
    if(intensity >= 1)
      intensity = 0; 
  }

  if(rand_flag){
    ledcWrite(ledChannel_red, random(256));
    ledcWrite(ledChannel_green, random(256));
    ledcWrite(ledChannel_blue, random(256));
    
    for (int i = 0; i <= 70; i++){
      delay(10);
      
      if (SerialBT.available())
        data = SerialBT.readString();
        
      if(data.substring(7, 9) == "OF"){
        rand_flag = false;
        ledcWrite(ledChannel_red, red_value);
        ledcWrite(ledChannel_green, green_value);
        ledcWrite(ledChannel_blue, blue_value); 
      }
    }
  }
}

void multicolor(int &tmp)
{
  if (sign) {
    tmp++;
  }
  else {
    tmp--;
  }
  if (tmp == 0 || tmp == 255)
  {
    sign = ! sign;
    if (idx == 2) {
      idx = 0;
    }
    else {
      idx++;
    }
  }
}

void check_data(){
  data.trim();
  
  if(data.substring(0, 6) == "#COLOR")
  {
    long hex = (long) strtol( &data[6], NULL, 16);
    red_value = hex >> 16;
    red_value = map(red_value, 0, 255, 255, 0);
    
    green_value = hex >> 8 & 0xFF;
    green_value = map(green_value, 0, 255, 255, 0);
    
    blue_value = hex & 0xFF;
    blue_value = map(blue_value, 0, 255, 255, 0);
    
    ledcWrite(ledChannel_red, red_value);
    ledcWrite(ledChannel_green, green_value);
    ledcWrite(ledChannel_blue, blue_value);
  }

  if(data.substring(0, 6) == "#CYCLI")
  {
    red_value_c = 255;
    green_value_c = 0;
    blue_value_c = 0;
    sign = true;
    idx = 0;
    
    if(data.substring(6, 8) == "ON")
      cyclic_flag = true;
          
    if(data.substring(6, 8) == "OF"){
      cyclic_flag = false;
      
      ledcWrite(ledChannel_red, red_value);
      ledcWrite(ledChannel_green, green_value);
      ledcWrite(ledChannel_blue, blue_value); 
    }
  }
  
  if(data.substring(0, 6) == "#PULSE")
  {
    if(data.substring(6, 8) == "ON")
      pulse_flag = true;

    if(data.substring(6, 8) == "OF"){
      pulse_flag = false;
      ledcWrite(ledChannel_red, red_value);
      ledcWrite(ledChannel_green, green_value);
      ledcWrite(ledChannel_blue, blue_value);
    }     
  }
  
  if(data.substring(0, 7) == "#RNDCLR")
  {
    if(data.substring(7, 9) == "ON")
      rand_flag = true;

    if(data.substring(7, 9) == "OF"){
      rand_flag = false;
          
      ledcWrite(ledChannel_red, red_value);
      ledcWrite(ledChannel_green, green_value);
      ledcWrite(ledChannel_blue, blue_value); 
    }
  }    
}
