package edison;

public class Measure
{
	String type;
	String value;
	String unit;
	
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
