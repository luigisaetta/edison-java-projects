package edison;

import com.google.gson.Gson;

public class DeviceMessage
{
	private float temp;
	private float light;

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

	public DeviceMessage(float theTemp, float theLight)
	{
		this.temp = theTemp;
		this.light = theLight;
	}

	public String toJSONString()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
