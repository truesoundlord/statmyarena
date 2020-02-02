package MagicArena;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class ma_tablemodel extends AbstractTableModel
{
	// Un MODEL permet de gérer de manière transparente le contenu d'un tableau
	// Il faut minimum un tableau de deux dimensions
	
	Object Donnees[][];
	
	// Il faut aussi pouvoir donner des noms aux "headers" (le nom des colonnes)
	
	private final String DefaultHeaders[]=new String[]{"Name","Colors","Victories","Defeats","Conceded","Score E","Score P","Played","Draws"};
	String titres[]=DefaultHeaders;
	
	// Il faut fixer la position de la colonne par une constante
	
	final static int NAME=0;
	final static int COL=1;
	final static int VIC=2;
	final static int DEF=3;
	final static int CON=4;
	final static int SCE=5;			// son score total
	final static int SCP=6;			// mon score total pour tous les matches
	final static int MJ=7;
	final static int DRWS=8;
	
	private int updatedCol=-1;
	private int updatedRow=-1;
	
	// Il faut indiquer quels seraient les "classes" ou types d'objets dans chacune des colonnes (le renderer fera ensuite le nécessaire)
	
	Class types[]=new Class[] {String.class,ma_Couleurs.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class};
	
	// Constructeur(s)
	
	public ma_tablemodel()
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
	public Object getValueAt(int posY, int posX) 
	{
		return Donnees[posY][posX];
	}
	
	@Override
	public void setValueAt(Object value,int ligne,int colonne)
	{
		Donnees[ligne][colonne]=value;
		fireTableDataChanged();
		updatedCol=colonne;
	}
	
	/**
	 *
	 * @param column
	 * @return
	 */
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
	
	
	/*************************************************************************************************************************************************************
	 * Adds a blank row to the model.
	 */ 
	public void addBlankRow()
	{
		int CurrentRow=Donnees.length-1;
		// pour ne pas foutre la merde dans la table il faut sauvegarder son contenu actuel...
		Object saved[][]=new Object[Donnees.length][titres.length];
		saved=Donnees;
		// il faut redimensionner le tableau
		Donnees=new Object[Donnees.length+1][titres.length];
		// il faut restituer les valeurs
		for(int cpt=CurrentRow-1;cpt>=0;cpt--) Donnees[cpt]=saved[cpt];
		
		Donnees[CurrentRow][NAME]=""; 
		Donnees[CurrentRow][COL]=new ma_Couleurs();
		Donnees[CurrentRow][VIC]="";
		Donnees[CurrentRow][DEF]="";
		Donnees[CurrentRow][CON]="";
		Donnees[CurrentRow][SCP]="";
		Donnees[CurrentRow][SCE]="";
		Donnees[CurrentRow][MJ]="";
		Donnees[CurrentRow][DRWS]="";
		
		fireTableDataChanged();                                        // On indique au contrôle que quelque chose a changé et qu'il faut redessiner le contenu
  }
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) 
	{
		return false;
	}
	
	public void addRow(classEnemy target)
	{
		int CurrentRow=Donnees.length-1;
		Object saved[][]=new Object[Donnees.length][titres.length];
		saved=Donnees;
		Donnees=new Object[Donnees.length+1][titres.length];
		for(int cpt=CurrentRow-1;cpt>=0;cpt--) Donnees[cpt]=saved[cpt];
		
		Donnees[CurrentRow][NAME]=target.getName(); 
		Donnees[CurrentRow][COL]=new ma_Couleurs(target.getColors());
		Donnees[CurrentRow][VIC]=Integer.toString(target.getVictories());
		Donnees[CurrentRow][DEF]=Integer.toString(target.getDefeats());
		Donnees[CurrentRow][CON]=Integer.toString(target.getConceded());
		Donnees[CurrentRow][SCE]=Integer.toString(target.getScoreE());
		Donnees[CurrentRow][SCP]=Integer.toString(target.getScoreP());
		Donnees[CurrentRow][MJ]=Integer.toString(target.getClashes());
		Donnees[CurrentRow][DRWS]=Integer.toString(target.getDraws());
		
	//	fireTableDataChanged();                                        // On indique au contrôle que quelque chose a changé et qu'il faut redessiner le contenu
		fireTableRowsInserted(CurrentRow, CurrentRow);
	}
	
	public void setRow(classEnemy target,int CurrentRow)
	{
		Donnees[CurrentRow][NAME]=target.getName(); 
		Donnees[CurrentRow][COL]=new ma_Couleurs(target.getColors());
		Donnees[CurrentRow][VIC]=Integer.toString(target.getVictories());
		Donnees[CurrentRow][DEF]=Integer.toString(target.getDefeats());
		Donnees[CurrentRow][CON]=Integer.toString(target.getConceded());
		Donnees[CurrentRow][SCE]=Integer.toString(target.getScoreE());
		Donnees[CurrentRow][SCP]=Integer.toString(target.getScoreP());
		Donnees[CurrentRow][MJ]=Integer.toString(target.getClashes());
		Donnees[CurrentRow][DRWS]=Integer.toString(target.getDraws());
		
		fireTableRowsUpdated(CurrentRow, CurrentRow);
	}
		
	public void ClearDatas()
  {
		Donnees=new Object[1][titres.length];
    fireTableDataChanged();
  }

	public int getUpdatedCol()
	{
		return updatedCol;
	}
	
	public void setUpdatedCol(int param)
	{
		updatedCol=param;
	}
	
	public int getUpdatedRow()
	{
		return updatedRow;
	}
	
	public void setUpdatedRow(int param)
	{
		updatedRow=param;
	}
	
}
