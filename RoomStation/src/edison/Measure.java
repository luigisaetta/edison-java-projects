package edison;

import com.google.gson.annotations.Expose;

/**
 * This class represent a single reading from a sensor
 * @author LSaetta
 *
 */
public class Measure
{
	@Expose String type;
	@Expose String unit;
	@Expose String value;
	
	public Measure(String theType, String theUnit, String theValue)
	{
	   setType(theType);
	   setValue(theValue);
	   setUnit(theUnit);
	}
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
}
