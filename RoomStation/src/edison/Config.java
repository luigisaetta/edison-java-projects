package edison;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config
{
	private final static String propFileName = "config.properties";

	//
	// some defaults
	//
	protected String CLIENTID = "thunder10";
	protected String TYPE = "myType";

	// timing
	protected long SLEEP_TIME = 5000;


	// MQTT: normally read from config.properties
	protected String BROKER = "tcp://iotgateway1:1883";
	protected int QOS = 1;
	
	// This is the topic we're sending messages
	protected String TOPIC = CLIENTID + "/msg";
	
	protected String IN_TOPIC = "device/thunder10/in";

	// Sensors
	protected int nSensors;

	protected List<SensorDef> lSensorsDef = new ArrayList<SensorDef>();

	// for TLS
	protected String KEYSTORE;
	protected String KEYPWD;
	
	public void readConfig()
	{
		Properties prop = new Properties();

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null)
		{
			try
			{
				prop.load(inputStream);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			System.out.println("property file '" + propFileName + "' not found in the classpath");

			System.exit(-1);
		}

		// Device Type
		TYPE = prop.getProperty("TYPE");
		BROKER = prop.getProperty("BROKER");
		CLIENTID = prop.getProperty("CLIENTID");
		TOPIC = prop.getProperty("TOPIC");
        IN_TOPIC = prop.getProperty("IN_TOPIC");
		
		String sLeepTime = prop.getProperty("SLEEP_TIME");
		SLEEP_TIME = Long.parseLong(sLeepTime);

		// read number of sensors
		nSensors = Integer.parseInt(prop.getProperty("NSENSORS"));

		// read definition of sensors
		for (int i = 1; i <= nSensors; i++)
		{	
			String sType = prop.getProperty("sensor." + i + ".type");

			String sLabel = prop.getProperty("sensor." + i + ".label");
			
			int iPin = Integer.parseInt(prop.getProperty("sensor." + i + ".pin"));

			SensorDef sDef = new SensorDef(sType, sLabel, iPin);

			lSensorsDef.add(sDef);
		}
		
		KEYSTORE = prop.getProperty("KEYSTORE");
		KEYPWD = prop.getProperty("KEYPWD");
	}

	public void printConfig()
	{
		System.out.println("");
		System.out.println("Configuration Read from config.properties:");
		System.out.println("CLIENTID: " + CLIENTID);
		System.out.println("TYPE: " + TYPE);
		System.out.println("BROKER: " + BROKER);
		System.out.println("TOPIC: " + TOPIC);
		System.out.println("IN_TOPIC: " + IN_TOPIC);
		
		System.out.println("SLEEP_TIME: " + SLEEP_TIME);
		System.out.println("NSENSORS: " + nSensors);
		
		for (int i = 0; i < nSensors; i++)
		{
			System.out.println(lSensorsDef.get(i).getType());
		}
		
		// for SSL
		System.out.println("KEYSTORE: " + KEYSTORE);
		System.out.println("KEYPWD: " + KEYPWD);
	}
}
