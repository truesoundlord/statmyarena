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
	private GregorianCalendar MatchLength;
	
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
	
	/** PARFAITEMENT INUTILE ^^
	 * 
	 * @return 
	 */
	public boolean isStarted()
	{
		return bStart;
	}
	
	public classMatch(String Enemy,int param_myscore,int param_hisscore,BitSet Couleurs)
	{
		this.bStart = false;
		EnemyName=Enemy;
		MyScore=param_myscore;
		HisScore=param_hisscore;
		MatchColors=new BitSet(5);
		MatchColors=Couleurs;
		
		Levels=new LinkedList<>();
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
		
		dateDebut=(GregorianCalendar)GregorianCalendar.getInstance();
		dateFin=(GregorianCalendar)GregorianCalendar.getInstance();
		MatchLength=(GregorianCalendar)GregorianCalendar.getInstance();
	};

	// set methods
	
	public void setName(String param)
	{
		EnemyName=param;
	}
	
	public void setScore(boolean direction,int param)
	{
		if(direction) MyScore=param;
		else HisScore=param;
	}
	
	public void setTurns(int param)
	{
		Turns=param;
	}
	
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
	 * @param direction true for me
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
	
	public void setBegin()
	{
		dateDebut=(GregorianCalendar)GregorianCalendar.getInstance();
		bStart=true;
	}
	
	public void setEnd()
	{
		dateFin=(GregorianCalendar)GregorianCalendar.getInstance();
		if(dateDebut!=null)
		{
			setMatchLength();
		}
		bStart=false;
	}
	
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
	
	public String getBeginDate()
	{
		String composed=new String();
		
		// Comme Java c'est de la merde et qu'il n'est pas possible d'extraire les champs de l'objet il va falloir 
		//chipoter (c'est là qu'en C en une seule ligne de code c'était réglé... enfin bref...)
		
		composed=String.format("%02d",dateDebut.get(GregorianCalendar.DAY_OF_MONTH))+"/"+String.format("%02d",dateDebut.get(GregorianCalendar.MONTH))+"/"+dateDebut.get(GregorianCalendar.YEAR)+" ";
		composed+=String.format("%02d",dateDebut.get(GregorianCalendar.HOUR_OF_DAY))+":"+String.format("%02d", dateDebut.get(GregorianCalendar.MINUTE))+":"+String.format("%02d",dateDebut.get(GregorianCalendar.SECOND));
				
		return composed;
	}
	
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
	
	public boolean SaveMeToDB(java.sql.Connection LaConnection) throws SQLException
	{
		String SQLRequest=new String();
		
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			
			// Préparation de la requête
			
			SQLRequest="INSERT INTO Matches ";
			SQLRequest+="(idPlayer,MatchColor,Turns,HisScore,MyScore,Result,StartTime,EndTime,EnLvl,MyLvl,Manas,Duration) VALUES (";
			SQLRequest+=idPlayer+",";
			
			ma_Couleurs tmp=new ma_Couleurs(MatchColors);
			
			SQLRequest+=tmp.getInt()+",";
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
			System.err.println("Enregistrement ajoûté à la base de données...");
			
			idMatch=getLastInsertedID(LaConnection);
			
			Statement.close();
		}
		else return false;
		
		return true;
	}
	
	public boolean LoadMeFromDB(java.sql.Connection LaConnexion)
	{
		
		return true;
	}
	
	/**
	 * Pratiquement inutile en fait :{
	 * Finalement si, j'ai bien fait de la prévoir cette fonction
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
}
