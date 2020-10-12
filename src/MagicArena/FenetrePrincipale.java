package MagicArena;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import org.mariadb.jdbc.Driver;


/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class FenetrePrincipale extends javax.swing.JFrame 
{

	/**
	 * Creates new form FenetrePrincipale
	 * @throws java.sql.SQLException
	 */
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public FenetrePrincipale() throws SQLException 
	{
		lesIntervales=new UEPElement[]{new UEPElement(0,3),new UEPElement(4,7),new UEPElement(8,11),new UEPElement(12,15),new UEPElement(16,19),new UEPElement(20,20)};
		this.innerModel = new ma_tablemodelmatch();
		this.lesdonnees = new LinkedList<>();
		this.listeEnnemis = new LinkedList<>();
		this.strPostfixes = new String[]{"Tier 4", "Tier 3", "Tier 2", "Tier 1"};
		this.strPrefixes = new String[]{"Bronze", "Silver", "Gold", "Platinium", "Diamond","Mythic"};
		
		verifLevels=new UEPInterval(lesIntervales);
		
		initComponents();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/iconma.png")));
		
		setTitle("MagicArena Stats v"+FenetrePrincipale.Version+FenetrePrincipale.SubVersion+" [Match helper]");
				
		jTablePlayer.setCellSelectionEnabled(true);
		jTablePlayer.setAutoCreateColumnsFromModel(true);
		jTablePlayer.setModel(ModeleTable);
		
		jTablePlayer.setShowGrid(false);
		jTablePlayer.getTableHeader().setReorderingAllowed(false);
		jTablePlayer.getTableHeader().setResizingAllowed(true);
		
		((DefaultTableCellRenderer) jTablePlayer.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
			
		
		jTablePlayer.setDefaultRenderer(String.class,AfficheurStandard);
		jTablePlayer.setDefaultRenderer(ma_Couleurs.class,AfficheurStandard);
		

		jTablePlayer.setRowHeight(40);
		jTablePlayer.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

				
		// ComboBoxes populating
		
		jComboBoxEnemyLevel.removeAllItems();
		jComboBoxMyLevel.removeAllItems();
		
		int cptLevel=0;
		for(int cptPrefixes=0;cptPrefixes<strPrefixes.length-1;cptPrefixes++)
		{
			for(int cptPostfixes=0;cptPostfixes<strPostfixes.length;cptPostfixes++)
			{
				Levels.add(strPrefixes[cptPrefixes]+"("+strPostfixes[cptPostfixes]+")");
				//System.err.println("["+cptLevel+"]"+strLevels[cptLevel]);
				jComboBoxMyLevel.addItem(Levels.get(cptLevel));
				jComboBoxEnemyLevel.addItem(Levels.get(cptLevel));
				cptLevel++;
			}
		}
		
		// ajouter le niveau mythic
		jComboBoxMyLevel.addItem(strPrefixes[5]);
		jComboBoxEnemyLevel.addItem(strPrefixes[5]);
		Levels.add(strPrefixes[5]);
	
		// JTable populating
		
		LeDriver = new Driver();
		
		try 
		{
			LaConnection = LeDriver.connect("jdbc:mariadb://192.168.0.3:3306/MagicArena?user=magicuser&password=resucigam",null);
			if(LaConnection.isValid(1))
			{
				isConnected=true;
				
				PopulateTable();
				
				Tours=0;
				
				jTableMatches=new UEPTable(innerModel,innerRenderer)
				{
					@Override
					public String getFromModel(int ligne, int colonne)
					{
						try
						{
							switch(colonne)
							{
								case ma_tablemodelmatch.NAME:		return "Match: "+String.valueOf(ma_Statistiques.ListeDesMatches.get(ligne).getMatchID())+" "+
																								ma_Statistiques.ListeDesMatches.get(ligne).getBeginDate()+
																								" ["+ma_Statistiques.ListeDesMatches.get(ligne).getResults()+"]";
								case ma_tablemodelmatch.COL:			return ((ma_Couleurs)innerModel.getValueAt(ligne, ma_tablemodelmatch.COL)).getBinaryString();
								case ma_tablemodelmatch.ENLVL:		// return Levels.get(Integer.valueOf(innerModel.getValueAt(ligne, ma_tablemodelmatch.ENLVL).toString()));
																								return TTTLevels(ligne,false);
								case ma_tablemodelmatch.MYLVL:		// return Levels.get(Integer.valueOf(innerModel.getValueAt(ligne, ma_tablemodelmatch.MYLVL).toString()));
																								return TTTLevels(ligne,true);
								case ma_tablemodelmatch.SCP:		
																								return ma_Statistiques.ListeDesMatches.get(ligne).getComments(LaConnection);
																								 
																								
								default: return "";
							}
						}
						catch(SQLException ex) 
						{
							Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
						}
						return "";
					}

					private String TTTLevels(int ligne,boolean param) throws SQLException
					{
						// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
						int level;
						if(param)
							level=Integer.valueOf(innerModel.getValueAt(ligne, ma_tablemodelmatch.MYLVL).toString());
						else
							level=Integer.valueOf(innerModel.getValueAt(ligne, ma_tablemodelmatch.ENLVL).toString());
						if(level==20)
						{
							int pourcentage=ma_Statistiques.ListeDesMatches.get(ligne).getMysticLevel(LaConnection, param);
							if(pourcentage<0) pourcentage=0;
							return Levels.get(level)+" ("+String.format("%02d %%",pourcentage)+")";
						}
						else return Levels.get(level);
					}
				};
				
				Statistiques=new ma_Statistiques(innerModel,innerRenderer);
				Statistiques.setConnection(LaConnection);
		
				
				jScrollPanePlayers.getVerticalScrollBar().setUnitIncrement(400);
				jScrollPanePlayers.getVerticalScrollBar().setBlockIncrement(400);

				jScrollPanePlayers.getVerticalScrollBar().setValue(80);
				jScrollPanePlayers.getVerticalScrollBar().setDoubleBuffered(true);
				
				jTableMatches.addDefaultRenderer(String.class);
				jTableMatches.addDefaultRenderer(ma_Couleurs.class);
			
				jTableMatches.setBackground(new java.awt.Color(31, 112, 121));
				jTableMatches.setCellSelectionEnabled(true);
				jTableMatches.setAutoCreateColumnsFromModel(false);

				jTableMatches.setShowGrid(false);
				jTableMatches.getTableHeader().setReorderingAllowed(false);
				jTableMatches.getTableHeader().setResizingAllowed(true);

				jTableMatches.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
				jTableMatches.setRequestFocusEnabled(false);
				jTableMatches.setRowHeight(40);
				jTableMatches.setRowSelectionAllowed(false);
				jTableMatches.setShowHorizontalLines(true);
				
				// Gestion du clic sur une des colonnes de la table listant les matches...
				
				jTableMatches.addMouseListener(new java.awt.event.MouseAdapter()
				{
					@Override
					public void mouseClicked(java.awt.event.MouseEvent evt)
					{
						
						if(evt.getButton()==2) 
						{
							ProceedMiddleClick();
							//System.err.println("CLIC !!");
						}
						
						SelectionMatch(evt);
						
						// éviter de devoir passer par le tri au-dessus pour retrouver TOUS les matches joués contre un joueur en particulier quelque soit son deck
						
						int colonne=jTableMatches.getSelectedColumn();
						int ligne=jTableMatches.getSelectedRow();
						//System.err.println("DEBUG: "+colonne+","+ligne);
						if(colonne==0)
						{
							String NomDuConnard=String.valueOf(jTableMatches.getModel().getValueAt(ligne,colonne));
							//System.err.println("DEBUG: "+NomDuConnard);
							
							try
							{
								Statistiques.listMatchesAgainstPlayer(NomDuConnard);
							} 
							catch (SQLException ex)
							{
								Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
				});
				
				// TEST avril 2020
				
						JTableHeader header = jTablePlayer.getTableHeader();
						header.addMouseListener(new MouseAdapter()
						{
							@Override
							public void mouseClicked(java.awt.event.MouseEvent evt)
							{
								Point position=evt.getPoint();
								int colonne=jTableMatches.columnAtPoint(position);
								//System.err.println("Colonne: "+colonne);
								if(colonne==5)
								{
									System.err.println("CLIC !!");
									// TODO: faire un tri sur le nombre de rencontres ordre décroissant ou croissant
								}
							}
						});
				

				((DefaultTableCellRenderer) jTableMatches.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

				jScrollPaneMatchesExt.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
				jScrollPaneMatchesExt.setViewportView(jTableMatches);
				
				jTextAreaCommentaires.setEditable(false);
				setMyLevel();
				setEnLevel();
				
				if(jComboBoxEnemyLevel.getSelectedIndex()==20 || jComboBoxMyLevel.getSelectedIndex()==20)
				{
					// Traitement du niveau spécial Mythic
					jTFEnMythicLevel.setVisible(true);
					jTFMyMythicLevel.setVisible(true);
				}
				else
				{
					jTFEnMythicLevel.setVisible(false);
					jTFMyMythicLevel.setVisible(false);
				}
			}
		} 
		catch (SQLException ex) 
		{
			Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(this, "La base de données n'est pas disponible\n"+ex.getMessage()+"["+ex.getSQLState()+"]");
			
		}
	}
	
	/**
	 * This method is called from within the constructor to initialize the form. 
	 * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanelPanneau = new javax.swing.JPanel();
    jTextField_enemy = new javax.swing.JTextField();
    jScrollPanePlayers = new javax.swing.JScrollPane();
    jTablePlayer = new javax.swing.JTable();
    jCheckBoxRouge = new javax.swing.JCheckBox();
    jCheckBoxVert = new javax.swing.JCheckBox();
    jCheckBoxBleu = new javax.swing.JCheckBox();
    jCheckBoxBlanc = new javax.swing.JCheckBox();
    jCheckBoxNoir = new javax.swing.JCheckBox();
    jComboBoxMyLevel = new javax.swing.JComboBox<>();
    jComboBoxEnemyLevel = new javax.swing.JComboBox<>();
    jPanelResults = new javax.swing.JPanel();
    jSliderResults = new javax.swing.JSlider();
    jTextFieldEnemyScore = new javax.swing.JTextField();
    jTextFieldMyScore = new javax.swing.JTextField();
    jTextFieldResultText = new javax.swing.JTextField();
    jButtonUpdate = new javax.swing.JButton();
    jLabelTours = new javax.swing.JLabel();
    jButtonAddTurn = new javax.swing.JButton();
    jScrollPaneComments = new javax.swing.JScrollPane();
    jTextAreaCommentaires = new javax.swing.JTextArea();
    jTextFieldDate = new javax.swing.JTextField();
    jTextFieldMatchDuration = new javax.swing.JTextField();
    jTextFieldMatchTurns = new javax.swing.JTextField();
    jTextFieldOAVictories = new javax.swing.JTextField();
    jTextFieldOADefeats = new javax.swing.JTextField();
    jSpinnerManaNoires = new javax.swing.JSpinner();
    jLabelManas = new javax.swing.JLabel();
    jStatsManasTours = new javax.swing.JLabel();
    jScrollPaneMatchesExt = new javax.swing.JScrollPane();
    jTFMyMythicLevel = new javax.swing.JTextField();
    jTFEnMythicLevel = new javax.swing.JTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("MagicArena Stats v0.2.2020");
    setBackground(new java.awt.Color(67, 100, 121));
    setMaximumSize(new java.awt.Dimension(1390, 880));
    setMinimumSize(new java.awt.Dimension(1390, 880));
    setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    setUndecorated(true);
    setPreferredSize(new java.awt.Dimension(1390, 880));
    setResizable(false);
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        formMouseClicked(evt);
      }
    });
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    jPanelPanneau.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("MagicArena Statistics")));
    jPanelPanneau.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jPanelPanneau.setMaximumSize(new java.awt.Dimension(1400, 900));
    jPanelPanneau.setMinimumSize(new java.awt.Dimension(1400, 900));
    jPanelPanneau.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jPanelPanneauClic(evt);
      }
    });

    jTextField_enemy.setBackground(new java.awt.Color(0, 102, 102));
    jTextField_enemy.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextField_enemy.setForeground(new java.awt.Color(255, 255, 204));
    jTextField_enemy.setToolTipText("Player name");
    jTextField_enemy.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
    jTextField_enemy.setFocusTraversalPolicyProvider(true);
    jTextField_enemy.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        displayAsTyped(evt);
      }
    });

    jScrollPanePlayers.setPreferredSize(new java.awt.Dimension(1080, 400));

    jTablePlayer.setAutoCreateColumnsFromModel(false);
    jTablePlayer.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jTablePlayer.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTablePlayer.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jTablePlayer.setCellSelectionEnabled(true);
    jTablePlayer.setDoubleBuffered(true);
    jTablePlayer.setMaximumSize(new java.awt.Dimension(0, 400));
    jTablePlayer.setMinimumSize(new java.awt.Dimension(0, 400));
    jTablePlayer.setRowHeight(32);
    jTablePlayer.setUpdateSelectionOnSort(false);
    jTablePlayer.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        SelectionJoueur(evt);
      }
    });
    jScrollPanePlayers.setViewportView(jTablePlayer);

    jCheckBoxRouge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manarouge.png"))); // NOI18N
    jCheckBoxRouge.setRolloverEnabled(false);
    jCheckBoxRouge.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manarouge.png"))); // NOI18N
    jCheckBoxRouge.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectDeckRouge(evt);
      }
    });

    jCheckBoxVert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manavert.png"))); // NOI18N
    jCheckBoxVert.setRolloverEnabled(false);
    jCheckBoxVert.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manavert.png"))); // NOI18N
    jCheckBoxVert.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectDeckVert(evt);
      }
    });

    jCheckBoxBleu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manableu.png"))); // NOI18N
    jCheckBoxBleu.setRolloverEnabled(false);
    jCheckBoxBleu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manableu.png"))); // NOI18N
    jCheckBoxBleu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectDeckBleu(evt);
      }
    });

    jCheckBoxBlanc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manablanc.png"))); // NOI18N
    jCheckBoxBlanc.setRolloverEnabled(false);
    jCheckBoxBlanc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manablanc.png"))); // NOI18N
    jCheckBoxBlanc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectDeckBlanc(evt);
      }
    });

    jCheckBoxNoir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_mananoir.png"))); // NOI18N
    jCheckBoxNoir.setRolloverEnabled(false);
    jCheckBoxNoir.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/mananoir.png"))); // NOI18N
    jCheckBoxNoir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectDeckNoir(evt);
      }
    });

    jComboBoxMyLevel.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jComboBoxMyLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jComboBoxMyLevel.setToolTipText("My current level");
    jComboBoxMyLevel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectionMyLevel(evt);
      }
    });

    jComboBoxEnemyLevel.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jComboBoxEnemyLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jComboBoxEnemyLevel.setToolTipText("Enemy level");
    jComboBoxEnemyLevel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectionEnemyLevel(evt);
      }
    });

    jPanelResults.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Liberation Mono", 1, 12))); // NOI18N

    jSliderResults.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jSliderResults.setMajorTickSpacing(1);
    jSliderResults.setMaximum(4);
    jSliderResults.setMinimum(1);
    jSliderResults.setMinorTickSpacing(1);
    jSliderResults.setPaintTicks(true);
    jSliderResults.setSnapToTicks(true);
    jSliderResults.setToolTipText("Results");
    jSliderResults.setValue(1);
    jSliderResults.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jSliderResults.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jsliderchanged(evt);
      }
    });

    jTextFieldEnemyScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldEnemyScore.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTextFieldEnemyScore.setText("20");
    jTextFieldEnemyScore.setToolTipText("Set player score");
    jTextFieldEnemyScore.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTextFieldEnemyScoreKeyPressed(evt);
      }
    });

    jTextFieldMyScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldMyScore.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTextFieldMyScore.setText("20");
    jTextFieldMyScore.setToolTipText("Set my score");
    jTextFieldMyScore.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTextFieldMyScoreKeyPressed(evt);
      }
    });

    jTextFieldResultText.setEditable(false);
    jTextFieldResultText.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldResultText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTextFieldResultText.setText("Defeat");
    jTextFieldResultText.setToolTipText("Results");

    javax.swing.GroupLayout jPanelResultsLayout = new javax.swing.GroupLayout(jPanelResults);
    jPanelResults.setLayout(jPanelResultsLayout);
    jPanelResultsLayout.setHorizontalGroup(
      jPanelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelResultsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelResultsLayout.createSequentialGroup()
            .addGroup(jPanelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jTextFieldEnemyScore)
              .addComponent(jTextFieldMyScore))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldResultText, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jSliderResults, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanelResultsLayout.setVerticalGroup(
      jPanelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelResultsLayout.createSequentialGroup()
        .addComponent(jSliderResults, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanelResultsLayout.createSequentialGroup()
            .addComponent(jTextFieldEnemyScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldMyScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanelResultsLayout.createSequentialGroup()
            .addGap(19, 19, 19)
            .addComponent(jTextFieldResultText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );

    jButtonUpdate.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jButtonUpdate.setText("Update");
    jButtonUpdate.setToolTipText("Save datas");
    jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        updatedatas(evt);
      }
    });

    jLabelTours.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLabelTours.setText("Turns");
    jLabelTours.setToolTipText("Indicates the number of turns");

    jButtonAddTurn.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jButtonAddTurn.setText("Turn");
    jButtonAddTurn.setToolTipText("Push each time it is YOUR turn");
    jButtonAddTurn.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        clicsouris(evt);
      }
    });
    jButtonAddTurn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        AjouterTour(evt);
      }
    });

    jTextAreaCommentaires.setBackground(new java.awt.Color(204, 255, 255));
    jTextAreaCommentaires.setColumns(20);
    jTextAreaCommentaires.setFont(new java.awt.Font("Monospaced", 1, 10)); // NOI18N
    jTextAreaCommentaires.setForeground(new java.awt.Color(102, 102, 102));
    jTextAreaCommentaires.setLineWrap(true);
    jTextAreaCommentaires.setRows(5);
    jTextAreaCommentaires.setTabSize(2);
    jTextAreaCommentaires.setToolTipText("Match comments");
    jTextAreaCommentaires.setWrapStyleWord(true);
    jTextAreaCommentaires.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jTextAreaCommentaires.setMaximumSize(new java.awt.Dimension(126, 66));
    jTextAreaCommentaires.setMinimumSize(new java.awt.Dimension(126, 66));
    jTextAreaCommentaires.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        SauvegarderCommentaires(evt);
      }
    });
    jScrollPaneComments.setViewportView(jTextAreaCommentaires);

    jTextFieldDate.setBackground(new java.awt.Color(0, 153, 153));
    jTextFieldDate.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldDate.setForeground(new java.awt.Color(255, 170, 0));
    jTextFieldDate.setToolTipText("Fixture date");

    jTextFieldMatchDuration.setBackground(new java.awt.Color(0, 153, 153));
    jTextFieldMatchDuration.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldMatchDuration.setForeground(new java.awt.Color(255, 170, 0));
    jTextFieldMatchDuration.setToolTipText("Match duration");

    jTextFieldMatchTurns.setBackground(new java.awt.Color(0, 153, 153));
    jTextFieldMatchTurns.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldMatchTurns.setForeground(new java.awt.Color(255, 170, 0));
    jTextFieldMatchTurns.setToolTipText("Turns played ");

    jTextFieldOAVictories.setBackground(new java.awt.Color(0, 153, 153));
    jTextFieldOAVictories.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldOAVictories.setForeground(new java.awt.Color(255, 170, 0));
    jTextFieldOAVictories.setToolTipText("Number of won matches (including concedes)");

    jTextFieldOADefeats.setBackground(new java.awt.Color(0, 153, 153));
    jTextFieldOADefeats.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTextFieldOADefeats.setForeground(new java.awt.Color(255, 170, 0));
    jTextFieldOADefeats.setToolTipText("Number of lost matches (including concedes)");

    jSpinnerManaNoires.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jSpinnerManaNoires.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        modifyManasNoires(evt);
      }
    });

    jLabelManas.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLabelManas.setText("Manas");
    jLabelManas.setToolTipText("Indicates the number of black manas");

    jStatsManasTours.setText("Manas/Tours");

    jScrollPaneMatchesExt.setMaximumSize(new java.awt.Dimension(32767, 280));
    jScrollPaneMatchesExt.setMinimumSize(new java.awt.Dimension(25, 280));
    jScrollPaneMatchesExt.setPreferredSize(new java.awt.Dimension(6, 280));

    jTFMyMythicLevel.setBackground(new java.awt.Color(0, 204, 102));
    jTFMyMythicLevel.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTFMyMythicLevel.setForeground(new java.awt.Color(0, 102, 102));
    jTFMyMythicLevel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFMyMythicLevel.setText("0");
    jTFMyMythicLevel.setToolTipText("Your Mythic Level");
    jTFMyMythicLevel.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTFMyMythicLevelKeyPressed(evt);
      }
    });

    jTFEnMythicLevel.setBackground(new java.awt.Color(255, 51, 51));
    jTFEnMythicLevel.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTFEnMythicLevel.setForeground(new java.awt.Color(249, 252, 252));
    jTFEnMythicLevel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFEnMythicLevel.setText("0");
    jTFEnMythicLevel.setToolTipText("Enemy Mythic Level");
    jTFEnMythicLevel.setAutoscrolls(false);
    jTFEnMythicLevel.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        jTFEnMythicLevelKeyPressed(evt);
      }
    });

    javax.swing.GroupLayout jPanelPanneauLayout = new javax.swing.GroupLayout(jPanelPanneau);
    jPanelPanneau.setLayout(jPanelPanneauLayout);
    jPanelPanneauLayout.setHorizontalGroup(
      jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelPanneauLayout.createSequentialGroup()
        .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanelPanneauLayout.createSequentialGroup()
              .addGap(24, 24, 24)
              .addComponent(jButtonUpdate))
            .addGroup(jPanelPanneauLayout.createSequentialGroup()
              .addGap(6, 6, 6)
              .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jComboBoxEnemyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBoxMyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelResults, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelPanneauLayout.createSequentialGroup()
                  .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTours, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelManas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonAddTurn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSpinnerManaNoires))))))
          .addGroup(jPanelPanneauLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(jPanelPanneauLayout.createSequentialGroup()
                .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanelPanneauLayout.createSequentialGroup()
                      .addComponent(jCheckBoxNoir)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(jCheckBoxRouge)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(jCheckBoxVert)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(jCheckBoxBleu)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(jCheckBoxBlanc))
                    .addComponent(jTextField_enemy, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPanneauLayout.createSequentialGroup()
                    .addComponent(jTFEnMythicLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTFMyMythicLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPanePlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
              .addGroup(jPanelPanneauLayout.createSequentialGroup()
                .addComponent(jScrollPaneMatchesExt, javax.swing.GroupLayout.PREFERRED_SIZE, 1006, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jStatsManasTours, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jScrollPaneComments, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jTextFieldOADefeats, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jTextFieldOAVictories, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jTextFieldMatchTurns, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jTextFieldMatchDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        .addGap(4, 4, 4))
    );
    jPanelPanneauLayout.setVerticalGroup(
      jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelPanneauLayout.createSequentialGroup()
        .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanelPanneauLayout.createSequentialGroup()
            .addComponent(jTextField_enemy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jCheckBoxNoir)
              .addComponent(jCheckBoxRouge)
              .addComponent(jCheckBoxVert)
              .addComponent(jCheckBoxBleu)
              .addComponent(jCheckBoxBlanc))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jComboBoxEnemyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jComboBoxMyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanelResults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jTFEnMythicLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jTFMyMythicLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jSpinnerManaNoires, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabelManas))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelTours)
              .addComponent(jButtonAddTurn))
            .addGap(33, 33, 33)
            .addComponent(jButtonUpdate))
          .addComponent(jScrollPanePlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(8, 8, 8)
        .addGroup(jPanelPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanelPanneauLayout.createSequentialGroup()
            .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldMatchDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldMatchTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldOAVictories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextFieldOADefeats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jStatsManasTours, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPaneComments))
          .addComponent(jScrollPaneMatchesExt, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(8, 8, 8))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanelPanneau, javax.swing.GroupLayout.PREFERRED_SIZE, 1367, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanelPanneau, javax.swing.GroupLayout.PREFERRED_SIZE, 830, Short.MAX_VALUE))
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void selectDeckNoir(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDeckNoir
    if(enemyColors.DeckColors.get(ma_Couleurs.NOIR)==false) enemyColors.DeckColors.set(ma_Couleurs.NOIR);
		else enemyColors.DeckColors.clear(ma_Couleurs.NOIR);
		//System.err.println("Value: "+enemyColors.DeckColors.toString()+"("+enemyColors.getBinaryString()+")");
		
  }//GEN-LAST:event_selectDeckNoir

  private void selectDeckRouge(java.awt.event.ActionEvent evt) {                                 
    if(enemyColors.DeckColors.get(ma_Couleurs.ROUGE)==false) enemyColors.DeckColors.set(ma_Couleurs.ROUGE);
		else enemyColors.DeckColors.clear(ma_Couleurs.ROUGE);
		//System.err.println("Value: "+enemyColors.DeckColors.toString()+"("+enemyColors.getBinaryString()+")");
  }                                

  private void selectDeckVert(java.awt.event.ActionEvent evt) {                                 
    if(enemyColors.DeckColors.get(ma_Couleurs.VERT)==false) enemyColors.DeckColors.set(ma_Couleurs.VERT);
		else enemyColors.DeckColors.clear(ma_Couleurs.VERT);
		//System.err.println("Value: "+enemyColors.DeckColors.toString()+"("+enemyColors.getBinaryString()+")");
  }                                
  
  private void selectDeckBleu(java.awt.event.ActionEvent evt) {                                 
    if(enemyColors.DeckColors.get(ma_Couleurs.BLEU)==false) enemyColors.DeckColors.set(ma_Couleurs.BLEU);
		else enemyColors.DeckColors.clear(ma_Couleurs.BLEU);
		//System.err.println("Value: "+enemyColors.DeckColors.toString()+"("+enemyColors.getBinaryString()+")");
  }                                
  
  private void selectDeckBlanc(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDeckRouge
    if(enemyColors.DeckColors.get(ma_Couleurs.BLANC)==false) enemyColors.DeckColors.set(ma_Couleurs.BLANC);
		else enemyColors.DeckColors.clear(ma_Couleurs.BLANC);
		//System.err.println("Value: "+enemyColors.DeckColors.toString()+"("+enemyColors.getBinaryString()+")");
  }//GEN-LAST:event_selectDeckRouge

  private void jsliderchanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jsliderchanged
    
		if(!jSliderResults.getValueIsAdjusting())
		{
			switch(jSliderResults.getValue())
			{
				case 1: jTextFieldResultText.setText("Defeat");
								break;
				case 2:	jTextFieldResultText.setText("Concede");
								break;
				case 3:	jTextFieldResultText.setText("Victory");
								break;
				case 4: jTextFieldResultText.setText("Draw");
								break;
			}
		}
  }//GEN-LAST:event_jsliderchanged

  private void updatedatas(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatedatas
    MatchResult=jSliderResults.getValue();
		
		if(cematch==null)
		{
			JOptionPane.showMessageDialog(getParent(), "cematch est à null");
			return;
		}
		
		cematch.setEnd();																					// le match est terminé
		System.err.println("End: "+cematch.getEndDate());
		
		// Par rapport au match, on se place de mon point de vue...
		switch(MatchResult)
		{
			case 1: cematch.setResult(classMatch.Resultats.DEF);		// défaite ou lorsque je concède un match
							break;
			case 2: cematch.setResult(classMatch.Resultats.CON);		// (petite victoire) l'ennemi abandonne 
							break;
			case 3: cematch.setResult(classMatch.Resultats.VIC);		// Je lui ai explosé la gueulle
							break;
			case 4: cematch.setResult(classMatch.Resultats.DRW);		// Ca m'est arrivé une fois sur les 600 parties jouées
							break;
		}
			
		String dlgMessage="Informations: \n";
		dlgMessage+="Match results:\n"+jTextFieldResultText.getText()+"\n";
		dlgMessage+="Enemy name:"+jTextField_enemy.getText()+"\n";
		dlgMessage+="Enemy Score (up):"+jTextFieldEnemyScore.getText()+"\n";
		dlgMessage+="Your score (down):"+jTextFieldMyScore.getText()+"\n";
		dlgMessage+="Enemy Level:"+jComboBoxEnemyLevel.getSelectedItem()+"\n";
		dlgMessage+="Your Level:"+jComboBoxMyLevel.getSelectedItem()+"\n";
		dlgMessage+="Enemy match colors:"+enemyColors.getBinaryString()+"\n";
		dlgMessage+="Turns: "+jLabelTours.getText()+"\n";
		dlgMessage+="Manas: "+jSpinnerManaNoires.getValue()+"\n";
		dlgMessage+="Begin: "+cematch.getBeginDate()+"\n";
		
		
		System.err.println(dlgMessage);
		
		// AVRIL 2020
		
		int intervaleE=verifLevels.getListIndexFromValue(jComboBoxEnemyLevel.getSelectedIndex());
		int intervaleP=verifLevels.getListIndexFromValue(jComboBoxMyLevel.getSelectedIndex());
				
		cematch.setLevel(false, (String)jComboBoxEnemyLevel.getSelectedItem());
		cematch.setLevel(true, (String)jComboBoxMyLevel.getSelectedItem());
		
		// C'est tellement de la merde qu'on ne voit rien...
						
		int answer=JOptionPane.showConfirmDialog(getParent(), dlgMessage, "Are you sure ?", JOptionPane.YES_NO_OPTION);
		if(answer==JOptionPane.YES_OPTION)
		{
			// C'est ICI que nous devrons ajouter l'enregistrement des données du match...
						
			// Première vérification
									
			if(jTextField_enemy.getText().isEmpty())
			{
				dlgMessage="Name never been set :{";
				answer=JOptionPane.showConfirmDialog(getParent(), dlgMessage, "Are you sure ?", JOptionPane.YES_NO_OPTION);
				if(answer==JOptionPane.NO_OPTION || answer==JOptionPane.CANCEL_OPTION)
				{
					System.err.println("Aborted !!");
					return;
				}
			}
			
			// Deuxième vérification
			
			if(enemyColors.DeckColors.isEmpty()) 
			{	
				dlgMessage="Colors never been set :{";
				answer=JOptionPane.showConfirmDialog(getParent(), dlgMessage, "Are you sure ?", JOptionPane.YES_NO_OPTION);
				if(answer==JOptionPane.NO_OPTION || answer==JOptionPane.CANCEL_OPTION)
				{
					System.err.println("Aborted !!");
					return;
				}
			}
			
			// AVRIL 2020
			if(intervaleE!=intervaleP)
			{
				dlgMessage="Levels are not similars :{";
				answer=JOptionPane.showConfirmDialog(getParent(), dlgMessage, "Are you sure ?", JOptionPane.YES_NO_OPTION);
				if(answer==JOptionPane.NO_OPTION || answer==JOptionPane.CANCEL_OPTION)
				{
					System.err.println("Aborted !!");
					return;
				}
			}
			
			// 26 AVRIL 2020 (date à laquelle j'ai atteint le niveau Mythic ^^)
			
			if(cematch.getLevel(false)==20 && cematch.getLevel(true)==20)
			{
				if(jTFEnMythicLevel.getText().isEmpty() || jTFMyMythicLevel.getText().isEmpty())
				{
					dlgMessage="Mythic levels are not set :{";
					answer=JOptionPane.showConfirmDialog(getParent(), dlgMessage, "Are you sure ?", JOptionPane.YES_NO_OPTION);
					if(answer==JOptionPane.NO_OPTION || answer==JOptionPane.CANCEL_OPTION)
					{
						System.err.println("Aborted !!");
						return;
					}
				}
			}
			
			// Pour éviter tout soucis !!
			
			if(jTextField_enemy.getText().isEmpty()) 
			{
				System.err.println("ERROR !! Name couldn't be empty !!"); // cas du <Oublié> dans la base de données ^^
				return;
			}
			
			cematch.setName(jTextField_enemy.getText());
			cematch.setColors(enemyColors.DeckColors);																					
			cematch.setScore(false,Integer.valueOf(jTextFieldEnemyScore.getText()));
			cematch.setScore(true,Integer.valueOf(jTextFieldMyScore.getText()));
			cematch.setMatchLength();
			
			// Petits soucis (deux matchs sur 2500) AVRIL 2020
			if(cematch.getScore(false)<=0 && cematch.getResults()!=classMatch.Resultats.VIC) 
			{
				cematch.setResult(classMatch.Resultats.VIC);
				MatchResult=3;
				System.err.println("Fixed: MUST BE VICTORY !!");
			}
			if(cematch.getScore(true)<=0 && cematch.getResults()!=classMatch.Resultats.DEF) 
			{
				cematch.setResult(classMatch.Resultats.DEF);
				MatchResult=1;
				System.err.println("Fixed: MUST BE DEFEAT !!");
			}
			if(cematch.getScore(false)==20 && cematch.getScore(true)==20 && cematch.getColors().isEmpty() && cematch.getResults()!=classMatch.Resultats.CON)
			{
				cematch.setResult(classMatch.Resultats.CON);
				MatchResult=2;
				System.err.println("Fixed: MUST BE CONCEDE !!");
			}
						
			// Troisième vérification
			
			if(jLabelTours.getText().contains("Turns")) 
			{	
				jLabelTours.setText("1");
				dlgMessage="Turns never been set :{";
				answer=JOptionPane.showConfirmDialog(getParent(), dlgMessage, "Are you sure ?", JOptionPane.YES_NO_OPTION);
				if(answer==JOptionPane.NO_OPTION || answer==JOptionPane.CANCEL_OPTION)
				{
					System.err.println("Aborted !!");
					return;
				}
			}
						
			cematch.setTurns(Integer.valueOf(jLabelTours.getText()));
			cematch.setManas((int)(jSpinnerManaNoires.getValue()));
			
			// Chercher le nom dans la liste des ennemis (je sais c'est lent mais ça va aller ^^ )
			
			ceConnard=new classEnemy();
			
			boolean bFound=false;
			for(int cpt=0;cpt<listeEnnemis.size();cpt++)
			{
				ceConnard=listeEnnemis.get(cpt);

				// Si le joueur n'a pas de couleur (suite à un "concede" avant même le premier tour)
				// OU si le joueur du match n'a pas de couleur (pour les mêmes raisons) ->
				// Je considère que ce joueur est le même et que nous l'avons trouvé...
										
				if(ceConnard.getName().equals(cematch.getName()))
				{
					// Vérifier si les couleurs sont définies ou pas...
					
					//System.err.println("ceConnard colors: "+ceConnard.getColors().toString());
					//System.err.println("cematch colors: "+cematch.getColors().toString());
					
					if(ceConnard.getColors().isEmpty() || cematch.getColors().isEmpty())
					{
						System.err.println("Trouvé sans couleurs...");
						
						if(ceConnard.getColors().isEmpty()) ceConnard.setColors(cematch.getColors()); // le joueur prend la couleur du dernier match...
						bFound=true;
						break;
					}
				}
				
				// Si le joueur joue avec un autre set de couleurs pour son deck, il faut considérer qu'il s'agit d'un autre joueur
				// On affronte un joueur ET UN deck
				
				// Si on trouve le nom dans la liste et que la couleur est identique on va modifier l'enregistrement
				// Dans le cas contraire nous allons ajouter un enregistrement
				
				if(ceConnard.getName().equals(cematch.getName()) && ceConnard.getColors().equals(cematch.getColors()))
				{
					bFound=true;
					break;
				}
			}
			try 
			{				
				if(bFound)
				{
					// Le joueur existe déjà...
					System.err.println("PLAYER EXISTS !!");

					int seekfor=listeEnnemis.indexOf(ceConnard);												// récupérer l'index dans la liste...
					//System.err.println("List index: "+seekfor);
					//System.err.println("DB id: "+ceConnard.getSQLid());

					// opérer les modifications uniquement sur cet enregistrement...

					listeEnnemis.get(seekfor).addClash();

					switch(MatchResult)
					{
						case 1: listeEnnemis.get(seekfor).addVictory();
										break;
						case 2: listeEnnemis.get(seekfor).addConcede();
										break;
						case 3: listeEnnemis.get(seekfor).addDefeat();
										break;
						case 4: listeEnnemis.get(seekfor).addDraw();
										break;
					}

					int scoreP=listeEnnemis.get(seekfor).getScoreP();
					int scoreE=listeEnnemis.get(seekfor).getScoreE();

					scoreP+=cematch.getScore(true);			// true c'est moi
					scoreE+=cematch.getScore(false);		// false c'est lui

					listeEnnemis.get(seekfor).setScoreE(scoreE);
					listeEnnemis.get(seekfor).setScoreP(scoreP);
					
					listeEnnemis.get(seekfor).setColors(ceConnard.getColors());

					// BUG ici la couleur n'était pas mise à jour :{
					listeEnnemis.get(seekfor).updatedb(LaConnection);
					System.err.println("[FenetrePrincipale] updatedb DONE !!");
					
					RefreshTable();
				}
				else
				{
					// Il n'existe pas encore dans la base de données...
					System.err.println("NEW PLAYER or NEW set of colors for this player !!");

					ceConnard.Reset(); // oui il faut remettre l'objet à zéro sinon on va "hériter" des merdes résultantes de la recherche dans la liste...
					
					ceConnard.setName(cematch.getName().replace("\\", "\\\\"));
					ceConnard.setName(cematch.getName().replace("'", "\\'"));
					ceConnard.setClashes(1);
					ceConnard.setColors(enemyColors.DeckColors);
					ceConnard.setScoreE(Integer.valueOf(jTextFieldEnemyScore.getText()));
					ceConnard.setScoreP(Integer.valueOf(jTextFieldMyScore.getText()));
					switch(MatchResult)
					{
						case 1: ceConnard.addVictory();					// victoire contre moi (ou que j'ai concédé)
										break;	
						case 2: ceConnard.addConcede();					// il a concédé
										break;	
						case 3: ceConnard.addDefeat();					// il a été martyrisé par mes services et ma stratégie
										break;
						case 4: ceConnard.addDraw();
										break;
					}
					
					ceConnard.insertdb(LaConnection);
					System.err.println("[FenetrePrincipale] insertdb DONE !!");
					
				
					
					// Lié au bug de la recherche
					// JE NE SAIS PAS POURQUOI "bidon" est déjà dans la liste des ennemis alors que je ne lui ai rien demandé ???????
					// En effet, mis à part dans PopulateTable() je ne fais aucun accès à listeEnnemis.add() autre part (?????)
					
					//if(!listeEnnemis.contains(ceConnard))
					//	listeEnnemis.add(ceConnard);      // explique pourquoi "bidon" apparaît deux fois dans la liste
					
					PopulateTable(); // ici c'est "normal" vu que je vais lire la base de données (ce que je voulais éviter mais apparemment ça ne marche que comme ça :{ )
					
				}
								
				// Normalement que ce soit une insertion ou un update ceConnard est connu...
				
				cematch.setDBPlayerID(ceConnard.getPlayerID());
				cematch.SaveMeToDB(LaConnection);																				// l'id du match est connue ici !!
				System.err.println("[FenetrePrincipale] SaveMeToDB DONE !!");
				System.err.println("DB id Players: "+ceConnard.getPlayerID());
				System.err.println("DB id Matches: "+cematch.getMatchID());					
				
					// 24 avril 2020
					if(cematch.getLevel(false)==20) // Traitement du niveau spécial Mythic
					{
						// Pour ne pas ajouter de la complexité le niveau mythic sera géré dans une table à part...
						ComputeMythicFixture(cematch);
					}
				
				jTextFieldDate.setText("");
				jTextFieldMatchDuration.setText("");
				jTextFieldOADefeats.setText("");
				jTextFieldOAVictories.setText("");
				jTextFieldMatchTurns.setText("");
				
				switch(MatchResult)
				{
					case 1:	Statistiques.computeResults('D');
									//ToolTipForStats="Defeats suffered by the player against "+cematch.getName(); // TODO: vérifier que ce soit utile... depuis UEPTable... 
									break;
					case 2: Statistiques.computeResults('C');
									//ToolTipForStats="Concedes by the player "+cematch.getName();
									break;
					case 3:	Statistiques.computeResults('V');
									//ToolTipForStats="Victories againsts the player "+cematch.getName();
									break;
					case 4: Statistiques.computeResults('E');
									//ToolTipForStats="Draws with the player "+cematch.getName();
									break;
				}
				
				jTextFieldOAVictories.setText("Victories: "+getVictories());
				jTextFieldOADefeats.setText("Defeats: "+getDefeats());
				String prefix="Match "+String.valueOf(cematch.getMatchID());
				jTextFieldDate.setText("Match fix: "+cematch.getBeginDate());
				jTextFieldMatchDuration.setText("Match duration: "+cematch.getDuration());
				jTextFieldMatchTurns.setText("Turns: "+cematch.getTurns());
				jTextFieldDate.setToolTipText(prefix);
				//jTFMyMythicLevel.setText("0");
			} 
			catch (SQLException ex) 
			{
				//Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
				System.err.println("updatedatas :{");
				System.err.println(ex.getMessage());
				System.err.println(ex.getCause());
			}
			
			// Préparer une nouvelle partie...
						
			Tours=0;
			jLabelTours.setText("Turns");
			jTextFieldEnemyScore.setText("20");
			jTextFieldMyScore.setText("20");
			jSliderResults.setValue(1);
			jTextField_enemy.setText("");
			jStatsManasTours.setText(cematch.getManas()+" manas noires/"+cematch.getTurns()+" tours");
			
			cematch=new classMatch();
			if(lastIndexForEnemyLevel!=-1) jComboBoxEnemyLevel.setSelectedIndex(lastIndexForEnemyLevel);
			if(lastIndexForMyLevel!=-1) jComboBoxMyLevel.setSelectedIndex(lastIndexForMyLevel);
			setIcons(new BitSet(5));
			// ne pas oublier l'objet enemyColors...
			enemyColors.DeckColors=new BitSet(5);  
			jSpinnerManaNoires.setValue(0);
			jTFEnMythicLevel.setText("0");
			
			jTextAreaCommentaires.setText("");
			jTextAreaCommentaires.setEditable(true);
			//jTextField_enemy.requestFocus();
			jTextAreaCommentaires.requestFocus();
		}
  }//GEN-LAST:event_updatedatas

	private void PopulateTable() throws SQLException
	{
		if(!listeEnnemis.isEmpty())
			listeEnnemis.clear();  // sinon la taille augmente sans cesse...
		if(isConnected)
		{
			Statement=LaConnection.createStatement();
			bStatusRequest=Statement.execute("SELECT * FROM Players");
			if(bStatusRequest)
			{
				// Il y a des informations
				
				Resultats = Statement.getResultSet();
				while(Resultats.next())
				{
					// Récupérer les données
					java.sql.ResultSetMetaData MetaDonnees=Resultats.getMetaData();
					int NbChamps=MetaDonnees.getColumnCount();
					for(int cptchamps=0;cptchamps<NbChamps;cptchamps++)
					{
						String unecolonne=ParseSQL(MetaDonnees.getColumnTypeName(cptchamps+1),Resultats,cptchamps+1);
						lesdonnees.add(unecolonne);
					} // sortie de la boucle concernant les colonnes pour chaque enregistrement (éléments du result set)
					PackDatasFromDB(lesdonnees);
					lesdonnees.clear();
				}
				Resultats.close();
				if(ModeleTable.getColumnCount()>=1) ModeleTable.ClearDatas();
				
				for(int cptEnnemis=listeEnnemis.size()-1;cptEnnemis>=0;cptEnnemis--) ModeleTable.addRow(listeEnnemis.get(cptEnnemis));	
			}
			Statement.close();
		}
	}
	
	
	private void RefreshTable()
	{
		ModeleTable.ClearDatas();
		for(int cptEnnemis=listeEnnemis.size()-1;cptEnnemis>=0;cptEnnemis--) ModeleTable.addRow(listeEnnemis.get(cptEnnemis));
	}
	
  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    System.err.println("Fenêtre fermée !!");
		if(LaConnection!=null) 
		{
			try 
			{
				LaConnection.commit();	// on ne sait jamais...
				LaConnection.close();
			} 
			catch (SQLException ex) 
			{
				// Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
				System.err.println("formWindowClosing");
				System.err.println(ex.getMessage());
				System.err.println(ex.getCause());
			}
		}
  }//GEN-LAST:event_formWindowClosing
	
	/**
	 * This method helps the player to select a player in the list
	 * <The begin date is set HERE>
	 * @param evt 
	 */
  private void SelectionJoueur(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SelectionJoueur
		
		int Row=((JTable)evt.getSource()).getSelectedRow();
		
		
		// AVRIL 2020
		// TODO: essayer de détecter le clic du milieu sur la frame et non la table...		
		
		if(evt.getButton()==2) 
		{
			//if(cematch!=null) ProceedMiddleClick(cematch.getName());
			ProceedMiddleClick();
			return;
		}
		/*if(evt.getButton()==3) 
		{
			if(cematch!=null) ProceedRightClick(cematch.getName());
			return;
		}*/
		
		if(Row==-1) return;
			
		// copie le nom de l'élément sélectionné dans le contrôle jTextField
		
		jTextField_enemy.setText((String)ModeleTable.getValueAt(Row, ma_tablemodel.NAME));
		
		// copie les couleurs de la table vers la ligne des icones
		
		ma_Couleurs couleursimportees=(ma_Couleurs)ModeleTable.getValueAt(Row, ma_tablemodel.COL);		
		
		int repint=couleursimportees.getInt();
		
		if((repint&ma_Couleurs.VALNOIR)==ma_Couleurs.VALNOIR) jCheckBoxNoir.setSelected(true);
		else jCheckBoxNoir.setSelected(false);
		if((repint&ma_Couleurs.VALROUGE)==ma_Couleurs.VALROUGE) jCheckBoxRouge.setSelected(true);
		else jCheckBoxRouge.setSelected(false);
		if((repint&ma_Couleurs.VALVERT)==ma_Couleurs.VALVERT) jCheckBoxVert.setSelected(true);
		else jCheckBoxVert.setSelected(false);
		if((repint&ma_Couleurs.VALBLEU)==ma_Couleurs.VALBLEU) jCheckBoxBleu.setSelected(true);
		else jCheckBoxBleu.setSelected(false);
		if((repint&ma_Couleurs.VALBLANC)==ma_Couleurs.VALBLANC) jCheckBoxBlanc.setSelected(true);
		else jCheckBoxBlanc.setSelected(false);
		
		oldValues_col=new BitSet(5);
		
		if((repint&ma_Couleurs.VALNOIR)==ma_Couleurs.VALNOIR) oldValues_col.set(ma_Couleurs.NOIR);
		if((repint&ma_Couleurs.VALROUGE)==ma_Couleurs.VALROUGE) oldValues_col.set(ma_Couleurs.ROUGE);
		if((repint&ma_Couleurs.VALVERT)==ma_Couleurs.VALVERT) oldValues_col.set(ma_Couleurs.VERT);
		if((repint&ma_Couleurs.VALBLEU)==ma_Couleurs.VALBLEU) oldValues_col.set(ma_Couleurs.BLEU);
		if((repint&ma_Couleurs.VALBLANC)==ma_Couleurs.VALBLANC) oldValues_col.set(ma_Couleurs.BLANC);
				
		enemyColors.DeckColors=couleursimportees.DeckColors;
		
		
		// Concept: si le joueur utilise des autres couleurs, on AJOUTE une nouvelle entrée avec le nom du joueur
		// mais avec un set de couleurs différentes...
		if(cematch==null) 
		{
			cematch=new classMatch();
			cematch.setBegin();
		}
		
		try 
		{
			String tmp=getCommentsFromPlayer(jTextField_enemy.getText());
			if(!tmp.isEmpty())
				jTextAreaCommentaires.setText(tmp);
			jTextAreaCommentaires.setEditable(false);
			// TODO: ajouter les matches joués contre ce joueur dans la liste, peu importe le résultat
			
			Statistiques.listMatchesAgainstPlayer(jTextField_enemy.getText());
			
			// TODO: déterminer le  niveau du joueur sélectionné...
			
			setSelectedEnemyLastLevel(jTextField_enemy.getText());
	
		} 
		catch (SQLException ex) 
		{
			//Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("SelectionJoueur");
			System.err.println(ex.getMessage());
			System.err.println(ex.getCause());
		}
		jTextFieldDate.setToolTipText("Fixture date");
		//ToolTipForStats="Fixtures with player "+jTextField_enemy.getText();
  }//GEN-LAST:event_SelectionJoueur

	 private void SelectionMatch(java.awt.event.MouseEvent evt) 
	 {
    int row=((JTable)evt.getSource()).getSelectedRow();
    // int column=((JTable)evt.getSource()).getSelectedColumn();

		jTextAreaCommentaires.setText("");
		
		classMatch ctmp=ma_Statistiques.ListeDesMatches.get(row);
		if(ctmp!=null)
    {
			String prefix="Match "+String.valueOf(ctmp.getMatchID());
			jTextFieldDate.setText("Match fix: "+ctmp.getBeginDate());
			jTextFieldMatchDuration.setText("Match duration: "+ctmp.getDuration());
			jTextFieldMatchTurns.setText("Turns: "+ctmp.getTurns());
			try 
			{
				jTextFieldOAVictories.setText("Victories: "+getVictories());
				jTextFieldOADefeats.setText("Defeats: "+getDefeats());
			} 
			catch (SQLException ex) 
			{
				//Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
				System.err.println("SelectionMatch :{");
				System.err.println(ex.getMessage());
				System.err.println(ex.getCause());
			}
			
			jStatsManasTours.setText(ctmp.getManas()+" manas noires/"+ctmp.getTurns()+" tours");
								
			// Chercher le commentaire du match dans la base de données...
			// Puis ajouter le texte...
			jTextAreaCommentaires.setEditable(true);
			jTextFieldDate.setToolTipText(prefix);
			ChargerCommentaires(ctmp.getMatchID());
    }
  }
	
	
  private void AjouterTour(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterTour
    String oldvalue=jLabelTours.getText();
		try
		{
			Tours=Integer.valueOf(oldvalue);
			Tours++;
			jLabelTours.setText(String.valueOf(Tours));
		}
		catch(NumberFormatException ex)
		{
			Tours=1;
			jLabelTours.setText(String.valueOf(Tours));
		}
  }//GEN-LAST:event_AjouterTour
	
	/**
	 * As we type there is a selection in the list related to the pattern typed
	 * <The Begin date is set here>
	 * @param evt 
	 */
  private void displayAsTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_displayAsTyped
    String seekfor=jTextField_enemy.getText();
		boolean bFound=false;
		
		// Nous allons chercher dans la liste des ennemis si un nom n'apparaîtrait pas dans celle-ci...
		
		//System.err.println("DEBUG: "+evt.getExtendedKeyCode());
		
		int nbreElem=listeEnnemis.size();
	
		// recherche séquentielle, il est vrai TRES peu performante...
		// ...et les regex (expressions régulières) dans JAVA c'est caca... 
		
		jTablePlayer.setToolTipText(null);
		ModeleTable.ClearDatas();
		jTablePlayer.revalidate();
		jTablePlayer.repaint();
		nbreElem--;
		while(nbreElem>=0)
		{
			String comparaison=((classEnemy)listeEnnemis.get(nbreElem)).getName();
			
			if(comparaison.contains(seekfor))
			{
				// Il faudra virer les éléments de la table et ne mettre que ceux qui correspondent... (il n'y a pas, dans le modèle, moyen d'enlever une ligne)
				
				// BUG: il y a deux éléments ajoutés dans le cas des tests avec "bidon" la liste n'est donc pas cohérente :{
				
				// 1. insérer un player "bidon" sans couleur
				// 2. chercher "bidon" --> il y aura deux "bidon" dans la liste au lieu d'un seul...
				// Lorsque "bidon" n'est pas encore dans la liste je l'ajoute
				// Lorsque je recherche un "bidon" juste après il va encore l'ajouter dans la liste
				
				// essai du contournement du bug
				
				// System.err.println(seekfor+"<->"+comparaison);
				bFound=true;
				
				ModeleTable.addRow(listeEnnemis.get(nbreElem));
				jTablePlayer.setToolTipText("Select one row to set the name of the player if present");
			}
			
			jTablePlayer.revalidate();
			jTablePlayer.repaint();
			nbreElem--;
		}
		
		if(!bFound || seekfor.isBlank())
		{
			enemyColors.DeckColors.clear();
		
			jCheckBoxNoir.setSelected(false);
			jCheckBoxRouge.setSelected(false);
			jCheckBoxVert.setSelected(false);
			jCheckBoxBleu.setSelected(false);
			jCheckBoxBlanc.setSelected(false);
		}
		
		if(cematch==null)
		{
			cematch=new classMatch();
			cematch.setBegin();
		}
  }//GEN-LAST:event_displayAsTyped

  private void selectionMyLevel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionMyLevel
    lastIndexForMyLevel=((JComboBox)evt.getSource()).getSelectedIndex();
		String value=String.valueOf(((JComboBox)evt.getSource()).getItemAt(lastIndexForMyLevel));
				
		if(cematch!=null)
		{
			cematch.setLevel(true,value);
		}
		
		jTextField_enemy.requestFocus();
  }//GEN-LAST:event_selectionMyLevel

  private void SelectionEnemyLevel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectionEnemyLevel
    lastIndexForEnemyLevel=((JComboBox)evt.getSource()).getSelectedIndex();
		String value=String.valueOf(((JComboBox)evt.getSource()).getItemAt(lastIndexForEnemyLevel));
		
		if(cematch!=null)
		{
			cematch.setLevel(false,value);
		}   
		jTextField_enemy.requestFocus();
  }//GEN-LAST:event_SelectionEnemyLevel

  private void modifyManasNoires(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_modifyManasNoires
    int value=(int)jSpinnerManaNoires.getValue();
		if(value<0) jSpinnerManaNoires.setValue(0);
		
  }//GEN-LAST:event_modifyManasNoires

  private void SauvegarderCommentaires(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SauvegarderCommentaires
    
		if(jTextAreaCommentaires.isEditable())
		{
			try 
			{
				if(LaConnection.isValid(1))
				{
					System.err.println("Comments SAVED !!");
					
					// j'avais oublié que dans les bases de données ça ne passe pas les backslashes (il y a des connards qui mettent ça dans leur alias), apostrophes
					String tttcarinterdits=jTextAreaCommentaires.getText().replace("\\", "\\\\");											
					tttcarinterdits=tttcarinterdits.replace("'", "\\'");
					
					String SQLRequest="INSERT INTO Comments (Comments,idMatch) VALUES ('";
					SQLRequest+=tttcarinterdits+"',";
					SQLRequest+=classMatch.getLastInsertedID(LaConnection);
					SQLRequest+=")";
										
					Statement=LaConnection.createStatement();
					Statement.execute(SQLRequest);
					Statement.close();
				}
			} 
			catch (SQLException ex) 
			{
				//Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
				System.err.println("SauvegarderCommentaires :{");
				System.err.println(ex.getMessage());
				System.err.println(ex.getCause());
			}
		}
		jTextAreaCommentaires.setText("");
		jTextAreaCommentaires.setEditable(false); 
		jStatsManasTours.setText("Manas/Tours");
		jTextField_enemy.requestFocus();
  }//GEN-LAST:event_SauvegarderCommentaires
	
  private void clicsouris(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clicsouris
    int button=evt.getButton();
		if(button==3)
		{
			if(!jLabelTours.getText().contains("Turns"))
			{
				Tours=Integer.valueOf(jLabelTours.getText());
				Tours--;
				if(Tours<=1) Tours=1;
				jLabelTours.setText(String.valueOf(Tours));
			}
		}
  }//GEN-LAST:event_clicsouris

  private void jPanelPanneauClic(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jPanelPanneauClic
  {//GEN-HEADEREND:event_jPanelPanneauClic
    // AVRIL 2020
		// DONE	 ^*^ 
		if(evt.getButton()==2) 
		{
			ProceedMiddleClick();
			//System.err.println("CLIC !!");
		}
  }//GEN-LAST:event_jPanelPanneauClic

  private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
  {//GEN-HEADEREND:event_formMouseClicked
    // AVRIL 2020
		// DONE	 ^*^ 
		if(evt.getButton()==2) 
		{
			ProceedMiddleClick();
			//System.err.println("CLIC !!");
		}
  }//GEN-LAST:event_formMouseClicked

  private void jTextFieldEnemyScoreKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTextFieldEnemyScoreKeyPressed
  {//GEN-HEADEREND:event_jTextFieldEnemyScoreKeyPressed
    if( (evt.getKeyChar() >= '0' && evt.getKeyChar() <='9') || 
				evt.getKeyCode()==KeyEvent.VK_BACK_SPACE || 
				evt.getKeyCode()==KeyEvent.VK_DELETE || 
				evt.getKeyChar()=='-') jTextFieldEnemyScore.setEditable(true);
		else jTextFieldEnemyScore.setEditable(false);
  }//GEN-LAST:event_jTextFieldEnemyScoreKeyPressed

  private void jTextFieldMyScoreKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTextFieldMyScoreKeyPressed
  {//GEN-HEADEREND:event_jTextFieldMyScoreKeyPressed
    if(	(evt.getKeyChar() >= '0' && evt.getKeyChar() <='9') || 
				evt.getKeyCode()==KeyEvent.VK_BACK_SPACE || 
				evt.getKeyCode()==KeyEvent.VK_DELETE || 
				evt.getKeyChar()=='-') jTextFieldEnemyScore.setEditable(true);
		else jTextFieldMyScore.setEditable(false);
  }//GEN-LAST:event_jTextFieldMyScoreKeyPressed

  private void jTFEnMythicLevelKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTFEnMythicLevelKeyPressed
  {//GEN-HEADEREND:event_jTFEnMythicLevelKeyPressed
    if( (evt.getKeyChar() >= '0' && evt.getKeyChar() <='9') || 
				evt.getKeyCode()==KeyEvent.VK_BACK_SPACE || 
				evt.getKeyCode()==KeyEvent.VK_DELETE || 
				evt.getKeyChar()=='-') jTextFieldEnemyScore.setEditable(true);
		else jTFEnMythicLevel.setEditable(false);
  }//GEN-LAST:event_jTFEnMythicLevelKeyPressed

  private void jTFMyMythicLevelKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTFMyMythicLevelKeyPressed
  {//GEN-HEADEREND:event_jTFMyMythicLevelKeyPressed
		if(	(evt.getKeyChar() >= '0' && evt.getKeyChar() <='9') || 
				evt.getKeyCode()==KeyEvent.VK_BACK_SPACE || 
				evt.getKeyCode()==KeyEvent.VK_DELETE || 
				evt.getKeyChar()=='-') jTextFieldEnemyScore.setEditable(true);
		else jTFMyMythicLevel.setEditable(false);   
  }//GEN-LAST:event_jTFMyMythicLevelKeyPressed
		
	private void ChargerCommentaires(int MatchID)
	{
		if(jTextAreaCommentaires.isEditable())
		{
			try 
			{
				if(LaConnection.isValid(1))
				{
					String SQLRequest="SELECT Comments FROM Comments WHERE idMatch="+MatchID;
					Statement=LaConnection.createStatement();
					Statement.execute(SQLRequest);
					Resultats=Statement.getResultSet();
					if(Resultats.first())
					{
						//String previous=jTextAreaCommentaires.getText();
						jTextAreaCommentaires.setText(Resultats.getString(1));
					}
					Resultats.close();
					Statement.close();
				}
			} 
			catch (SQLException ex) 
			{
				System.err.println("ChargerCommentaires :{");
				System.err.println(ex.getMessage());
				System.err.println(ex.getCause());
			}
		}
		jTextAreaCommentaires.setEditable(false);
	}
	
	
	/**
	 * Java entry point
	 * @param args the command line arguments
	 */
	public static void main(String args[]) 
	{
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try 
		{
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) 
			{
				if ("Nimbus".equals(info.getName())) 
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) 
		{
			java.util.logging.Logger.getLogger(FenetrePrincipale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() -> {
			JFrame MaFrame;
			try 
			{
				MaFrame = new FenetrePrincipale();
				MaFrame.setVisible(true);
			}
			catch (SQLException ex)
			{
				Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}
	
	// Mes méthodes
	
	private String ParseSQL(String type,java.sql.ResultSet source,int colonne) throws SQLException
	{
		String tmp=new String();
		
		if(type.contains("SMALLINT")) tmp=String.valueOf(source.getInt(colonne));
		if(type.contains("CHAR")) tmp=source.getString(colonne);
		if(type.contains("BIT")) tmp=String.valueOf(source.getByte(colonne));
		
		return tmp;
	}
	
	private boolean PackDatasFromDB(LinkedList<Object> source)
	{
		classEnemy tmp=new classEnemy();
		BitSet tmpbs=new BitSet(5);
		int repint;
		
		tmp.setSQLid(Integer.valueOf((String)source.get(IDPLAYER)));
		tmp.setName((String)source.get(PLAYERNAME));
		repint=Integer.valueOf((String)source.get(PLAYERCOL));
		
		if((repint&ma_Couleurs.VALNOIR)==ma_Couleurs.VALNOIR) tmpbs.set(ma_Couleurs.NOIR);
		if((repint&ma_Couleurs.VALROUGE)==ma_Couleurs.VALROUGE) tmpbs.set(ma_Couleurs.ROUGE);
		if((repint&ma_Couleurs.VALVERT)==ma_Couleurs.VALVERT) tmpbs.set(ma_Couleurs.VERT);
		if((repint&ma_Couleurs.VALBLEU)==ma_Couleurs.VALBLEU) tmpbs.set(ma_Couleurs.BLEU);
		if((repint&ma_Couleurs.VALBLANC)==ma_Couleurs.VALBLANC) tmpbs.set(ma_Couleurs.BLANC);
		
		tmp.setColors(tmpbs);
		//oldValues_col=tmpbs;
		
		tmp.setVictories(Integer.valueOf((String)source.get(PLAYERVIC)));
		tmp.setDefeats(Integer.valueOf((String)source.get(PLAYERDEF)));
		tmp.setConcedes(Integer.valueOf((String)source.get(PLAYERCON)));
		tmp.setDraws(Integer.valueOf((String)source.get(PLAYERDRW)));
		
		tmp.setScoreE(Integer.valueOf((String)source.get(PLAYERSCOREE)));  
		tmp.setScoreP(Integer.valueOf((String)source.get(PLAYERSCOREP)));
		
		tmp.setClashes(Integer.valueOf((String)source.get(PLAYERCSH)));
		
		return listeEnnemis.add(tmp);
	}

	private void setIcons(BitSet color)
	{
		jCheckBoxNoir.setSelected(color.get(ma_Couleurs.NOIR));
		jCheckBoxRouge.setSelected(color.get(ma_Couleurs.ROUGE));
		jCheckBoxVert.setSelected(color.get(ma_Couleurs.VERT));
		jCheckBoxBleu.setSelected(color.get(ma_Couleurs.BLEU));
		jCheckBoxBlanc.setSelected(color.get(ma_Couleurs.BLANC));
	}
	
	private int getVictories() throws SQLException
	{
		int tmp=-1;
		String SQLRequest="SELECT COUNT(Result) FROM Matches WHERE Result='C' OR Result='V'";
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			Statement.execute(SQLRequest);
			
			Resultats=Statement.getResultSet();
			
			if(Resultats!=null)
			{
				if(!Resultats.first()) return tmp;							// peu de chances d'avoir deux enregistrements dans le resulset à moins que je sois une grosse merde (oui mais si la liste est vide...)
				
				tmp=Resultats.getInt(1);
			}
			Resultats.close();
			Statement.close();
			
		}
		return tmp;
	}
	
	private int getDefeats() throws SQLException
	{
		int tmp=-1;
		String SQLRequest="SELECT COUNT(Result) FROM Matches WHERE Result='D'";
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			Statement.execute(SQLRequest);
			
			Resultats=Statement.getResultSet();
			if(Resultats!=null)
			{
				if(!Resultats.first()) return tmp;							// peu de chances d'avoir deux enregistrements dans le resulset à moins que je sois une grosse merde (oui mais si la liste est vide...)
				
				tmp=Resultats.getInt(1);
			}
			Resultats.close();
			Statement.close();
			
		}
		return tmp;
	}
	
	private void ProceedRightClick(String current)
	{
		// Afficher les matches
		System.err.println("Clic droit");
	}
	
	private void ProceedMiddleClick()
	{
		// Afficher la fenêtre de statistiques

		FenetreStatistiques=new superStats(LaConnection);
		
		int posXRef=super.getLocation().x;
		int posYRef=super.getLocation().y;
		
		int widthref=super.getWidth();
		int heightref=super.getHeight();
		
		int width=FenetreStatistiques.getWidth();
		int height=FenetreStatistiques.getHeight();
				
		FenetreStatistiques.setLocation(posXRef+((widthref-width)/2),posYRef+((heightref-height)/2));
		jTextField_enemy.requestFocus();
	}
	
	private String getCommentsFromPlayer(String playername) throws SQLException
	{
		String tmp="";
		
		// BUG 1 février 2020
		playername=playername.replace("\\", "\\\\"); // Comment gérer les connards qui mettent un backslash dans leurs alias ???
		playername=playername.replace("'", "\\'");
		
		
		// BUG Avril 2020
		
		String SQLRequest="SELECT Matches.idMatch,Comments FROM Comments, Matches WHERE Matches.idMatch=Comments.idMatch AND Matches.idPlayer IN (SELECT idPlayer FROM Players WHERE Alias= BINARY '"+playername+"')";
		
		Statement=LaConnection.createStatement();
		Statement.execute(SQLRequest);
			
		Resultats=Statement.getResultSet();
		if(Resultats!=null)
		{
			if(!Resultats.first()) return "";							
			do
			{
				tmp+="[ Match "+Resultats.getInt(1)+"]\n";
				tmp+=Resultats.getString(2);
				tmp+="\n-----\n";
			}while(Resultats.next());
		}
		Resultats.close();
		Statement.close();
		
		return tmp;
	}

	private void setSelectedEnemyLastLevel(String param) throws SQLException
	{
		int tmp=-1;
		
		// hihi ^^
		
		param=param.replace("\\", "\\\\"); // Comment gérer les connards qui mettent un backslash dans leurs alias ???
		param=param.replace("'", "\\'");
		
		// Il faut indiquer BINARY sinon 'David' sera la même chose que 'david' alors que ce sont deux joueurs différents !! (BUG avril 2020)
		String SQLRequest="SELECT EnLvl FROM Matches WHERE idPlayer = (SELECT idPlayer FROM Players WHERE Alias= BINARY '"+param+"' ORDER BY idPlayer DESC LIMIT 1) ORDER BY EnLvl DESC LIMIT 1";
		Statement=LaConnection.createStatement();
		Statement.execute(SQLRequest);
			
		Resultats=Statement.getResultSet();
		if(Resultats!=null) 
		{
			Resultats.first();					// Il ne devrait y avoir qu'un seul enregistrement sélectionné (mais je pourrais être une grosse merde aussi... à tester ;) )
			tmp=Resultats.getInt(1);
		}
		Resultats.close();
		Statement.close();
		
		if(tmp!=-1) 
		{
			jComboBoxEnemyLevel.setSelectedIndex(tmp);
		}
	}
	

	private void setMyLevel() throws SQLException 
	{
		int tmp=-1;
		
		String SQLRequest="SELECT MyLvl FROM Matches ORDER BY StartTime DESC LIMIT 1";
		Statement=LaConnection.createStatement();
		Statement.execute(SQLRequest);
			
		Resultats=Statement.getResultSet();
		if(Resultats!=null) 
		{
			// bug 2020 -- quand la base de données est vide --> CACA
			
			if(Resultats.getRow()==0) 
			{
				jComboBoxMyLevel.setSelectedIndex(0);
				Resultats.close();
				Statement.close();
				return;
			}
			
			Resultats.first();					
			tmp=Resultats.getInt(1);
		}
		Resultats.close();
		Statement.close();
		
		if(tmp!=-1) jComboBoxMyLevel.setSelectedIndex(tmp);
	}
	
	private void setEnLevel() throws SQLException
	{
		int tmp=-1;
		
		String SQLRequest="SELECT EnLvl FROM Matches ORDER BY StartTime DESC LIMIT 1";
		Statement=LaConnection.createStatement();
		Statement.execute(SQLRequest);
			
		Resultats=Statement.getResultSet();
		if(Resultats!=null) 
		{
			if(Resultats.getRow()==0) 
			{
				jComboBoxEnemyLevel.setSelectedIndex(0);
				Resultats.close();
				Statement.close();
				return;
			}
			
			Resultats.first();					
			tmp=Resultats.getInt(1);
		}
		Resultats.close();
		Statement.close();
		
		if(tmp!=-1) jComboBoxEnemyLevel.setSelectedIndex(tmp);
	}
	
	private void ComputeMythicFixture(classMatch param) throws SQLException
	{
		// besoin de l'identifiant du match
		int idMatch=param.getMatchID();
		System.err.println("ComputeMythicFixture: idMatch "+idMatch);
		
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			
			String SQLRequest="INSERT INTO Mythic (idMatch,PourcentageEn,PourcentagePl) VALUES (";
			SQLRequest+=idMatch+",";
			SQLRequest+=jTFEnMythicLevel.getText()+",";
			SQLRequest+=jTFMyMythicLevel.getText()+")";
			
			Statement.execute(SQLRequest);
			Statement.close();
		}
	}
	
  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JButton jButtonAddTurn;
  public javax.swing.JButton jButtonUpdate;
  private javax.swing.JCheckBox jCheckBoxBlanc;
  private javax.swing.JCheckBox jCheckBoxBleu;
  private javax.swing.JCheckBox jCheckBoxNoir;
  private javax.swing.JCheckBox jCheckBoxRouge;
  private javax.swing.JCheckBox jCheckBoxVert;
  public javax.swing.JComboBox<String> jComboBoxEnemyLevel;
  public javax.swing.JComboBox<String> jComboBoxMyLevel;
  public javax.swing.JLabel jLabelManas;
  public javax.swing.JLabel jLabelTours;
  public javax.swing.JPanel jPanelPanneau;
  private javax.swing.JPanel jPanelResults;
  private javax.swing.JScrollPane jScrollPaneComments;
  private javax.swing.JScrollPane jScrollPaneMatchesExt;
  private javax.swing.JScrollPane jScrollPanePlayers;
  public javax.swing.JSlider jSliderResults;
  private javax.swing.JSpinner jSpinnerManaNoires;
  private javax.swing.JLabel jStatsManasTours;
  private javax.swing.JTextField jTFEnMythicLevel;
  private javax.swing.JTextField jTFMyMythicLevel;
  public javax.swing.JTable jTablePlayer;
  public javax.swing.JTextArea jTextAreaCommentaires;
  public javax.swing.JTextField jTextFieldDate;
  public javax.swing.JTextField jTextFieldEnemyScore;
  public javax.swing.JTextField jTextFieldMatchDuration;
  public javax.swing.JTextField jTextFieldMatchTurns;
  public javax.swing.JTextField jTextFieldMyScore;
  public javax.swing.JTextField jTextFieldOADefeats;
  public javax.swing.JTextField jTextFieldOAVictories;
  public javax.swing.JTextField jTextFieldResultText;
  public javax.swing.JTextField jTextField_enemy;
  // End of variables declaration//GEN-END:variables

	private final ma_tablemodel ModeleTable=new ma_tablemodel();	// Modèle (données) contenues dans la table 
	private final defPlayer AfficheurStandard=new defPlayer();		// Afficheur (dessinateur) des données de la table
	private final ma_Couleurs enemyColors=new ma_Couleurs();			// Couleur(s) du deck de l'ennemi 
		
	private final LinkedList<String> Levels=new LinkedList<>();		// Contient les niveaux 
	private final String[] strPrefixes;														
	private final String[] strPostfixes;
	
	private boolean isConnected=false;														// FLAG indiquant le statut de la connexion à mariadb 
	private boolean bStatusRequest=false;													// FLAG indique le statut de la requête SQL 
	
	private int MatchResult;																			// Résultat du match 
	private int Tours;																						// Nombre de tours du match (joués par moi) 
	
	private LinkedList<classEnemy> listeEnnemis;									// Contient la liste des ennemis 
	private LinkedList<Object> lesdonnees;												// Liste des champs en provenance d'une table (ODBC style) 
	
	private BitSet oldValues_col;																	// (???) ancienne valeur du set de couleurs (5 bits -> 0 à 31) 	
	
	public static int lastIndexForMyLevel=-1;											// (???) ancien indice concernant mon niveau	 
	public static int lastIndexForEnemyLevel=-1;									// (???) ancien indice concernant le niveau de l'ennemi 
	//public static String ToolTipForStats;													// (???) Message pour la JTable de ma_Statistiques (defMatch)	 	
		
	public static classEnemy ceConnard;														// Nouvel ennemi 
	public static classMatch cematch;															// Représente le match en cours... 
	
	// Position des champs
	
	public final int IDPLAYER=0;																	// Position du champs idPlayer 	
	public final int PLAYERNAME=1;																// Position du champs Alias 	
	public final int PLAYERCOL=2;																	// Position du champs Couleurs 
	public final int PLAYERVIC=3;																	// Position du champs Victories 
	public final int PLAYERCON=4;																	// Position du champs Concedes 
	public final int PLAYERDEF=5;																	// Position du champs Defeats 
	public final int PLAYERDRW=6;																	// Position du champs Draws 
	public final int PLAYERSCOREP=7;															// Position du champs MyScore 
	public final int PLAYERSCOREE=8;															// Position du champs HisScore 
	public final int PLAYERCSH=9;																	// Position du champs MatchesDone 
	
	// Statistiques
	
	public ma_Statistiques Statistiques;													// Objet permettant d'obtenir les stats d'un match 
	public superStats FenetreStatistiques;												// Objet permettant d'accéder aux statistiques étendues 
		
	private defMatch innerRenderer=new defMatch();
	private ma_tablemodelmatch innerModel;
	
	// SQL related
	
	private Driver LeDriver;																			// Objet ODBC représentant le pilote 
	private java.sql.Connection LaConnection;											// Objet représentant la connection avec mariaDB 
	private java.sql.Statement Statement;													// Objet représentant une requête SQL 	
	private java.sql.ResultSet Resultats;													// Objet représentant les résultats d'une requête 

	public static String SubVersion=(GregorianCalendar.getInstance().get(GregorianCalendar.MONTH)+1)+"."+GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
	public static String Version="1.";
	
	private UEPTable jTableMatches;
	private UEPInterval verifLevels;
	private UEPElement[] lesIntervales;
}

