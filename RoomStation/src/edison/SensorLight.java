package edison;

import upm_grove.*;

/**
 * Class added to implement a unifor interface (ISensor) for Sensors
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
		String ret = Float.toString(raw_value());
		
		return ret;
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
