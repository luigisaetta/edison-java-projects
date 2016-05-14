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
 * 14/05/2016: introduced ability to reconnect to MQTT broker
 */
public class EdisonDevice
{
	// handle configuration
	Config config = new Config();
			
	// client for sending MQTT messages to the broker
	private MqttClient mqttClient = null;
	
	private MqttConnectOptions connOpts = new MqttConnectOptions();
	
	public EdisonDevice()
	{
		//read configuration from config.properties
		config.readConfig();
				
		// set once or all connOptions
		connOpts.setCleanSession(true);
		
		// Initialize MQTT client
		try
		{
			mqttClient = new MqttClient(config.BROKER, config.CLIENTID);
		} catch (MqttException e)
		{
			printMsgAndExit(e);
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println("Starting program...");
		
		EdisonDevice device = new EdisonDevice();

		// declaration of the UPM objects representing sensors
		GroveTemp tempSensor = new GroveTemp(device.config.PIN_TEMP);
		GroveLight lightSensor = new GroveLight(device.config.PIN_LIGHT);
		// Air Quality sensor v 1.3
		TP401 aqsSensor = new TP401(device.config.PIN_AQS);
		
		// LCD
		Jhd1313m1 lcd = new Jhd1313m1(device.config.PIN_LCD, 0x3E, 0x62);

		try
		{
			System.out.println("Connecting to broker... ");

			device.connect();

		} catch (MqttException e1)
		{
			// unrecoverable error, EXIT!
			printMsgAndExit(e1);
		}

		// this must be long
		long iter = 0;

		/**
		 * Read, send msg loop
		 */
		while (true)
		{
			System.out.println("Iteration n. " + iter++);
			
			//
			// READ value from sensors
			//
			float temp = tempSensor.value();
			float light = lightSensor.raw_value();
			int airQuality = aqsSensor.getSample();

			// Strings to visualize
			String r1 = "T:" + temp + ",L:" + light;
			String r2 = "A.Q.:" + airQuality;
			
			// on console for debug
			System.out.println(r1);
			System.out.println(r2);
			
			// write on the LCD
			lcd.clear();
			lcd.setCursor(0,0);
		    lcd.write(r1);
		    lcd.setCursor(1,0); // second row
		    lcd.write(r2);
		    
		    /*
		     * Send the msg to the topic
		     */
			try
			{
				// send the message in JSON format {id: thunder, temp: 33,
				// light: 101, airQuality: 55}

				// Build the object representing the msg to send
				// it is a type of MqttMessage
				DeviceMessage message = new DeviceMessage(device.config, temp, light, airQuality);

				// publish msg to the topic
				if (device.isConnected())
				{
					device.publish(device.config.TOPIC, message);
				}
				else
				{
					System.out.println("Not connected... retry connection...");
					
					// retry connection
					device.connect();
					
					// will send msg in next iteration
				}

			} catch (MqttException e1)
			{
				e1.printStackTrace();
			}

			// wait till the next operation cycle
			device.sleep(device.config.SLEEP_TIME);
		}
	}

	private static void printMsgAndExit(MqttException e1)
	{
		e1.printStackTrace();

		System.out.println("Exiting!");
		System.exit(-1);
	}
	
	/*
	 * Connect to MQTT broker
	 */
	public void connect() throws MqttException
	{
		if (mqttClient != null)
			mqttClient.connect(connOpts);
		
		System.out.println("Connected...");
	}
	
	/*
	 * Verify if the device is connected to the MQTT Broker
	 */
	public boolean isConnected()
	{
		if (mqttClient != null)
		   return mqttClient.isConnected();
		else
			return false;
	}
	
	public void publish(String theTopic, MqttMessage theMsg) throws MqttException
	{
		if (mqttClient != null)
			mqttClient.publish(theTopic, theMsg);
	}
	
	public void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}