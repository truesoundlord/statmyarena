package MagicArena;

import java.sql.SQLException;
import java.util.BitSet;
import java.util.LinkedList;


/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class ma_Statistiques extends javax.swing.JPanel  
{

	/**
	 * To be usable the SetConnection() method must be called once the constructor invoked
	 * @param parModel
	 * @param parRenderer
	 */
	public ma_Statistiques(ma_tablemodelmatch parModel,defMatch parRenderer)
	{
		lesdonneesMatches = new LinkedList<>();
		ListeDesMatches = new LinkedList<>();
		
		ModeleTableMatch=parModel;
		AfficheurMatch=parRenderer;
		
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
	
		/**
	 * Fills the model with the datas from the database concerning the matches done by one player
	 * @param param the player's alias which we want to list the matches played with
	 * @throws SQLException 
	 */
	public void listMatchesAgainstPlayer(String param) throws SQLException
	{
			// hihi ^^
		
		param=param.replace("\\", "\\\\"); // Comment gérer les connards qui mettent un backslash dans leurs alias ???
		param=param.replace("'", "\\'");
		
		if(uneConnexion.isValid(1))
		{
			ListeDesMatches.clear();
			Statement=uneConnexion.createStatement();
			bStatusRequest=Statement.execute("SELECT * FROM Matches WHERE idPlayer IN (SELECT idPlayer FROM Players WHERE Alias= BINARY '"+param+"' ORDER BY Couleurs DESC) ORDER BY idMatch DESC");
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

	static public LinkedList<classMatch> ListeDesMatches;
	public LinkedList<Object> lesdonneesMatches;
	boolean bStatusRequest;
	
	private final ma_tablemodelmatch ModeleTableMatch; // =new ma_tablemodelmatch();
	private final defMatch AfficheurMatch; //=new defMatch();
	
	private java.sql.Connection uneConnexion;
	private java.sql.ResultSet Resultats;
	private java.sql.Statement Statement;
	private java.sql.ResultSet SousRqte;
	
	static int MatchesDone;
}
