package MagicArena;

import java.sql.SQLException;
import java.util.BitSet;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class classEnemy implements java.io.Serializable  
{
	private String Name;
	private BitSet Colors;
	
	private int SQLid;
	
	//private String Level;
	
	private int Victories;
	private int Defeats;
	private int Concedes;
	private int Draws;
		
	private int MyScore;
	private int HisScore;
	
	private int Clashes;
	
	private String Evaluation; 
	
	// SQL Related
	
	static private java.sql.Statement Statement;
	
	// Constructeur(s)
	
	public classEnemy()
	{
		this.Colors = new BitSet(5);
		Name="unknown";
		Colors.clear();
		Victories=0;
		Defeats=0;
		MyScore=0;
		HisScore=0;
		Evaluation="";
		Concedes=0;
		Clashes=0;
		Draws=0;
	};
	
	public classEnemy(String sonbiessenom)
	{
		this.Colors = new BitSet(5);
		Name=sonbiessenom;
	};
	
	// @result: indique par vrai que J'AI gagné

	/**
	 *
	 * @param biessenom
	 * @param couleurs
	 * @param MyScore
	 * @param HisScore
	 * @throws NullPointerException
	 */
	public classEnemy(String biessenom,BitSet couleurs,int MyScore,int HisScore) 
	{
		this.Colors = new BitSet(5);
		Name=biessenom;
		Colors=(BitSet)couleurs.clone();
				
		this.MyScore+=MyScore;
		this.HisScore+=HisScore;
	};
	
	// Interface get...
	
	
	public String getName()
	{
		return Name;
	};
	
	/*public String getLevel()
	{
		return Level;
	}*/
	
	public BitSet getColors()
	{
		return Colors;
	};
	
	public int getVictories()
	{
		return Victories;
	};
	
	public int getDefeats()
	{
		return Defeats;
	};
	
	/**
	 * 
	 * @return Mon score à moi 
	 */
	public int getScoreP()
	{
		return MyScore;
	};
	
	/**
	 * 
	 * @return  son score à lui
	 */
	public int getScoreE()
	{
		return HisScore;
	};
	
	public String getEvaluation()
	{
		return Evaluation;
	};
	
	public int getDraws()
	{
		return Draws;
	}
	
	public int getConceded()
	{
		return Concedes;
	}
	
	public int getClashes()
	{
		return Clashes;
	}
	
	public int getSQLid()
	{
		return SQLid;
	}
	
	// Interface set...
	
	public void setName(String param)
	{
		Name=param;
	};
	
	/*public void setLevel(String param)
	{
		Level=param;
	}*/
	
	public void setColors(BitSet param)
	{
		Colors=(BitSet)param.clone();
	};
	
	/**
	 * 
	 * 
	 * @param thescoreE
	 * @return 
	 */
	public int setScoreE(int thescoreE)
	{
		return this.HisScore=thescoreE;
	};
	
	public int setScoreP(int thescoreP)
	{
		return this.MyScore=thescoreP;
	};
	
	public void setEvaluation(String param)
	{
		Evaluation=param;
	};
	
	public void setVictories(int value)
	{
		Victories=value;
	}
	
	public void setDefeats(int value)
	{
		Defeats=value;
	}
	
	public void setConcedes(int value)
	{
		Concedes=value;
	}
	
	public void setSQLid(int id)
	{
		SQLid=id;
	}

	public void setClashes(int clashes)
	{
		Clashes=clashes;
	}
	
	public void setDraws(int param)
	{
		Draws=param;
	}

	// Interface add
	
	
	public int addVictory()
	{
		return ++Victories;
	};
	
	public int addDefeat()
	{
		return ++Defeats;
	};	
	
	public int addConcede()
	{
		return ++Concedes;
	}
		
	public int addDraw()
	{
		return ++Draws;
	}
	
	public int addClash()
	{
		return ++Clashes;
	}
	
	public void updatedb(java.sql.Connection LaConnection) throws SQLException
	{
		String SQLRequest=new String();
		
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			SQLRequest="UPDATE Players SET ";
			SQLRequest+="Victories="+Victories+",";
			SQLRequest+="Conceded="+Concedes+",";
			SQLRequest+="Defeated="+Defeats+",";
			SQLRequest+="Draws="+Draws+",";
			SQLRequest+="MyScore="+MyScore+",";
			SQLRequest+="HisScore="+HisScore+",";
			SQLRequest+="MatchesDone="+Clashes;
			SQLRequest+=" WHERE idPlayer="+SQLid;
			
			Statement.execute(SQLRequest);
			Statement.close();
		}
	}
	
	public void insertdb(java.sql.Connection LaConnection) throws SQLException
	{
		String SQLRequest=new String();
		
		ma_Couleurs tmp=new ma_Couleurs(Colors);
		int couleursenint=tmp.getInt();
		
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			SQLRequest="INSERT INTO Players (Alias,Couleurs,Victories,Conceded,Defeated,Draws,MyScore,HisScore,MatchesDone) VALUES (";
			SQLRequest+="'"+Name.replace("'", "\\'")+"',";
			SQLRequest+=couleursenint+",";
			SQLRequest+=Victories+",";
			SQLRequest+=Concedes+",";
			SQLRequest+=Defeats+",";
			SQLRequest+=Draws+",";
			SQLRequest+=MyScore+",";
			SQLRequest+=HisScore+",";
			SQLRequest+=Clashes;
			SQLRequest+=")";
			
			Statement.execute(SQLRequest);
			Statement.close();
			
			SQLid=getLastInsertedID(LaConnection);
		}
	}
	
	/**
	 * 
	 * @param LaConnection
	 * @return l'identifiant de cet ennemi (pour un deck particulier)
	 * @throws SQLException 
	 */
	public int getEnemyID_DB(java.sql.Connection LaConnection) throws SQLException
	{
		String SQLRequest=new String();
		
		ma_Couleurs tmp=new ma_Couleurs(Colors);
		int couleursenint=tmp.getInt();
		
		if(LaConnection.isValid(1))
		{
			System.err.println("[DEBUG getEnemyID_DB] "+Name+"("+couleursenint+")");
			try
			{
				Statement=LaConnection.createStatement();
				SQLRequest="SELECT idPlayer FROM Players WHERE Couleurs="+couleursenint+" AND Alias='"+Name+"'";
			
				Statement.execute(SQLRequest);
			
				java.sql.ResultSet Resultats;
				Resultats=Statement.getResultSet();
			
				if(Resultats!=null)
				{
					if(!Resultats.first()) return -1;							// peu de chances d'avoir deux enregistrements dans le resulset à moins que je sois une grosse merde
					SQLid=Resultats.getInt(1);
					Resultats.close();
				}
				Statement.close();
				
			}
			catch(SQLException ex)
			{
				throw ex;
			}
			
			return SQLid;
		}
		return -1;
	}

	
	static public int getLastInsertedID(java.sql.Connection LaConnection) throws SQLException
	{
		int id=-1;
		String SQLRequest="SELECT MAX(idPlayer) FROM Players";
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
	
	public void Reset()
	{
		Colors = new BitSet(5);
		Name=new String();
		Colors.clear();
		Victories=0;
		Defeats=0;
		MyScore=0;
		HisScore=0;
		Evaluation=new String();
		Concedes=0;
		Clashes=0;
		Draws=0;
	}
}
