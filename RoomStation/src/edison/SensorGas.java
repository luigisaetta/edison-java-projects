package edison;

import upm_gas.*;

public class SensorGas extends TP401 implements ISensor 
{
	private final static String NAME = "Grove.TP401";
	
	public SensorGas(int pin)
	{
		super(pin);
	}
	
	@Override
	public String getValue()
	{
		return Integer.toString(getSample());
	}

	@Override
	public String getType()
	{
		return NAME;
	}

	@Override
	public String getLabel()
	{
		return "A.Q.:";
	}

	@Override
	public Measure getMeasure()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
