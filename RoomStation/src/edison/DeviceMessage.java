package edison;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

//import needed for MQTT communication
import org.eclipse.paho.client.mqttv3.*;

/**
 * Extends MqttMessage
 * 
 * @author LSaetta
 *
 */
public class DeviceMessage extends MqttMessage
{
    // the annotation @Expose to avoid that field of the superclass (MqttMessage) 
	// are serialized in JSON message
	@Expose private String id;
	@Expose private String type;
	@Expose private String temp;
	@Expose private String light;
	@Expose private int airQuality;
	
	public DeviceMessage(Config config, String theTemp, String theLight, int theAirQuality)
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

		setPayload(this.toJSONString().getBytes());
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

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

	public String getTemp()
	{
		return temp;
	}

	public void setTemp(String temp)
	{
		this.temp = temp;
	}

	public String getLight()
	{
		return light;
	}

	public void setLight(String light)
	{
		this.light = light;
	}

	public String toJSONString()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		return gson.toJson(this);
	}
}
