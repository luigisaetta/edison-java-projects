package edison;

import upm_grove.*;

/**
 * This class implements the Adapter (GOF) Design Patter in order to have
 * a uniform interface to UPM classes
 * 
 * The Uniform Interface is defined in the ISensor Java Interface
 * @author LSaetta
 *
 */
public class SensorTemp extends GroveTemp implements ISensor 
{
	private final static String NAME = "Grove.Temp";
	
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
		return NAME;
	}

	@Override
	public String getLabel()
	{
		return "T:";
	}

	@Override
	public Measure getMeasure()
	{
		Measure mes = new Measure("GAS", "UNIT", getValue());
		
		return mes;
	}

	@Override
	public String getUnit()
	{
		return "C";
	}

}
