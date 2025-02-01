#include <Adafruit_NeoPixel.h>
#include "DHT.h"
#include <ArduinoJson.h>
#include <PulseSensorPlayground.h> 

#define PIN 6           // NeoPixel 핀 번호
#define NUMPIXELS 4     // LED 개수
#define GSR_PIN A1      // GSR 센서 핀
#define DHT_PIN 2       // DHT11 온도/습도 센서 핀
#define DHT_TYPE DHT11
#define PULSE_SENSOR_PIN A0 // PulseSensorPlayground 핀

DHT dht(DHT_PIN, DHT_TYPE);
Adafruit_NeoPixel pixels(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);
PulseSensorPlayground pulseSensor; 

// 무지개 색상 루프
void rainbowCycle(uint8_t wait) {
  uint16_t i, j;
  for (j = 0; j < 256 * 5; j++) { 
    for (i = 0; i < NUMPIXELS; i++) {
      pixels.setPixelColor(i, Wheel((i * 256 / NUMPIXELS + j) & 255));
    }
    pixels.show();
    delay(wait);
  }
}

uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if (WheelPos < 85) {
    return pixels.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  } else if (WheelPos < 170) {
    WheelPos -= 85;
    return pixels.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  } else {
    WheelPos -= 170;
    return pixels.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
  }
}

void setColor(uint8_t red, uint8_t green, uint8_t blue) {
  for (int i = 0; i < NUMPIXELS; i++) {
    pixels.setPixelColor(i, pixels.Color(red, green, blue));
  }
  pixels.show();
}

void setup() {
  Serial.begin(9600); 
  pixels.begin();    
  pinMode(GSR_PIN, INPUT);    
  dht.begin();        
  pulseSensor.analogInput(PULSE_SENSOR_PIN);
  pulseSensor.setThreshold(550);
  pulseSensor.begin(); 
}

void loop() {
  // 센서 데이터 읽기
  int pulseValue = pulseSensor.getBeatsPerMinute(); 
  int gsrValue = analogRead(GSR_PIN);  
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();      
  

  if (isnan(temperature) || isnan(humidity)) {
    Serial.println("Failed to read from DHT sensor!");
  } else {
    // JSON 객체 생성 및 센서 값 추가
    StaticJsonDocument<200> jsonDoc;
    jsonDoc["BPM"] = pulseValue;
    jsonDoc["GSR"] = gsrValue;
    jsonDoc["Temperature"] = temperature;
    jsonDoc["Humidity"] = humidity;
    
    // JSON 문자열로 직렬화하여 출력
    String jsonString;
    serializeJson(jsonDoc, jsonString);
    Serial.println(jsonString);
  }

 // NeoPixel 제어 명령 수신 및 처리
  if (Serial.available() > 0) {
    String command = Serial.readStringUntil('\n');  // Node.js에서 명령어 읽기

    if (command == "LIGHT_ON") {
      // 무지개 효과 실행
      rainbowCycle(10);
    } else if (command == "LIGHT_OFF") {
      pixels.clear();
      pixels.show();
    } else if (command == "LIGHT_RED") {
      setColor(255, 0, 0);
      pixels.show();
    } else if (command == "LIGHT_ORANGE") {
      setColor(255, 165, 0);
      pixels.show();
    } else if (command == "LIGHT_YELLOW") {
      setColor(255, 255, 0);
      pixels.show();
    } else if (command == "LIGHT_GREEN") {
      setColor(0, 255, 0);
      pixels.show();
    } else if (command == "LIGHT_BLUE") {
      setColor(0, 0, 255);
      pixels.show();
    } else if (command == "LIGHT_INDIGO") {
      setColor(75, 0, 130);
      pixels.show();
    } else if (command == "LIGHT_PURPLE") {
      setColor(128, 0, 128);
      pixels.show();
    }
    //밝기 조절 -> SET_BRIGHTNESS 숫자
    else if (command.startsWith("SET_BRIGHTNESS")) {
      int brightness = command.substring(15).toInt(); // 15는 "SET_BRIGHTNESS" 길이
      if (brightness >= 0 && brightness <= 255) {
        pixels.setBrightness(brightness);
        pixels.show();
      } 
    }
  }

  // delay(5000);
  delay(10000);
}
