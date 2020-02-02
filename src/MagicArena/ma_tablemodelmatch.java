package MagicArena;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class ma_tablemodelmatch extends AbstractTableModel 
{
	Object Donnees[][];
	private final String DefaultHeaders[]=new String[]{"Name","Colors","Level E","Score E","Score P","Level P"};
	String titres[]=DefaultHeaders;
	
	final static int NAME=0;
	final static int COL=1;
	final static int MYLVL=5;
	final static int SCP=4;
	final static int SCC=3;
	final static int ENLVL=2;
	//final static int RES=6;
	
	Class types[]=new Class[] {String.class,ma_Couleurs.class,String.class,String.class,String.class,String.class};
	
	public ma_tablemodelmatch()
	{
		Donnees=new Object[1][titres.length];
		
   
	}
	
	@Override
	public int getRowCount() 
	{
		return Donnees.length-1;
	}

	@Override
	public int getColumnCount() 
	{
		return titres.length;
	}

	@Override
	public Object getValueAt(int ligne, int colonne) 
	{
		return Donnees[ligne][colonne];
	}
	
	@Override
	public void setValueAt(Object value,int ligne,int colonne)
	{
		Donnees[ligne][colonne]=value;
		fireTableDataChanged();
		
	}
	
	@Override
	public String getColumnName (int column)
	{
		return titres[column];
	}
	
	@Override
	public Class getColumnClass (int column)
	{
		return types[column];
	}
	
	public void addRow(classMatch unmatch)
	{
		int CurrentRow=Donnees.length-1;
		Object saved[][]=new Object[Donnees.length][titres.length];
		saved=Donnees;
		Donnees=new Object[Donnees.length+1][titres.length];
		for(int cpt=CurrentRow-1;cpt>=0;cpt--) Donnees[cpt]=saved[cpt];
		
		Donnees[CurrentRow][NAME]=unmatch.getName();
		Donnees[CurrentRow][COL]=new ma_Couleurs(unmatch.getColors());
		Donnees[CurrentRow][MYLVL]=String.valueOf(unmatch.getLevel(true));
		Donnees[CurrentRow][SCP]=String.valueOf(unmatch.getScore(true));
		Donnees[CurrentRow][SCC]=String.valueOf(unmatch.getScore(false));
		Donnees[CurrentRow][ENLVL]=String.valueOf(unmatch.getLevel(false));
		//Donnees[CurrentRow][RES]=String.valueOf(unmatch.getResults());
		
		//System.err.println("Appel au modèle -- classMatch (CurrentRow "+CurrentRow+")");
		
		//fireTableDataChanged();                                        // On indique au contrôle que quelque chose a changé et qu'il faut redessiner le contenu
		fireTableRowsInserted(CurrentRow-1, CurrentRow);
	}
	
	public void ClearDatas()
  {
		Donnees=new Object[1][titres.length];
    fireTableDataChanged();
  }

}
