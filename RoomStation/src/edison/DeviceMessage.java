package edison;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	// @Expose marks the only field serialized in JSON message
	@Expose private String id;
	@Expose private String type;
	@Expose private Date date;
	
	//
	// READINGS FROM SENSORS
	//
	@Expose private List<Measure> readings = new ArrayList<Measure>();
	
	public DeviceMessage(Config config, List<Measure> theList)
	{
		super();
		
		this.setQos(config.QOS);
		this.id = config.CLIENTID;
		this.type = config.TYPE;
		this.date = new Date();
		
		readings.addAll(theList);
		
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

	public String toJSONString()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		return gson.toJson(this);
	}
}
