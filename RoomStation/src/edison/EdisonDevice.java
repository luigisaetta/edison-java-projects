package edison;

import upm_grove.*;
import upm_gas.*;
import upm_i2clcd.*;

// import needed for MQQ communication
import org.eclipse.paho.client.mqttv3.*;

/**
 * 
 * @author LSaetta
 *
 */
public class EdisonDevice
{
	public static void main(String[] args)
	{
		// handle configuration
		// now hard coded in Config.java
		Config config = new Config();
		
		//read configuration from config.properties
		config.readConfig();

		System.out.println("Starting program...");

		// declaration of the UPM objects representing sensors
		GroveTemp tempSensor = new GroveTemp(config.PIN_TEMP);
		GroveLight lightSensor = new GroveLight(config.PIN_LIGHT);
		// Air Quality sensor v 1.3
		TP401 aqsSensor = new TP401(config.PIN_AQS);
		
		// LCD
		Jhd1313m1 lcd = new Jhd1313m1(config.PIN_LCD, 0x3E, 0x62);

		// client for sending MQTT messages to the broker
		MqttClient mqttClient = null;

		try
		{
			// Initialize MQTT client
			mqttClient = new MqttClient(config.BROKER, config.CLIENTID);

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("Connecting to broker... ");

			mqttClient.connect(connOpts);

			System.out.println("Connected!");

		} catch (MqttException e1)
		{
			// unrecoverable error, EXIT!
			e1.printStackTrace();

			System.out.println("Exiting!");
			System.exit(-1);
		}

		int i = 0;

		while (true)
		{
			// READ value from sensors
			float temp = tempSensor.value();
			float light = lightSensor.raw_value();
			int airQuality = aqsSensor.getSample();

			// console messages for debug
			System.out.println("Iteration n. " + i++);
			System.out.println("Temperature is : " + temp + ", Light is: " + light
					+ ", Air Quality is: " + airQuality);
			
			// write on the LCD
			lcd.setCursor(0,0);
		    lcd.write("T:" + temp + ",L:" + light);
		    lcd.setCursor(1,0); // second row
		    lcd.write("AQ:" + airQuality);
		    
			// Build the object representing the msg to send
			DeviceMessage msg = new DeviceMessage(config.CLIENTID, config.TYPE, temp, light, airQuality);

			try
			{
				// send the message in JSON format {id: thunder, temp: 33,
				// light: 101, airQuality: 55}

				// create the msg
				MqttMessage message = new MqttMessage(msg.toJSONString().getBytes());
				message.setQos(config.QOS);

				// publish msg to the topic
				if (mqttClient.isConnected())
				{
					mqttClient.publish(config.TOPIC, message);
				}

			} catch (MqttException e1)
			{
				e1.printStackTrace();
			}

			// wait till the next operation cycle
			try
			{
				Thread.sleep(config.SLEEP_TIME);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}