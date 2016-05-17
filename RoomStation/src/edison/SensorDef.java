package edison;

/**
 * Used in config.propertis
 * @author LSaetta
 *
 */
public class SensorDef
{
	// use this constant to initialize the type
	public static final int TEMP = 1;
	public static final int LIGHT = 2;
	public static final int GAS = 3;
	
	private String label;
	private String type;
	
	private boolean analogic;
	private int pin;
	
	public boolean isAnalogic()
	{
		return analogic;
	}

	public void setAnalogic(boolean analogic)
	{
		this.analogic = analogic;
	}

	public int getPin()
	{
		return pin;
	}

	public void setPin(int pin)
	{
		this.pin = pin;
	}

	public SensorDef(String theType, String theLabel, int thePin)
	{
		this.type = theType;
		this.label = theLabel;
		this.pin = thePin;
	}
	
	public String getLabel()
	{
		return label;
	}
	public void setLabel(String label)
	{
		this.label = label;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
}
