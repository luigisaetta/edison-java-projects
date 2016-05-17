package edison;

import upm_gas.*;

public class SensorGas extends TP401 implements ISensor 
{

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
		return "Grove.TP401";
	}

	@Override
	public String getLabel()
	{
		return "A.Q.:";
	}
}
