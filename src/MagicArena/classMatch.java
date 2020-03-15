package MagicArena;

import java.sql.SQLException;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class classMatch 
{
	private String EnemyName;
	private int MyScore;
	private int HisScore;
	private int Turns;
	
	private int keyNiveauEnemy;
	private int keyMonNiveau;
	
	static LinkedList<String> Levels;
		
	private BitSet MatchColors;
	
	private int idPlayer;
	private int idMatch;
	private int Manas;									// noires je ne pense pas changer de deck pour faire les tapettes avec plusieurs couleurs et faire des combinaisons de merde qui prennent des heures...
																			// et qui une fois sur deux ne servent à rien ^^

	public enum Resultats {VIC,CON,DEF,DRW;};
	
	Resultats MatchResult;
	
	private GregorianCalendar dateDebut;
	private GregorianCalendar dateFin;
	private final GregorianCalendar MatchLength;
	
	private final String strPostfixes[]=new String[]{"Tier 4", "Tier 3", "Tier 2", "Tier 1"};
	private final String strPrefixes[]=new String[]{"Bronze", "Silver", "Gold", "Platinium", "Diamond","Mystic"};   // platinium tier 1 <-> 15
	
	private boolean bStart;
	
	// Pour les accès aux bases de données
	
	static final int IDMATCH=0;
	static final int IDPLAYER=1;
	static final int MCOL=2;
	static final int TRNS=3;
	static final int HIS=4;
	static final int MYS=5;
	static final int RES=6;
	static final int STT=7;
 	static final int NDT=8;
	static final int ELVL=9;
	static final int PLVL=10;
	static final int MANAS=11;
	static final int MT=12;
	
	static final int PRSY=0;
	static final int PRSM=1;
	static final int PRSD=2;
	static final int PRSH=3;
	static final int PRSm=4;
	static final int PRSS=5;
	
	
	// SQL Related
	
	static private java.sql.Statement Statement;
	
			
	// Constructeur
	
	/**
	 * Object representing a "match"... (here each time 1 vs 1)
	 */
	public classMatch()
	{
		this.bStart = false;
		Levels=new LinkedList<>();
		MatchColors=new BitSet(5);
		dateDebut=(GregorianCalendar)GregorianCalendar.getInstance();
		dateFin=(GregorianCalendar)GregorianCalendar.getInstance();
		
		Levels.clear();
		
		//int cptLevel=0;
		for(int cptPrefixes=0;cptPrefixes<strPrefixes.length;cptPrefixes++)
		{
			for(int cptPostfixes=0;cptPostfixes<strPostfixes.length;cptPostfixes++)
			{
				String strLevel=strPrefixes[cptPrefixes]+"("+strPostfixes[cptPostfixes]+")";
				Levels.add(strLevel);
				//cptLevel++;
			}		
		}
		
		MatchLength=(GregorianCalendar)GregorianCalendar.getInstance();				
	}

	// set methods
	
	/**
	 * Sets the name of the opponent 
	 * @param param 
	 */
	public void setName(String param)
	{
		EnemyName=param;
	}
	
	/**
	 * Sets the score
	 * @param direction
	 * true: the score of the player
	 * false: the score of the opponent
	 * @param param 
	 * value representing the score
	 */
	public void setScore(boolean direction,int param)
	{
		if(direction) MyScore=param;
		else HisScore=param;
	}
	
	public void setTurns(int param)
	{
		Turns=param;
	}
	
	/**
	 * 
	 * @param direction false opponent, true player
	 * @param param the string representation, not the index
	 */
	public void setLevel(boolean direction,String param)
	{
		if(direction)
		{
			keyMonNiveau=Levels.indexOf(param);
			
		}
		else
		{
			keyNiveauEnemy=Levels.indexOf(param);
		}
	}
	
	/**
	 *
	 * @param direction true for player, false for opponent
	 * @param param
	 */
	public void importLevel(boolean direction,String param)
	{
		if(direction)
		{
			keyMonNiveau=Integer.valueOf(param);
			
		}
		else
		{
			keyNiveauEnemy=Integer.valueOf(param);
		}
	}
	
	public void setColors(BitSet param)
	{
		MatchColors=(BitSet)param.clone();
	}
	
	public void setDBMatchID(int param)
	{
		idMatch=param;
	}
	
	public void setDBPlayerID(int param)
	{
		idPlayer=param;
	}
	
	public int getDBPlayerID()
	{
		return idPlayer;
	}
	
	public void setResult(Resultats param)
	{
		MatchResult=param;
	}
	
	public void setResult(String param)
	{
		if(param.contains("V")) MatchResult=Resultats.VIC;
		if(param.contains("D")) MatchResult=Resultats.DEF;
		if(param.contains("C")) MatchResult=Resultats.CON;
 		if(param.contains("E")) MatchResult=Resultats.DRW;
	}
	
	/**
	 * Sets the beginning time stamp
	 */
	public void setBegin()
	{
		dateDebut=(GregorianCalendar)GregorianCalendar.getInstance();
		bStart=true;
	}
	
	/**
	 * Sets the ending time stamp 
	 * Computes the duration of a match
	 */
	public void setEnd()
	{
		dateFin=(GregorianCalendar)GregorianCalendar.getInstance();
		if(dateDebut!=null)
		{
			setMatchLength();
		}
		bStart=false;
	}
	
	/**
	 * Computes the date representation from DB
	 * @param param 
	 */
	public void StartFROMDB(String param)
	{
		String[] parsed=param.split("[\\-|\\:|\\s]");
		
		dateDebut.set(GregorianCalendar.YEAR, Integer.valueOf(parsed[PRSY]));
		dateDebut.set(GregorianCalendar.MONTH, Integer.valueOf(parsed[PRSM]));
		dateDebut.set(GregorianCalendar.DAY_OF_MONTH, Integer.valueOf(parsed[PRSD]));
		dateDebut.set(GregorianCalendar.HOUR_OF_DAY, Integer.valueOf(parsed[PRSH]));
		dateDebut.set(GregorianCalendar.MINUTE, Integer.valueOf(parsed[PRSm]));
		dateDebut.set(GregorianCalendar.SECOND, Integer.valueOf(parsed[PRSS]));
		
	}
	
	/**
	 * Computes the date representation from DB
	 * @param param 
	 */
	public void EndFROMDB(String param)
	{
		String[] parsed=param.split("[\\-|\\:|\\s]");
		
		dateFin.set(GregorianCalendar.YEAR, Integer.valueOf(parsed[PRSY]));
		dateFin.set(GregorianCalendar.MONTH, Integer.valueOf(parsed[PRSM]));
		dateFin.set(GregorianCalendar.DAY_OF_MONTH, Integer.valueOf(parsed[PRSD]));
		dateFin.set(GregorianCalendar.HOUR_OF_DAY, Integer.valueOf(parsed[PRSH]));
		dateFin.set(GregorianCalendar.MINUTE, Integer.valueOf(parsed[PRSm]));
		dateFin.set(GregorianCalendar.SECOND, Integer.valueOf(parsed[PRSS]));
	}
	
	/**
	 * Sets the duration in milliseconds
	 */
	public void setMatchLength()
	{
		MatchLength.clear();
		long debut=dateDebut.getTimeInMillis();			// millisecondes
		long fin=dateFin.getTimeInMillis();					// millisecondes
		long elapsed=fin-debut;
			
		MatchLength.setTimeInMillis(elapsed);				// millisecondes
	}
	
	public void setManas(int param)
	{
		Manas=param;
	}
	
	/**
	 * Sets the duration of a match from seconds to milliseconds
	 * @param param 
	 */
	
	public void setDuration(long param)
	{
		MatchLength.setTimeInMillis(param*1000);																			// Ce sont des millisecondes qui sont passées alors qu'en fait ce sont des SECONDES (au regard des données dans la base de données)
	}
		
	// get methods
	
	public String getName()
	{
		return EnemyName;
	}
	
	public BitSet getColors()
	{
		return MatchColors;
	}
	
	/**
	 * To get the level of the opponent set to false.
	 * To get the level of the player set to true.
	 * @param direction
	 * @return 
	 */
	public int getLevel(boolean direction)
	{
		if(direction)
		{
			return keyMonNiveau;
		}
		else
		{
			return keyNiveauEnemy;
		}
	}
	
	/**
	 * To get the level of the opponent set to false.
	 * To get the level of the player set to true.
	 * @param direction
	 * @return 
	 */
	public int getScore(boolean direction)
	{
		if(direction)
		{
			return MyScore;
		}
		else
		{
			return HisScore;
		}
	}
	
	public Resultats getResults()
	{
		return MatchResult;
	}
	
	/**
	 * Get the date string in the DD/MM/YYYY HH:MM:SS format
	 * @return 
	 */
	public String getBeginDate()
	{
		String composed;
		
		// Comme Java c'est de la merde et qu'il n'est pas possible d'extraire les champs de l'objet il va falloir 
		//chipoter (c'est là qu'en C en une seule ligne de code c'était réglé... enfin bref...)
		
		composed=String.format("%02d",dateDebut.get(GregorianCalendar.DAY_OF_MONTH))+"/"+String.format("%02d",dateDebut.get(GregorianCalendar.MONTH))+"/"+dateDebut.get(GregorianCalendar.YEAR)+" ";
		composed+=String.format("%02d",dateDebut.get(GregorianCalendar.HOUR_OF_DAY))+":"+String.format("%02d", dateDebut.get(GregorianCalendar.MINUTE))+":"+String.format("%02d",dateDebut.get(GregorianCalendar.SECOND));
				
		return composed;
	}
	
	/**
	 * Get the date string in the DD/MM/YYYY HH:MM:SS format
	 * @return 
	 */
	String getEndDate()
	{
		String composed;
		
		// Comme Java c'est de la merde et qu'il n'est pas possible d'extraire les champs de l'objet il va falloir 
		//chipoter (c'est là qu'en C en une seule ligne de code c'était réglé... enfin bref...)
		
		composed=String.format("%02d",dateFin.get(GregorianCalendar.DAY_OF_MONTH))+"/"+String.format("%02d",dateFin.get(GregorianCalendar.MONTH))+"/"+dateFin.get(GregorianCalendar.YEAR)+" ";
		composed+=String.format("%02d",dateFin.get(GregorianCalendar.HOUR_OF_DAY))+":"+String.format("%02d", dateFin.get(GregorianCalendar.MINUTE))+":"+String.format("%02d",dateFin.get(GregorianCalendar.SECOND));
				
		return composed;
	}
	
	/**
	 * Get the date string in the HH:MM:SS format
	 * @return 
	 */
	public String getDuration()
	{
		//System.err.println("Vérifications: "+MatchLength.toString());
		return String.format("%02d", MatchLength.get(GregorianCalendar.HOUR_OF_DAY)-1)+":"+String.format("%02d", MatchLength.get(GregorianCalendar.MINUTE))+":"+String.format("%02d",MatchLength.get(GregorianCalendar.SECOND));
	}
	
	public int getTurns()
	{
		return Turns;
	}
	
	public int getManas()
	{
		return Manas;
	}
	
	public int getMatchID()
	{
		return idMatch;
	}
	
	/**
	 * Saves the object to the database
	 * @param LaConnection
	 * @return
	 * @throws SQLException 
	 */
	public boolean SaveMeToDB(java.sql.Connection LaConnection) throws SQLException
	{
		String SQLRequest;
		
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			
			// Préparation de la requête
			
			SQLRequest="INSERT INTO Matches ";
			SQLRequest+="(idPlayer,MatchColor,Turns,HisScore,MyScore,Result,StartTime,EndTime,EnLvl,MyLvl,Manas,Duration) VALUES (";
			SQLRequest+=idPlayer+",";
			
			//ma_Couleurs tmp=new ma_Couleurs(MatchColors);  février 2020
			
			SQLRequest+=ma_Couleurs.getInt(MatchColors)+",";
			SQLRequest+=Turns+",";
			SQLRequest+=HisScore+",";
			SQLRequest+=MyScore+",";
			if(MatchResult==Resultats.VIC) SQLRequest+="'V',";
			if(MatchResult==Resultats.CON) SQLRequest+="'C',";
			if(MatchResult==Resultats.DEF) SQLRequest+="'D',";
 			if(MatchResult==Resultats.DRW) SQLRequest+="'E',";
			SQLRequest+="(FROM_UNIXTIME("+dateDebut.getTimeInMillis()/1000+")),"; // DATETIME ne supporte pas les millisecondes apparemment (???)
			SQLRequest+="(FROM_UNIXTIME("+dateFin.getTimeInMillis()/1000+")),";
			SQLRequest+=keyNiveauEnemy+","; 
			SQLRequest+=keyMonNiveau+",";
			SQLRequest+=Manas+",";
			// au départ je divisais par 1000 pour n'obtenir que des secondes mais ça merdait après...
			// Or, dans la base de données mes données sont correctes, si je fais SELECT UNIX_TIMESTAMP(EndTime-StartTime) pour le champs Duration
			// j'obtiens bien des SECONDES
			
			// Par contre quand j'essaye de récupérer les secondes de MatchLength.get(GregorianCalendar.SECOND) il m'envoie chier car pour lui
			// se sont des MILLISECONDES... qui sont lues lorsqu'on charge la table Matches en mémoire... 
			
			SQLRequest+=MatchLength.getTimeInMillis()/1000;
			SQLRequest+=")";
							
			Statement.execute(SQLRequest);
			System.err.println("Match enregistré...");
			
			idMatch=getLastInsertedID(LaConnection);
			
			Statement.close();
		}
		else return false;
		
		return true;
	}

	/**
	 * Get the last inserted ID from DB 
	 * @param LaConnection
	 * @return
	 * @throws SQLException 
	 */
	static public int getLastInsertedID(java.sql.Connection LaConnection) throws SQLException
	{
		int id=-1;
		String SQLRequest="SELECT MAX(idMatch) FROM Matches";
		if(LaConnection.isValid(1))
		{
			try
			{
				Statement=LaConnection.createStatement();
				Statement.execute(SQLRequest);
				
				java.sql.ResultSet Resultats;
				Resultats=Statement.getResultSet();
			
				if(Resultats!=null)
				{
					if(!Resultats.first()) return id;							// peu de chances d'avoir deux enregistrements dans le resulset à moins que je sois une grosse merde
					id=Resultats.getInt(1);
					Resultats.close();
				}
				Statement.close();
			}
			catch(SQLException ex)
			{
				throw ex;
			}
		}
		return id;
	}
	
	public String getComments(java.sql.Connection LaConnection) throws SQLException
	{
		String tmp="no comments";
		if(idMatch>0)
		{
			if(LaConnection.isValid(1))
			{
				String SQLRequest="SELECT Comments FROM Comments WHERE idMatch="+idMatch;
				Statement=LaConnection.createStatement();
				Statement.execute(SQLRequest);
				
				java.sql.ResultSet Resultats;
				Resultats=Statement.getResultSet();
				
				if(Resultats.first())
				{
					// Il faudrait utiliser d'après la documentation le format HTML :{
					tmp="<html>";
					
					String prework=Resultats.getString(1);
					
					int length=prework.length();
					
					// Il faudrait formater le message de manière à ce que ce soit lisible ^^
					
					if(prework.length()>30) prework=WrapComments(prework);
										
					//tmp+="<TEXTAREA ROWS=4 COLS=50 BORDER=0>"+prework+"</TEXTAREA>";	
					tmp+=prework;	
					
					tmp+="</html>";
				}
				Resultats.close();
				Statement.close();
				
			}
		}
		tmp=tmp.replace("\n", "<BR>");
		
		return tmp;
	}
	
	/**
	 * Attempt to format the TootTipText in a readable way...
	 * @param texttoformat
	 * @return 
	 */
	private String WrapComments(String texttoformat)
	{
		String formatedtext="";
				
		//texttoformat=texttoformat.replace("\n"," ");
		
		char[] strText=texttoformat.toCharArray();
		
		// Principe: les commentaires tiennent sur un nombre limité de colonnes (30)
		// il faut faire en sorte que le texte qui vient de la base de données soit correctement formatté
				
		int longueurtexte=texttoformat.length();
		
		
		// vu qu'on est pas en C il va falloir se casser le cul :{
		
		int position=1;
		int positionligne=1;
		do
		{
			if(strText[position]=='\n') 
			{
				positionligne=0;
			}
			if((strText[position]>='0' && strText[position]<'9') && strText[position+1]=='x' && position<longueurtexte-1)															// essayer de lister
			{
				strText[position-1]='\n';
				positionligne=0;
				position+=2;
				formatedtext=String.valueOf(strText);
				continue;
			}
			if((strText[position]>='0' && strText[position]<'9') && strText[position+1]=='/' && position<longueurtexte-1)															// essayer de lister
			{
				positionligne=0;
				position+=2;
				formatedtext=String.valueOf(strText);
				continue;
			}
						
			if(positionligne>40)
			{
				if(strText[position]=='.') 
				{
					strText[position+1]='\n';
					positionligne=0;
				}
				if(strText[position]==' ' && strText[position+1]!='!' && position<longueurtexte-1) 
				{
					strText[position]='\n';
					positionligne=0;
				}
			}
			else positionligne++;
			formatedtext=String.valueOf(strText);
			position++;
		}while(position<longueurtexte-1);
		
		return formatedtext;
	}
	
} // END CLASS

