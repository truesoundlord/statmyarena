package MagicArena;

import java.awt.Color;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class ma_Statistiques extends javax.swing.JPanel  
{

	/**
	 * To be usable the SetConnection() method must be called once the constructor invoked
	 * @param source The JTable duplicated in main class
	 */
	public ma_Statistiques(JTable source)
	{
		lesdonneesMatches = new LinkedList<>();
		ListeDesMatches = new LinkedList<>();
		//parent=source;
				
		initComponents();
		setBackground(new Color(31, 112, 121,200));
		
		jTableInfos=source; // oui j'ai mis la table sur la fenêtre principale... j'ai pas trouvé mieux :{
				
		jTableInfos.setCellSelectionEnabled(true);
		jTableInfos.setAutoCreateColumnsFromModel(true);
		jTableInfos.setModel(ModeleTableMatch);
		
		jTableInfos.setShowGrid(false);
		jTableInfos.getTableHeader().setReorderingAllowed(false);
		jTableInfos.getTableHeader().setResizingAllowed(true);
		
		((DefaultTableCellRenderer) jTableInfos.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		jTableInfos.setDefaultRenderer(ma_Couleurs.class, AfficheurMatch);
		jTableInfos.setDefaultRenderer(String.class, AfficheurMatch);
		
		jTableInfos.setRowHeight(40);
	}
	
	public void computeResults(char param) throws SQLException
	{
		//System.err.println("compute "+param);
										
		
		if(uneConnexion.isValid(1))
		{
			ListeDesMatches.clear();
			Statement=uneConnexion.createStatement();
			bStatusRequest=Statement.execute("SELECT * FROM Matches WHERE Result='"+param+"' AND idPlayer=(SELECT idPlayer FROM Players WHERE idPlayer="+FenetrePrincipale.cematch.getDBPlayerID()+")");
			if(bStatusRequest)
			{
				Resultats = Statement.getResultSet();
				ModeleTableMatch.ClearDatas();
				while(Resultats.next())
				{
					java.sql.ResultSetMetaData MetaDonnees=Resultats.getMetaData();
					int NbChamps=MetaDonnees.getColumnCount();
					
					for(int cptchamps=0;cptchamps<NbChamps;cptchamps++)
					{
						String unecolonne=ParseSQL(MetaDonnees.getColumnTypeName(cptchamps+1),Resultats,cptchamps+1);
						lesdonneesMatches.add(unecolonne);
					}
					PackDatasFromDB(lesdonneesMatches); // ListeDesMatches est modifié ici (je sais c'est opaque)
					lesdonneesMatches.clear();
				}
				for(int cptMatches=0;cptMatches<ListeDesMatches.size();cptMatches++) 
				{
					ModeleTableMatch.addRow(ListeDesMatches.get(cptMatches));
				}
				
			}
			Resultats.close();
			Statement.close();
		}
		MatchesDone=ListeDesMatches.size();
	}
	
	public void computeStats(String param) throws SQLException
	{
			// hihi ^^
		
		param=param.replace("\\", "\\\\"); // Comment gérer les connards qui mettent un backslash dans leurs alias ???
		param=param.replace("'", "\\'");
		
		if(uneConnexion.isValid(1))
		{
			ListeDesMatches.clear();
			Statement=uneConnexion.createStatement();
			bStatusRequest=Statement.execute("SELECT * FROM Matches WHERE idPlayer IN (SELECT idPlayer FROM Players WHERE Alias='"+param+"' ORDER BY Couleurs DESC)");
			if(bStatusRequest)
			{
				Resultats = Statement.getResultSet();
				ModeleTableMatch.ClearDatas();
				while(Resultats.next())
				{
					java.sql.ResultSetMetaData MetaDonnees=Resultats.getMetaData();
					int NbChamps=MetaDonnees.getColumnCount();
					
					for(int cptchamps=0;cptchamps<NbChamps;cptchamps++)
					{
						String unecolonne=ParseSQL(MetaDonnees.getColumnTypeName(cptchamps+1),Resultats,cptchamps+1);
						lesdonneesMatches.add(unecolonne);
					}
					PackDatasFromDB(lesdonneesMatches);
					lesdonneesMatches.clear();
				}
				for(int cptMatches=0;cptMatches<ListeDesMatches.size();cptMatches++) 
				{
					ModeleTableMatch.addRow(ListeDesMatches.get(cptMatches));
				}
				
			}
			Resultats.close();
			Statement.close();
		}
		MatchesDone=ListeDesMatches.size();
	}
	
	public String ParseSQL(String type,java.sql.ResultSet source,int colonne) throws SQLException
	{
		String tmp=new String();
				
		if(type.contains("SMALLINT")) tmp=String.valueOf(source.getInt(colonne));
		if(type.contains("CHAR")) tmp=source.getString(colonne);
		if(type.contains("BIT")) tmp=String.valueOf(source.getByte(colonne));
 		if(type.contains("TINYINT")) tmp=String.valueOf(source.getInt(colonne));
		if(type.contains("DATETIME")) tmp=source.getDate(colonne).toString()+" "+source.getTime(colonne).toString();
		if(type.contains("INTEGER")) tmp=String.valueOf(source.getInt(colonne));
				
		return tmp;
	}
	

	
	/**
	 * ListeDesMatches is modified here...
	 * @param source
	 * @return
	 * @throws SQLException 
	 */
	public boolean PackDatasFromDB(LinkedList<Object> source) throws SQLException
	{
		classMatch tmp=new classMatch();
		BitSet tmpbs=new BitSet(5);
		String PlayerName=new String();
		
		int repint;
		
		tmp.setDBMatchID(Integer.valueOf((String)source.get(classMatch.IDMATCH)));
		tmp.setDBPlayerID(Integer.valueOf((String)source.get(classMatch.IDPLAYER)));
		
		if(uneConnexion.isValid(1))
		{
			PlayerName=getPlayerNameDB(uneConnexion,tmp.getDBPlayerID());
		}
		
		tmp.setName(PlayerName);
				
		repint=Integer.valueOf((String)source.get(classMatch.MCOL));
		
		if((repint&ma_Couleurs.VALNOIR)==ma_Couleurs.VALNOIR) tmpbs.set(ma_Couleurs.NOIR);
		if((repint&ma_Couleurs.VALROUGE)==ma_Couleurs.VALROUGE) tmpbs.set(ma_Couleurs.ROUGE);
		if((repint&ma_Couleurs.VALVERT)==ma_Couleurs.VALVERT) tmpbs.set(ma_Couleurs.VERT);
		if((repint&ma_Couleurs.VALBLEU)==ma_Couleurs.VALBLEU) tmpbs.set(ma_Couleurs.BLEU);
		if((repint&ma_Couleurs.VALBLANC)==ma_Couleurs.VALBLANC) tmpbs.set(ma_Couleurs.BLANC);
				
		tmp.setColors(tmpbs);
		
		
		tmp.setTurns(Integer.valueOf((String)source.get(classMatch.TRNS)));
		tmp.setScore(false,Integer.valueOf((String)source.get(classMatch.HIS)));	// son score en fin de match
		tmp.setScore(true,Integer.valueOf((String)source.get(classMatch.MYS)));		// mon score en fin de match
						
		tmp.setResult((String)source.get(classMatch.RES));
		
		
		String srcStart=(String)source.get(classMatch.STT);
		String srcEnd=(String)source.get(classMatch.NDT);
		
		tmp.StartFROMDB(srcStart);
		tmp.EndFROMDB(srcEnd);
						
		tmp.setDuration(Integer.valueOf((String)source.get(classMatch.MT)));				// nombres de millisecondes 
				
		String value;
		value = source.get(classMatch.ELVL).toString();
		
		tmp.importLevel(false,value);
		
		value=source.get(classMatch.PLVL).toString();
		
		tmp.importLevel(true,value);
		tmp.setManas(Integer.valueOf((String)source.get(classMatch.MANAS)));
		
				
		return ListeDesMatches.add(tmp);
	}
	
	public String getPlayerNameDB(java.sql.Connection LaConnection,int param) throws SQLException
	{
		String SQLRequest;
		String PlayerName=new String();
		
		if(LaConnection.isValid(1))
		{
			try
			{
				Statement=LaConnection.createStatement();
				SQLRequest="SELECT Alias FROM Players WHERE idPlayer="+param;
			
				Statement.execute(SQLRequest);
			
				SousRqte=Statement.getResultSet();
			
				if(SousRqte!=null)
				{
					if(!SousRqte.first()) return "";							// peu de chances d'avoir deux enregistrements dans le resulset à moins que je sois une grosse merde
					PlayerName=SousRqte.getString(1);
				}
				SousRqte.close();
				Statement.close();
				
			}
			catch(SQLException ex)
			{
				throw ex;
			}
		}
		return PlayerName;
	}

	public int getMatchesDone()
	{
		return ListeDesMatches.size();
	}
	
	public void setConnection(java.sql.Connection param) throws SQLException
	{
		if(param.isValid(1)) uneConnexion=param;
	}
	
		
	
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPaneTable = new javax.swing.JScrollPane();
    jTableInfos = new javax.swing.JTable();

    setBackground(new java.awt.Color(31, 112, 121));
    setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    setToolTipText("");
    setAlignmentX(0.0F);
    setAlignmentY(0.0F);
    setDoubleBuffered(false);
    setMaximumSize(new java.awt.Dimension(1300, 352));
    setPreferredSize(new java.awt.Dimension(1300, 352));
    setVerifyInputWhenFocusTarget(false);

    jTableInfos.setAutoCreateColumnsFromModel(false);
    jTableInfos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jTableInfos.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null},
        {null, null, null, null}
      },
      new String [] {
        "Title 1", "Title 2", "Title 3", "Title 4"
      }
    ));
    jTableInfos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    jTableInfos.setIntercellSpacing(new java.awt.Dimension(0, 3));
    jTableInfos.setPreferredSize(new java.awt.Dimension(800, 40));
    jTableInfos.setRequestFocusEnabled(false);
    jTableInfos.setRowHeight(40);
    jTableInfos.setRowSelectionAllowed(false);
    jTableInfos.setShowHorizontalLines(true);
    jScrollPaneTable.setViewportView(jTableInfos);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 964, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
	
	// Variables declaration - do not modify
	// End of variables declaration

	static public LinkedList<classMatch> ListeDesMatches;
	public LinkedList<Object> lesdonneesMatches;
	boolean bStatusRequest;
	
//	private JTabbedPane parent;
	private final ma_tablemodelmatch ModeleTableMatch=new ma_tablemodelmatch();
	private final defMatch AfficheurMatch=new defMatch();
	
	private java.sql.Connection uneConnexion;
	private java.sql.ResultSet Resultats;
	private java.sql.Statement Statement;
	private java.sql.ResultSet SousRqte;
	
	
	static int MatchesDone;
	
  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JScrollPane jScrollPaneTable;
  public javax.swing.JTable jTableInfos;
  // End of variables declaration//GEN-END:variables
}
