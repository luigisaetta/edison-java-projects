package edison;

public interface ISensor
{
	public String getValue();
	
	public Measure getMeasure();
	
	public String getType();
	
	// to be used in display
	public String getLabel();
}
