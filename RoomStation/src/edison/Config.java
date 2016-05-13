package edison;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config
{
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
	
	
	public void readConfig() 
	{
		Properties prop = new Properties();
		String propFileName = "config.properties";

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

		TYPE = prop.getProperty("TYPE");
		BROKER = prop.getProperty("BROKER");
		CLIENTID = prop.getProperty("CLIENTID");
		TOPIC = prop.getProperty("TOPIC");
		
		String sLeepTime =  prop.getProperty("SLEEP_TIME");
		SLEEP_TIME = Long.parseLong(sLeepTime);
	}
}
