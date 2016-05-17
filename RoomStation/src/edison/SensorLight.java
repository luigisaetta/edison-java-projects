package edison;

import upm_grove.*;

/**
 * Class added to implement a uniform interface (ISensor) for Sensors
 * @author LSaetta
 *
 */
public class SensorLight extends GroveLight implements ISensor
{
	public SensorLight(int pin)
	{
		super(pin);
	}
	
	@Override
	public String getValue()
	{	
		return Float.toString(raw_value());
	}

	@Override
	public String getType()
	{
		return "Grove.Light";
	}

	@Override
	public String getLabel()
	{
		return "L:";
	}

}
