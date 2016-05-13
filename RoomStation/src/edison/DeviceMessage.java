package edison;

import com.google.gson.Gson;

public class DeviceMessage
{
	private String id;
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

	public DeviceMessage(String theId, float theTemp, float theLight, int theAirQuality)
	{
		this.id = theId;
		this.temp = theTemp;
		this.light = theLight;
		this.airQuality = theAirQuality;
	}

	public String toJSONString()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
