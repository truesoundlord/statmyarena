package MagicArena;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class UEPElement 
{
	private final int leftBound;					// closed interval [lb,rb]
	private final int rightBound;
	
	public UEPElement(int lb,int rb)
	{
		leftBound=lb;
		rightBound=rb;
	}
	
	public boolean isInInterval(int value)
	{
		return value>=leftBound && value<=rightBound;
	}
		
	
};

