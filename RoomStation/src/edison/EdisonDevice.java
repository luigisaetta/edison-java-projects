package edison;

import java.util.ArrayList;
import java.util.List;

import upm_i2clcd.*;

// import needed for MQQ communication
import org.eclipse.paho.client.mqttv3.*;

/**
 * 
 * @author LSaetta
 *
 *         14/05/2016: introduced ability to reconnect to MQTT broker
 */
public class EdisonDevice
{
	// handle configuration
	Config config = new Config();

	// client for sending MQTT messages to the broker
	private MqttClient mqttClient = null;
	private MqttConnectOptions connOpts = new MqttConnectOptions();

	// Object representing sensors (UPM)
	int nSensors;

	ISensor tempSensor = null;
	ISensor lightSensor = null;
	ISensor aqsSensor = null;
	
	Jhd1313m1 lcd = null;

	private List<ISensor> sensorList = new ArrayList<ISensor>();
	
	public EdisonDevice()
	{
		// read configuration from config.properties
		config.readConfig();

		config.printConfig();

		// initialize Sensors Objects
		initSensors();

		// LCD
		lcd = new Jhd1313m1(config.PIN_LCD, 0x3E, 0x62);

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

	/**
	 * Initialize Sensors Definition
	 * 
	 */
	private void initSensors()
	{
		for (int i = 0; i < config.nSensors; i++)
		{
			SensorDef sDef = config.lSensorsDef.get(i);

			// Initialization of the UPM objects representing sensors
			switch (sDef.getType())
			{
				case "TEMP":
					tempSensor = new SensorTemp(sDef.getPin());
					
					sensorList.add(tempSensor);
					
					break;
				case "LIGHT":
					lightSensor = new SensorLight(sDef.getPin());
					
					sensorList.add(lightSensor);
					
					break;
				case "GAS": // Air Quality sensor v 1.3
					aqsSensor = new SensorGas(sDef.getPin());
					
					sensorList.add(aqsSensor);
					
					break;
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Starting program...");

		EdisonDevice device = new EdisonDevice();

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
			printToConsole("Iteration n. " + iter++);

			//
			// READ value from sensors
			//
			String temperature = device.tempSensor.getValue();
			String light = device.lightSensor.getValue();
			String airQuality = device.aqsSensor.getValue();

			// Strings to visualize
			String r1 = "T:" + temperature + ",L:" + light;
			String r2 = "A.Q.:" + airQuality;

			// on console for debug
			printToConsole(r1);
			printToConsole(r2);

			// write on the LCD
			device.lcd.clear();
			device.lcd.setCursor(0, 0);
			device.lcd.write(r1);
			device.lcd.setCursor(1, 0); // second row
			device.lcd.write(r2);

			/*
			 * Send the msg to the topic
			 */
			try
			{
				// send the message in JSON format {id: thunder, temp: 33,
				// light: 101, airQuality: 55}

				if (device.isConnected())
				{
					// Build the object representing the message to send
					// it is a type of MqttMessage
					// from configuration: id, type
					DeviceMessage message = new DeviceMessage(device.config, temperature, light,
							airQuality);

					// publish message to the topic
					device.publish(device.config.TOPIC, message);
				} else
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

	//
	// Class method
	//
	private static void printToConsole(String r1)
	{
		System.out.println(r1);
	}

	private static void printMsgAndExit(MqttException e1)
	{
		e1.printStackTrace();

		printToConsole("Exiting!");
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

	/**
	 * Verify if the device is connected to the MQTT Broker
	 * 
	 * @return true if connected
	 */
	public boolean isConnected()
	{
		if (mqttClient != null)
			return mqttClient.isConnected();
		else
			return false;
	}

	/**
	 * publish the message to the MQTT broker
	 * 
	 * @param theTopic
	 * @param theMsg
	 * @throws MqttException
	 */
	public void publish(String theTopic, MqttMessage theMsg) throws MqttException
	{
		if (mqttClient != null)
			mqttClient.publish(theTopic, theMsg);
	}

	/**
	 * sleep for time msec
	 * 
	 * @param time
	 *            (msec)
	 */
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