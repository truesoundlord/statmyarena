package MagicArena;

import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public abstract class UEPTable extends JTable 
{

	/**
	 * Tentative de création d'une table générale pour la gestion des joueurs et des matches
	 * Creation attempt of a "general" Table object for the players and matches
	 * @param TheModel the model used to handle the table datas
	 * @param TheRenderer the renderer to display the table datas
	 */
	public UEPTable(TableModel TheModel, TableCellRenderer TheRenderer) 
	{
		InnerModel=TheModel;
		InnerRenderer=TheRenderer;
		
		setModel(InnerModel);
		
	}
	
	public void addDefaultRenderer(Class<?> laclasse)
	{
		setDefaultRenderer(laclasse, InnerRenderer);
	}
	
	@Override
	public String getToolTipText(MouseEvent souris)
	{
		String infos = null;
		java.awt.Point coordonnee=souris.getPoint();
		// par rapport à ces coordonnées java est capable de déterminer quelle est la ligne ou la colonne pointée
		int ligne=rowAtPoint(coordonnee);
		int colonne=columnAtPoint(coordonnee);
		
		try
		{
			if(ligne>=0 && colonne!=ma_tablemodelmatch.SCE)
			{
				infos=getFromModel(ligne,colonne);
			}
		}
		catch(RuntimeException ex)
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
		return infos;
	}
	
	@Override
	public void setToolTipText(String LeTexte)
	{
		
	}
	
	private final TableModel InnerModel;
	private final TableCellRenderer InnerRenderer;
	
	// Abstract methods
	
	/**
	 * This abstract method has to be used to help the method getToolTipText to display coherent data
	 * @param ligne line
	 * @param colonne column
	 * @return a string representation of the data located at the coordinates ligne,colonne  
	 */
	public abstract String getFromModel(int ligne,int colonne);
}
