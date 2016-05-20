package edison;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config
{
	private final static String propFileName = "config.properties";

	protected String CLIENTID = "thunder10";
	protected String TYPE = "myType";

	// timing
	protected long SLEEP_TIME = 5000;


	// MQTT: normally read from config.properties
	protected String BROKER = "tcp://iotgateway1:1883";
	protected int QOS = 1;
	protected String TOPIC = CLIENTID + "/msg";

	// Sensors
	protected int nSensors;

	protected List<SensorDef> lSensorsDef = new ArrayList<SensorDef>();

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
	}

	public void printConfig()
	{
		System.out.println("");
		System.out.println("Configuration Read from config.properties:");
		System.out.println("CLIENTID: " + CLIENTID);
		System.out.println("TYPE: " + TYPE);
		System.out.println("BROKER: " + BROKER);
		System.out.println("TOPIC: " + TOPIC);
		System.out.println("SLEEP_TIME: " + SLEEP_TIME);
		System.out.println("NSENSORS: " + nSensors);
		
		for (int i = 0; i < nSensors; i++)
		{
			System.out.println(lSensorsDef.get(i).getType());
		}
		
	}
}
