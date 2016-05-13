package edison;

public class Config
{
	protected int SLEEP_TIME = 5000;
	
	// Sensors
	protected int PIN_TEMP = 0;
	protected int PIN_LIGHT = 1;
	// air quality sensor (AQS)
	protected int PIN_AQS = 2;
	
	// MQTT
	protected String BROKER = "tcp://iotgateway1:1883";
	protected int QOS = 1;
	protected String CLIENTID = "thunder10";
	protected String TOPIC = CLIENTID + "/msg";
	
	public void readConfig()
	{
		// TODO not yet implemented
		// from where ?
	}
}
