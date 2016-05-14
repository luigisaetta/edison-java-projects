package edison;

import com.google.gson.Gson;

//import needed for MQQ communication
import org.eclipse.paho.client.mqttv3.*;

/**
 * Extends MqttMessage
 * 
 * @author LSaetta
 *
 */
public class DeviceMessage extends MqttMessage
{
	
	private String id;
	private String type;
	
	public DeviceMessage(Config config, float theTemp, float theLight, int theAirQuality)
	{
		super();
		
		// config data
		this.setQos(config.QOS);
		this.id = config.CLIENTID;
	    this.type = config.TYPE;
	    
	    // sensor data
		this.temp = theTemp;
		this.light = theLight;
		this.airQuality = theAirQuality;
		
		this.setPayload(this.toJSONString().getBytes());
	}
	
	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	private float temp;
	private float light;
    private int airQuality;
    
    public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	public int getAirQuality()
	{
		return airQuality;
	}

	public void setAirQuality(int airQuality)
	{
		this.airQuality = airQuality;
	}

	public float getTemp()
	{
		return temp;
	}

	public void setTemp(float temp)
	{
		this.temp = temp;
	}

	public float getLight()
	{
		return light;
	}

	public void setLight(float light)
	{
		this.light = light;
	}

	

	public String toJSONString()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
