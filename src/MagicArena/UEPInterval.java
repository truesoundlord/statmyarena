package MagicArena;

import java.util.LinkedList;



/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class UEPInterval 
{
	private final LinkedList<UEPElement> internalList;
	public int nbElem;
	
		
	public UEPInterval(UEPElement[] elems)
	{
		internalList=new LinkedList<>();
		nbElem=addElements(elems);
	}
	
	/**
	 * @param value
	 * @return -1 of value is not in any interval in the list
	 */
					
	public int getListIndexFromValue(int value)
	{
		int cpt;
		
		for(cpt=0;cpt<internalList.size();cpt++)
		{
			if(((UEPElement)internalList.get(cpt)).isInInterval(value)) return cpt;
		}
		
		return -1;
	}
	
	private int addElements(UEPElement[] elems)
	{
		int cpt;
		for(cpt=0;cpt<elems.length;cpt++)
		{
			internalList.add(elems[cpt]);
		}
		return internalList.size();
	}
	
	public int getSize()
	{
		return nbElem;
	}
	
};



