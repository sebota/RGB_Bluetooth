#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

int red = 32;
int green = 33;
int blue = 25;
char data = 0;

// setting PWM properties
const int freq = 5000;
const int ledChannel = 0;
const int ledChannel2 = 1;
const int ledChannel3 = 2;
const int resolution = 8;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("RGB Control");
  Serial.println("The device started, now you can pair it with bluetooth!");

  // configure LED PWM functionalitites
  ledcSetup(ledChannel, freq, resolution);
  ledcSetup(ledChannel2, freq, resolution);
  ledcSetup(ledChannel3, freq, resolution);
  
  // attach the channel to the GPIO to be controlled
  ledcAttachPin(red, ledChannel);
  ledcAttachPin(green, ledChannel2);
  ledcAttachPin(blue, ledChannel3);

  ledcWrite(ledChannel, 255);
  ledcWrite(ledChannel2, 255);
  ledcWrite(ledChannel3, 255);
}

void loop() {
  /*if (Serial.available()) {
    SerialBT.write(Serial.read());
  }
  if (SerialBT.available()) {
    Serial.write(SerialBT.read());
  }
  delay(20);*/
  
  if (SerialBT.available()) {
    
  data = SerialBT.read();

  if(data == '1'){
    ledcWrite(ledChannel, 255);
    ledcWrite(ledChannel2, 0);
    ledcWrite(ledChannel3, 0);
  }
  
  if(data == '2'){
    ledcWrite(ledChannel, 0);
    ledcWrite(ledChannel2, 0);
    ledcWrite(ledChannel3, 0);
  }
  
  if(data == '3'){
    ledcWrite(ledChannel, 0);
    ledcWrite(ledChannel2, 0);
    ledcWrite(ledChannel3, 255);
  }
  
  if(data == '4'){
    ledcWrite(ledChannel, 0);
    ledcWrite(ledChannel2, 255);
    ledcWrite(ledChannel3, 0);
  }
  
  }
}
