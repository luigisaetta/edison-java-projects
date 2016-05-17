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

	// Sensors
	protected int PIN_TEMP = 0;
	protected int PIN_LIGHT = 1;
	// air quality sensor (AQS)
	protected int PIN_AQS = 2;
	// I2C for LCD (0)
	protected int PIN_LCD = 0;

	// MQTT: normally read from config.properties
	protected String BROKER = "tcp://iotgateway1:1883";
	protected int QOS = 1;
	protected String TOPIC = CLIENTID + "/msg";

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
			String sName = prop.getProperty("sensor." + i + ".name");
			System.out.println(sName);

			String sType = prop.getProperty("sensor." + i + ".type");
			System.out.println(sType);

			int iPin = Integer.parseInt(prop.getProperty("sensor." + i + ".pin"));
			System.out.println(iPin);

			SensorDef sDef = new SensorDef(sName, sType, iPin);

			lSensorsDef.add(sDef);
		}
	}

	public void printConfig()
	{
		System.out.println("Configuration :");
		System.out.println("CLIENTID: " + CLIENTID);
		System.out.println("TYPE: " + TYPE);
		System.out.println("BROKER: " + BROKER);
		System.out.println("TOPIC: " + TOPIC);
		System.out.println("SLEEP_TIME: " + SLEEP_TIME);
	}
}
