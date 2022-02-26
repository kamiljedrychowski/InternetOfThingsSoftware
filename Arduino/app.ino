#include "DHT.h"
#include "LiquidCrystal.h"

DHT dht(12, DHT11);
LiquidCrystal lcd(9, 8, 5, 4, 3, 2);

void showOnLcd(float temp, float hum)
{
    lcd.setCursor(0, 0);
    lcd.print("T ");
    lcd.print(temp);
    lcd.print(" C");

    lcd.setCursor(0, 2);
    lcd.print("H ");
    lcd.print(hum);
    lcd.print(" %");
}

void serialPrint(float temp, float hum)
{
    char h[5], data[11];
    dtostrf(temp, 5, 2, data);
    dtostrf(hum, 5, 2, h);
    strcat(data, ";");
    strcat(data, h);
    Serial.println(data);
}

void setup()
{
    Serial.begin(9600);
    lcd.begin(16, 2);
    dht.begin();
}

void loop()
{
    delay(1000);

    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();
    if (isnan(humidity) || isnan(temperature))
    {
        Serial.println(F("Failed to read from DHT sensor!"));
        return;
    }

    showOnLcd(temperature, humidity);
    serialPrint(temperature, humidity);
}