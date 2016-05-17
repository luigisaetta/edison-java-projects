package edison;

import upm_grove.*;

/**
 * Class added to implement a uniform interface (ISensor) for Sensors
 * @author LSaetta
 *
 */
public class SensorLight extends GroveLight implements ISensor
{
	private final static String NAME = "Grove.Light";
	
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
		return NAME;
	}

	@Override
	public String getLabel()
	{
		return "L:";
	}

	@Override
	public Measure getMeasure()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
