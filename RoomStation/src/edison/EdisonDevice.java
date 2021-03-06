package edison;

import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import upm_i2clcd.*;

// import needed for MQQ communication
import org.eclipse.paho.client.mqttv3.*;

/**
 * 
 * @author LSaetta
 *
 *         14/05/2016: introduced ability to reconnect to MQTT broker
 */
public class EdisonDevice implements MqttCallback
{
	// handle configuration
	Config config = new Config();

	// client for sending MQTT messages to the broker
	private MqttClient mqttClient = null;
	private MqttConnectOptions connOpts = new MqttConnectOptions();

	Jhd1313m1 lcd = null;

	upm_grove.GroveLed led = new upm_grove.GroveLed(13);

	// the list of sensors connected, with a uniform interface (ISensor)
	private List<ISensor> sensorList = new ArrayList<ISensor>();

	// to store the list of values at each reading
	private List<Measure> measureList = new ArrayList<Measure>();

	public EdisonDevice()
	{
		// read configuration from config.properties
		config.readConfig();

		// print Configuration, for debug
		config.printConfig();

		// initialize Sensors Objects
		initSensors();

		// LCD
		// lcd = new Jhd1313m1(config.PIN_LCD, 0x3E, 0x62);

		// set once for all connOptions
		connOpts.setCleanSession(true);
		connOpts.setKeepAliveInterval(30);

		// verify is SSL is requested
		if (config.BROKER.contains("ssl"))
		{
			addSSL();
		}

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
	 * Main
	 * @param args
	 */
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
		 * Read, Send message loop
		 */
		while (true)
		{
			printToConsole("");
			printToConsole("Iteration n. " + ++iter);

			//
			// READ value from sensors
			//

			// clean list of measure, for new readings
			device.measureList.clear();

			for (int i = 0; i < device.config.nSensors; i++)
			{
				ISensor is = device.sensorList.get(i);

				// read from sensor and create measure object
				Measure mes = new Measure(is.getType(), is.getUnit(), is.getValue());

				device.measureList.add(mes);
			}

			// Strings to visualize
			// String r1 = "T:" + temperature + ",L:" + light;
			// String r2 = "A.Q.:" + airQuality;

			// on console for debug
			printToConsole(device.measureList);

			// write on the LCD
			// device.lcd.clear();
			// device.lcd.setCursor(0, 0);
			// device.lcd.write(r1);
			// device.lcd.setCursor(1, 0); // second row
			// device.lcd.write(r2);

			/*
			 * Send the msg to the topic
			 */
			try
			{
				// send the message in JSON format

				if (device.isConnected())
				{
					// Build the object representing the message to send
					// it is a type of MqttMessage
					// from configuration: id, type

					DeviceMessage message = new DeviceMessage(device.config, device.measureList);

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
	// Class methods
	//
	private void addSSL()
	{
		try
		{
			SSLContext sslContext = SSLContext.getInstance("SSL");
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			
			KeyStore keyStore = readKeyStore();
			
			trustManagerFactory.init(keyStore);
			
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

			// add to MQTT connOpts
			connOpts.setSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e)
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

			ISensor iSens = null;

			// Initialization of the UPM objects representing sensors
			switch (sDef.getType())
			{
				case "Grove.Temp":
					printToConsole("Adding Sensor Temp");
					iSens = new SensorTemp(sDef.getPin());

					break;
				case "Grove.Light":
					printToConsole("Adding Sensor Light");
					iSens = new SensorLight(sDef.getPin());

					break;
				case "Grove.TP401": // Air Quality sensor v 1.3
					printToConsole("Adding Sensor TP401");
					iSens = new SensorGas(sDef.getPin());

					break;
			}
			// add to list of sensors
			sensorList.add(iSens);
		}
	}

	private static void printToConsole(String r1)
	{
		System.out.println(r1);
	}

	private static void printToConsole(List<Measure> measureList)
	{
		for (int i = 0; i < measureList.size(); i++)
		{
			Measure mes = (Measure) measureList.get(i);

			System.out.println(mes.getType() + ": " + mes.getValue());
		}
	}

	private static void printMsgAndExit(Exception e1)
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
		{
			mqttClient.setCallback(this);

			mqttClient.connect(connOpts);

			System.out.println("Connected...");

			// Subscribe to the input Topic
			// QoS = 0
			mqttClient.subscribe(config.IN_TOPIC, 0);
		}
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

	/**
	 * Methods for MQTT Callbacks
	 */
	@Override
	public void connectionLost(Throwable arg0)
	{
		System.out.println("Connection Lost...");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token)
	{

	}

	@Override
	public void messageArrived(String inputTopic, MqttMessage message) throws Exception
	{
		String sMessage = new String(message.getPayload());

		System.out.println("-------------------------------------------------");
		System.out.println("Topic:" + inputTopic);
		System.out.println("Message: " + sMessage);
		System.out.println("-------------------------------------------------");

		if (sMessage.indexOf("ON") > 0)
			led.on();
		if (sMessage.indexOf("OFF") > 0)
			led.off();
	}

	/**
	 * The function is used to read the KeyStore used in case SSL/TLS is used.
	 * @return
	 */
	private KeyStore readKeyStore()
	{
		KeyStore keystore = null;

		try
		{
			FileInputStream is = new FileInputStream(config.KEYSTORE);

			keystore = KeyStore.getInstance(KeyStore.getDefaultType());

			String keypwd = config.KEYPWD;

			keystore.load(is, keypwd.toCharArray());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return keystore;
	}
}