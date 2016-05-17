package edison;

import upm_grove.*;

public class SensorTemp extends GroveTemp implements ISensor 
{
	public SensorTemp(int pin)
	{
		super(pin);
	}
	
	@Override
	public String getValue()
	{
		return Float.toString(value());
	}

	@Override
	public String getType()
	{
		return "Grove.Temp";
	}

	@Override
	public String getLabel()
	{
		return "T:";
	}

}
