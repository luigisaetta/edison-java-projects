package edison;

public interface ISensor
{
	public String getValue();
	public String getType();
	
	// to be used in display
	public String getLabel();
}
