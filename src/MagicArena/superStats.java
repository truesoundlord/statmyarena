package MagicArena;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class superStats extends javax.swing.JFrame 
{

	/**
	 * Creates new form superStats 
	 * @param param The mariadb connection instance
	 */
		
	public superStats(java.sql.Connection param)
	{
		this.MDMatches = new LinkedList<>();
		initComponents();
		setVisible(true);
		enemyColors=new ma_Couleurs();
		LaConnection=param;
		Levels=new LinkedList<>();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/iconma.png")));
		setTitle("MagicArena Stats v"+FenetrePrincipale.Version+FenetrePrincipale.SubVersion+" [Statistics]");
		
		// populate the combobox
		
		int cptLevel=0;
		jCBEnemyLevel.removeAllItems();
		jCBEnemyLevel.setSelectedIndex(-1);
		
		for(int cptPrefixes=0;cptPrefixes<strPrefixes.length;cptPrefixes++)
		{
			for(int cptPostfixes=0;cptPostfixes<strPostfixes.length;cptPostfixes++)
			{
				String strLevel=strPrefixes[cptPrefixes]+"("+strPostfixes[cptPostfixes]+")";
				Levels.add(strLevel);
				jCBEnemyLevel.addItem(Levels.get(cptLevel++));
			}		
		}
		jCBEnemyLevel.setSelectedIndex(-1);
		jCBEnemyLevel.setToolTipText("Define the enemy level against which you need statistics");
		
		jPanelForme.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		try 
		{
			getEfficencyIcons(10);
			jPanelForme.setToolTipText("Last 10 matches status...");
			
			String tmpLast=getLast('V');
			String Results;
			if(tmpLast!=null)
			{
				String[] tmpSplit=tmpLast.split("-666-");
				Results=tmpSplit[0]+" ("+tmpSplit[1]+") ["+tmpSplit[2]+" matches ago...]";
				jTFLastRealWin.setText(Results);
			}
			tmpLast=getLast('P');
			if(tmpLast!=null)
			{
				String[] tmpSplit=tmpLast.split("-666-");
				Results=tmpSplit[0]+" ("+tmpSplit[1]+") ["+tmpSplit[2]+" matches ago...]";
				jTFLastRealDefeat.setText(Results);
			}
			tmpLast=getLast('D');
			if(tmpLast!=null)
			{
				String[] tmpSplit=tmpLast.split("-666-");
				Results=tmpSplit[0]+" ("+tmpSplit[1]+") ["+tmpSplit[2]+" matches ago...]";
				jTFLastConcedeByMe.setText(Results);
			}
			tmpLast=getLast('C');
			if(tmpLast!=null)
			{
				String[] tmpSplit=tmpLast.split("-666-");
				Results=tmpSplit[0]+" ("+tmpSplit[1]+") ["+tmpSplit[2]+" matches ago...]";
				jTFLastConcede.setText(Results);
			}
			tmpLast=getLast('E');
			if(tmpLast!=null)
			{
				String[] tmpSplit=tmpLast.split("-666-");
				Results=tmpSplit[0]+" ("+tmpSplit[1]+") ["+tmpSplit[2]+" matches ago...]";
				jTFLastDraw.setText(Results);
			}
			tmpLast=getLast('T');
			if(tmpLast!=null)
			{
				String[] tmpSplit=tmpLast.split("-666-");
				Results=tmpSplit[0]+" ("+tmpSplit[1]+") ["+tmpSplit[2]+" matches ago...]";
				jTFLastCowardry.setText(Results);
			}
						
			GroupeDeBoutons.add(jRBMAXP);
			GroupeDeBoutons.add(jRBMINP);
			GroupeDeBoutons.add(jRBMAXE);
			GroupeDeBoutons.add(jRBMINE);
			
			jRBMAXP.setText(getScoresInfos(ScoreInfo.MAXPP,ScoreInfo.MID));
			jRBMAXP.setToolTipText("Database ID corresponding to this match");
			jRBMINP.setText(getScoresInfos(ScoreInfo.MINPP, ScoreInfo.MID));
			jRBMINP.setToolTipText("Database ID corresponding to this match");
			jRBMAXE.setText(getScoresInfos(ScoreInfo.MAXPE, ScoreInfo.MID));
			jRBMAXE.setToolTipText("Database ID corresponding to this match");
			jRBMINE.setText(getScoresInfos(ScoreInfo.MINPE, ScoreInfo.MID));
			jRBMINE.setToolTipText("Database ID corresponding to this match");
			
			jLBLAliasMAXPScore.setText(getScoresInfos(ScoreInfo.MAXPP,ScoreInfo.Alias));
			jLBLAliasMAXPScore.setToolTipText("Player alias against which you scored the best");
			jLBLAliasMINPScore.setText(getScoresInfos(ScoreInfo.MINPP, ScoreInfo.Alias));
			jLBLAliasMINPScore.setToolTipText("Player alias against which your score was the less");
			jLBLAliasMAXEScore.setText(getScoresInfos(ScoreInfo.MAXPE, ScoreInfo.Alias));
			jLBLAliasMAXEScore.setToolTipText("Player alias which scored the more against you");
			jLBLAliasMINEScore.setText(getScoresInfos(ScoreInfo.MINPE, ScoreInfo.Alias));
			jLBLAliasMINEScore.setToolTipText("Player alias which scored the less against you");
			
			jLBLMAXPScore.setText(getScoresInfos(ScoreInfo.MAXPP, ScoreInfo.SCMP));
			jLBLMAXPScore.setToolTipText("Your maximum score was against "+jLBLAliasMAXPScore.getText());
			jLBLMAXEScore.setText(getScoresInfos(ScoreInfo.MAXPE, ScoreInfo.SCME));
			jLBLMAXEScore.setToolTipText(jLBLAliasMAXEScore.getText()+" scored the maximum against you");
			jLBLMINPScore.setText(getScoresInfos(ScoreInfo.MINPP, ScoreInfo.SCmP));
			jLBLMINPScore.setToolTipText("Your minimum score was against "+jLBLAliasMINPScore.getText());
			jLBLMINEScore.setText(getScoresInfos(ScoreInfo.MINPE, ScoreInfo.SCmE));
			jLBLMINEScore.setToolTipText(jLBLAliasMINEScore.getText()+" scored the less against you");
			
			jLBLMatchScoreVSMAX.setText(getScoresInfos(ScoreInfo.MAXPE, ScoreInfo.SCMP));
			jLBLMatchScoreVSMAX.setToolTipText("Your score against the best "+jLBLAliasMAXEScore.getText()+"'s score...");
			jLBLMatchScoreVSMIN.setText(getScoresInfos(ScoreInfo.MINPE, ScoreInfo.SCmP));
			jLBLMatchScoreVSMIN.setToolTipText("Your score against the worst "+jLBLAliasMINEScore.getText()+"'s score...");
			jLBLMatchOpponentScoreVSMAX.setText(getScoresInfos(ScoreInfo.MAXPP, ScoreInfo.SCME));
			jLBLMatchOpponentScoreVSMAX.setToolTipText(jLBLAliasMAXPScore.getText()+" scored this against your best score...");
			jLBLMatchOpponentScoreVSMIN.setText(getScoresInfos(ScoreInfo.MINPP, ScoreInfo.SCmE));
			jLBLMatchOpponentScoreVSMIN.setToolTipText(jLBLAliasMINPScore.getText()+" scored this against your worst score...");			
			
			jLBLVictoriesInaRow.setText("Victories in a row: "+getInARow('V'));
			jLBLConcedesInaRow.setText("Concedes in a row: "+getInARow('C'));
			jLBLDefeatsInaRow.setText("Defeats in a row: "+getInARow('D'));
			
			ModeleTableMatch=new ma_tablemodelmatch();
			RendererMatch=new defMatch();
			
			LaTable=new UEPTableImpl(ModeleTableMatch,RendererMatch);
			LaTable.addDefaultRenderer(String.class);
			LaTable.addDefaultRenderer(ma_Couleurs.class);
			
			LaTable.setBackground(new java.awt.Color(31, 112, 121));
			LaTable.setCellSelectionEnabled(true);
			LaTable.setAutoCreateColumnsFromModel(false);
		
			LaTable.setShowGrid(false);
			LaTable.getTableHeader().setReorderingAllowed(false);
			LaTable.getTableHeader().setResizingAllowed(true);
			
			LaTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
			LaTable.setRequestFocusEnabled(false);
			LaTable.setRowHeight(40);
			LaTable.setRowSelectionAllowed(false);
			LaTable.setShowHorizontalLines(true);
		
			((DefaultTableCellRenderer) LaTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
			jScrollPanePourTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			jScrollPanePourTable.setViewportView(LaTable);
		} 
		catch (SQLException ex) 
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    GroupeDeBoutons = new javax.swing.ButtonGroup();
    jPanelSuperStats = new javax.swing.JPanel();
    jPanelSStats = new javax.swing.JPanel();
    jCBBlanc = new javax.swing.JCheckBox();
    jCBBleu = new javax.swing.JCheckBox();
    jCBVert = new javax.swing.JCheckBox();
    jCBRouge = new javax.swing.JCheckBox();
    jCBNoir = new javax.swing.JCheckBox();
    jTFVictories = new javax.swing.JTextField();
    jLBLWinTotal = new javax.swing.JLabel();
    jTFDefeats = new javax.swing.JTextField();
    jLBLLstTotal = new javax.swing.JLabel();
    jLBLConTotal = new javax.swing.JLabel();
    jTFConcedes = new javax.swing.JTextField();
    jCBEnemyLevel = new javax.swing.JComboBox<>();
    jLBLWins = new javax.swing.JLabel();
    jLBLDef = new javax.swing.JLabel();
    jLBLCon = new javax.swing.JLabel();
    jPanelForme = new javax.swing.JPanel();
    jPanelLastest = new javax.swing.JPanel();
    jTFLastRealWin = new javax.swing.JTextField();
    jTFLastRealDefeat = new javax.swing.JTextField();
    jTFLastConcedeByMe = new javax.swing.JTextField();
    jTFLastConcede = new javax.swing.JTextField();
    jTFLastDraw = new javax.swing.JTextField();
    jTFLastCowardry = new javax.swing.JTextField();
    jPanelStatsScores = new javax.swing.JPanel();
    jLBLAliasMAXPScore = new javax.swing.JLabel();
    jLBLAliasMINPScore = new javax.swing.JLabel();
    jLBLAliasMAXEScore = new javax.swing.JLabel();
    jLBLAliasMINEScore = new javax.swing.JLabel();
    jLBLMAXPScore = new javax.swing.JLabel();
    jLBLMINPScore = new javax.swing.JLabel();
    jLBLMAXEScore = new javax.swing.JLabel();
    jLBLMINEScore = new javax.swing.JLabel();
    jRBMAXP = new javax.swing.JRadioButton();
    jRBMINP = new javax.swing.JRadioButton();
    jRBMINE = new javax.swing.JRadioButton();
    jRBMAXE = new javax.swing.JRadioButton();
    jLBLMatchOpponentScoreVSMAX = new javax.swing.JLabel();
    jLBLMatchOpponentScoreVSMIN = new javax.swing.JLabel();
    jLBLMatchScoreVSMAX = new javax.swing.JLabel();
    jLBLMatchScoreVSMIN = new javax.swing.JLabel();
    jLBLVictoriesInaRow = new javax.swing.JLabel();
    jLBLDefeatsInaRow = new javax.swing.JLabel();
    jLBLConcedesInaRow = new javax.swing.JLabel();
    jScrollPanePourTable = new javax.swing.JScrollPane();

    setMaximumSize(new java.awt.Dimension(1368, 762));
    setMinimumSize(new java.awt.Dimension(1368, 762));
    setResizable(false);
    setSize(new java.awt.Dimension(1368, 762));

    jPanelSuperStats.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jPanelSuperStats.setMaximumSize(new java.awt.Dimension(1300, 800));
    jPanelSuperStats.setMinimumSize(new java.awt.Dimension(1300, 800));
    jPanelSuperStats.setPreferredSize(new java.awt.Dimension(1300, 800));

    jPanelSStats.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statistiques", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Liberation Mono", 1, 10))); // NOI18N

    jCBBlanc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manablanc.png"))); // NOI18N
    jCBBlanc.setName("White"); // NOI18N
    jCBBlanc.setRolloverEnabled(false);
    jCBBlanc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manablanc.png"))); // NOI18N
    jCBBlanc.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectColor(evt);
      }
    });

    jCBBleu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manableu.png"))); // NOI18N
    jCBBleu.setName("Blue"); // NOI18N
    jCBBleu.setRolloverEnabled(false);
    jCBBleu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manableu.png"))); // NOI18N
    jCBBleu.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectColor(evt);
      }
    });

    jCBVert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manavert.png"))); // NOI18N
    jCBVert.setName("Green"); // NOI18N
    jCBVert.setRolloverEnabled(false);
    jCBVert.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manavert.png"))); // NOI18N
    jCBVert.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectColor(evt);
      }
    });

    jCBRouge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manarouge.png"))); // NOI18N
    jCBRouge.setName("Red"); // NOI18N
    jCBRouge.setRolloverEnabled(false);
    jCBRouge.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manarouge.png"))); // NOI18N
    jCBRouge.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectColor(evt);
      }
    });

    jCBNoir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_mananoir.png"))); // NOI18N
    jCBNoir.setName("Black"); // NOI18N
    jCBNoir.setRolloverEnabled(false);
    jCBNoir.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/mananoir.png"))); // NOI18N
    jCBNoir.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectColor(evt);
      }
    });

    jTFVictories.setEditable(false);
    jTFVictories.setColumns(3);
    jTFVictories.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTFVictories.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFVictories.setText("0");
    jTFVictories.setToolTipText("Victories");
    jTFVictories.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        jTFVictoriesMouseClicked(evt);
      }
    });

    jLBLWinTotal.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLWinTotal.setText("0");
    jLBLWinTotal.setToolTipText("Total victories");

    jTFDefeats.setEditable(false);
    jTFDefeats.setColumns(3);
    jTFDefeats.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTFDefeats.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFDefeats.setText("0");
    jTFDefeats.setToolTipText("Defeats");
    jTFDefeats.addFocusListener(new java.awt.event.FocusAdapter()
    {
      public void focusLost(java.awt.event.FocusEvent evt)
      {
        jTFDefeatsFocusLost(evt);
      }
    });
    jTFDefeats.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        jTFDefeatsMouseClicked(evt);
      }
    });

    jLBLLstTotal.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLLstTotal.setText("0");
    jLBLLstTotal.setToolTipText("Total defeats (and concedes)");

    jLBLConTotal.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLConTotal.setText("0");
    jLBLConTotal.setToolTipText("Total Concedes");

    jTFConcedes.setEditable(false);
    jTFConcedes.setColumns(3);
    jTFConcedes.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jTFConcedes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFConcedes.setText("0");
    jTFConcedes.setToolTipText("Concedes");
    jTFConcedes.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        jTFConcedesMouseClicked(evt);
      }
    });

    jCBEnemyLevel.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jCBEnemyLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jCBEnemyLevel.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        jCBEnemyLevelActionPerformed(evt);
      }
    });

    jLBLWins.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLWins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLBLWins.setText("0");
    jLBLWins.setMaximumSize(new java.awt.Dimension(7, 24));
    jLBLWins.setMinimumSize(new java.awt.Dimension(7, 24));
    jLBLWins.setPreferredSize(new java.awt.Dimension(7, 24));
    jLBLWins.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        jLBLWinsMouseClicked(evt);
      }
    });

    jLBLDef.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLDef.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLBLDef.setText("0");
    jLBLDef.setMaximumSize(new java.awt.Dimension(7, 24));
    jLBLDef.setMinimumSize(new java.awt.Dimension(7, 24));
    jLBLDef.setPreferredSize(new java.awt.Dimension(7, 24));
    jLBLDef.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        jLBLDefMouseClicked(evt);
      }
    });

    jLBLCon.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLCon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLBLCon.setText("0");
    jLBLCon.setMaximumSize(new java.awt.Dimension(7, 24));
    jLBLCon.setMinimumSize(new java.awt.Dimension(7, 24));
    jLBLCon.setPreferredSize(new java.awt.Dimension(7, 24));
    jLBLCon.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(java.awt.event.MouseEvent evt)
      {
        jLBLConMouseClicked(evt);
      }
    });

    jPanelForme.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jPanelForme.setToolTipText("Results on the last (10) matches ");
    jPanelForme.setFocusable(false);
    jPanelForme.setMaximumSize(new java.awt.Dimension(360, 40));
    jPanelForme.setMinimumSize(new java.awt.Dimension(360, 40));

    javax.swing.GroupLayout jPanelFormeLayout = new javax.swing.GroupLayout(jPanelForme);
    jPanelForme.setLayout(jPanelFormeLayout);
    jPanelFormeLayout.setHorizontalGroup(
      jPanelFormeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanelFormeLayout.setVerticalGroup(
      jPanelFormeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 36, Short.MAX_VALUE)
    );

    jPanelLastest.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

    jTFLastRealWin.setToolTipText("Last win");

    jTFLastRealDefeat.setToolTipText("Last defeat");

    jTFLastConcedeByMe.setToolTipText("Last concede by me");

    jTFLastConcede.setToolTipText("Last concede");

    jTFLastDraw.setToolTipText("Last draw");

    jTFLastCowardry.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    jTFLastCowardry.setToolTipText("Last abandon at first turn like a pussy");

    javax.swing.GroupLayout jPanelLastestLayout = new javax.swing.GroupLayout(jPanelLastest);
    jPanelLastest.setLayout(jPanelLastestLayout);
    jPanelLastestLayout.setHorizontalGroup(
      jPanelLastestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelLastestLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelLastestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTFLastRealWin, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
          .addComponent(jTFLastRealDefeat)
          .addComponent(jTFLastConcedeByMe)
          .addComponent(jTFLastConcede)
          .addComponent(jTFLastDraw)
          .addComponent(jTFLastCowardry))
        .addContainerGap())
    );
    jPanelLastestLayout.setVerticalGroup(
      jPanelLastestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelLastestLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTFLastRealWin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTFLastRealDefeat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTFLastConcedeByMe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTFLastConcede, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTFLastDraw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTFLastCowardry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanelStatsScores.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

    jLBLAliasMAXPScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLAliasMAXPScore.setText("Rien");

    jLBLAliasMINPScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLAliasMINPScore.setText("Rien");

    jLBLAliasMAXEScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLAliasMAXEScore.setText("Rien");

    jLBLAliasMINEScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLAliasMINEScore.setText("Rien");

    jLBLMAXPScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMAXPScore.setText("Rien");
    jLBLMAXPScore.setToolTipText("");

    jLBLMINPScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMINPScore.setText("Rien");

    jLBLMAXEScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMAXEScore.setText("Rien");

    jLBLMINEScore.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMINEScore.setText("Rien");

    jRBMAXP.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jRBMAXP.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectionBouton(evt);
      }
    });

    jRBMINP.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jRBMINP.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectionBouton(evt);
      }
    });

    jRBMINE.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jRBMINE.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectionBouton(evt);
      }
    });

    jRBMAXE.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jRBMAXE.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        SelectionBouton(evt);
      }
    });

    jLBLMatchOpponentScoreVSMAX.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMatchOpponentScoreVSMAX.setText("Rien");

    jLBLMatchOpponentScoreVSMIN.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMatchOpponentScoreVSMIN.setText("Rien");

    jLBLMatchScoreVSMAX.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMatchScoreVSMAX.setText("Rien");

    jLBLMatchScoreVSMIN.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLMatchScoreVSMIN.setText("Rien");

    jLBLVictoriesInaRow.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLVictoriesInaRow.setText("Rien");
    jLBLVictoriesInaRow.setToolTipText("Number of victories  in a row");

    jLBLDefeatsInaRow.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLDefeatsInaRow.setText("Rien");
    jLBLDefeatsInaRow.setToolTipText("Number of defeats in a row");

    jLBLConcedesInaRow.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLConcedesInaRow.setText("Rien");
    jLBLConcedesInaRow.setToolTipText("Number of concedes in a row");

    javax.swing.GroupLayout jPanelStatsScoresLayout = new javax.swing.GroupLayout(jPanelStatsScores);
    jPanelStatsScores.setLayout(jPanelStatsScoresLayout);
    jPanelStatsScoresLayout.setHorizontalGroup(
      jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelStatsScoresLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLBLVictoriesInaRow)
          .addComponent(jLBLAliasMINEScore)
          .addComponent(jLBLAliasMAXEScore)
          .addComponent(jLBLAliasMINPScore)
          .addComponent(jLBLAliasMAXPScore)
          .addComponent(jLBLDefeatsInaRow)
          .addComponent(jLBLConcedesInaRow))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 366, Short.MAX_VALUE)
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLBLMINPScore)
          .addComponent(jLBLMAXPScore)
          .addComponent(jLBLMAXEScore)
          .addComponent(jLBLMINEScore))
        .addGap(18, 18, 18)
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLBLMatchOpponentScoreVSMAX)
          .addComponent(jLBLMatchOpponentScoreVSMIN)
          .addComponent(jLBLMatchScoreVSMAX)
          .addComponent(jLBLMatchScoreVSMIN))
        .addGap(47, 47, 47)
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jRBMAXE)
          .addComponent(jRBMINP)
          .addComponent(jRBMAXP)
          .addComponent(jRBMINE))
        .addGap(16, 16, 16))
    );
    jPanelStatsScoresLayout.setVerticalGroup(
      jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelStatsScoresLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLBLMatchOpponentScoreVSMAX)
          .addComponent(jRBMAXP)
          .addComponent(jLBLMAXPScore)
          .addComponent(jLBLAliasMAXPScore))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jRBMINP)
          .addComponent(jLBLMatchOpponentScoreVSMIN)
          .addComponent(jLBLMINPScore)
          .addComponent(jLBLAliasMINPScore))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jRBMAXE)
          .addComponent(jLBLMatchScoreVSMAX)
          .addComponent(jLBLMAXEScore)
          .addComponent(jLBLAliasMAXEScore))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanelStatsScoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jRBMINE)
          .addComponent(jLBLMatchScoreVSMIN)
          .addComponent(jLBLMINEScore)
          .addComponent(jLBLAliasMINEScore))
        .addGap(18, 18, 18)
        .addComponent(jLBLVictoriesInaRow)
        .addGap(18, 18, 18)
        .addComponent(jLBLDefeatsInaRow)
        .addGap(18, 18, 18)
        .addComponent(jLBLConcedesInaRow)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanelSStatsLayout = new javax.swing.GroupLayout(jPanelSStats);
    jPanelSStats.setLayout(jPanelSStatsLayout);
    jPanelSStatsLayout.setHorizontalGroup(
      jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelSStatsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jTFVictories, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLBLWinTotal))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLBLLstTotal)
          .addComponent(jTFDefeats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jTFConcedes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLBLConTotal))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(jCBVert, javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCBRouge, javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jCBNoir))
          .addComponent(jCBBleu, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jCBBlanc, javax.swing.GroupLayout.Alignment.TRAILING))
        .addGap(18, 18, 18)
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addGroup(jPanelSStatsLayout.createSequentialGroup()
            .addComponent(jLBLWins, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(17, 17, 17)
            .addComponent(jLBLDef, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLBLCon, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jCBEnemyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jPanelLastest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanelForme, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanelStatsScores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    jPanelSStatsLayout.setVerticalGroup(
      jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelSStatsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanelSStatsLayout.createSequentialGroup()
            .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jTFConcedes, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jTFDefeats, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jTFVictories, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jCBNoir, javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jCBEnemyLevel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jPanelForme, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(jPanelSStatsLayout.createSequentialGroup()
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelLastest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addGroup(jPanelSStatsLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jCBRouge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBVert)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBBleu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBBlanc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanelSStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                  .addComponent(jLBLWins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLBLDef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLBLCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLBLConTotal)
                  .addComponent(jLBLLstTotal)
                  .addComponent(jLBLWinTotal))
                .addGap(16, 16, 16))))
          .addGroup(jPanelSStatsLayout.createSequentialGroup()
            .addComponent(jPanelStatsScores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())))
    );

    jCBBlanc.getAccessibleContext().setAccessibleName("White");
    jCBBleu.getAccessibleContext().setAccessibleName("Blue");
    jCBVert.getAccessibleContext().setAccessibleName("Green");
    jCBRouge.getAccessibleContext().setAccessibleName("Red");
    jCBNoir.getAccessibleContext().setAccessibleName("Black");

    javax.swing.GroupLayout jPanelSuperStatsLayout = new javax.swing.GroupLayout(jPanelSuperStats);
    jPanelSuperStats.setLayout(jPanelSuperStatsLayout);
    jPanelSuperStatsLayout.setHorizontalGroup(
      jPanelSuperStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelSuperStatsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelSuperStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanelSStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPanePourTable))
        .addContainerGap())
    );
    jPanelSuperStatsLayout.setVerticalGroup(
      jPanelSuperStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelSuperStatsLayout.createSequentialGroup()
        .addComponent(jPanelSStats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPanePourTable, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanelSuperStats, javax.swing.GroupLayout.DEFAULT_SIZE, 1356, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanelSuperStats, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jCBEnemyLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEnemyLevelActionPerformed
    int EnnemyLvl=((JComboBox)evt.getSource()).getSelectedIndex();
    if(EnnemyLvl>=0)
    {
      jCBEnemyLevel.setToolTipText("Gathering statistics for level: "+Levels.get(EnnemyLvl));
      try
      {
        String tmp=getEnemyLvlStats(EnnemyLvl);

        if(tmp==null) return;

        String results[]=tmp.split("\\|");

        jLBLWins.setText(results[0]);
        jLBLDef.setText(results[1]);
        jLBLCon.setText(results[2]);

        int Total=Integer.valueOf(jLBLWins.getText())+Integer.valueOf(jLBLDef.getText())+Integer.valueOf(jLBLCon.getText());

        float percentwin=(Float.valueOf(jLBLWins.getText())/(float)Total)*100.0f;
        float percentdef=(Float.valueOf(jLBLDef.getText())/(float)Total)*100.0f;
        float percentcon=(Float.valueOf(jLBLCon.getText())/(float)Total)*100.0f;

        jLBLWins.setToolTipText("Number of victories over level "+Levels.get(EnnemyLvl)+" ("+String.format("%.1f", percentwin)+"%)");
        jLBLDef.setToolTipText("Number of defeats against level "+Levels.get(EnnemyLvl)+" ("+String.format("%.1f", percentdef)+"%)");
        jLBLCon.setToolTipText("Number of games conceded by level "+Levels.get(EnnemyLvl)+" ("+String.format("%.1f", percentcon)+"%)");

				if(ModeleTableMatch!=null) ModeleTableMatch.ClearDatas();
      }
      catch (SQLException ex)
      {
        Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }//GEN-LAST:event_jCBEnemyLevelActionPerformed

  private void jTFDefeatsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFDefeatsMouseClicked
    if(enemyColors.getInt()==0) return;
		try
    {
			if(bToggleRealDefeats==false)
			{

				jTFDefeats.setText(getRealDefeats(enemyColors.getInt()));
				jTFDefeats.setBackground(new Color(152,182,182,255));
				jTFDefeats.setToolTipText(	"Matches conceded by me... ("+jTFDefeats.getText()+"/"+getStats(enemyColors.getInt(), 'D')+") --> "+
																		String.format("%.1f",Float.valueOf(jTFDefeats.getText())/Float.valueOf(getStats(enemyColors.getInt(), 'D'))*100.0f)+" %");

				bToggleRealDefeats=true;
				populateTableWith("RealDefeats");

			}
			else
			{
				jTFDefeats.setText(getStats(enemyColors.getInt(),'D'));
				jTFDefeats.setBackground(new Color(255,255,255,255));

				jTFDefeats.setToolTipText("Defeats (including concedes done by me)");
				int Total=Integer.valueOf(jTFVictories.getText())+Integer.valueOf(jTFConcedes.getText())+Integer.valueOf(jTFDefeats.getText());
				float PercentDef=(float)(Integer.valueOf(jTFDefeats.getText())/(float)Total)*100.0f;
				jTFDefeats.setToolTipText(jTFDefeats.getToolTipText()+" ("+String.format("%.1f", PercentDef)+"% )");
				
				bToggleRealDefeats=false;
				populateTableWith("Defeats");
			}
		}
    catch (SQLException ex)
    {
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
    }
		
  }//GEN-LAST:event_jTFDefeatsMouseClicked

  private void jTFDefeatsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTFDefeatsFocusLost
    try
    {
      jTFDefeats.setText(getStats(enemyColors.getInt(),'D'));
      jTFDefeats.setBackground(new Color(255,255,255,255));
			
			jTFDefeats.setToolTipText("Defeats (including concedes done by me)");
			int Total=Integer.valueOf(jTFVictories.getText())+Integer.valueOf(jTFConcedes.getText())+Integer.valueOf(jTFDefeats.getText());
			float PercentDef=(float)(Integer.valueOf(jTFDefeats.getText())/(float)Total)*100.0f;
			jTFDefeats.setToolTipText(jTFDefeats.getToolTipText()+" ("+String.format("%.1f", PercentDef)+"% )");
			
    }
    catch (SQLException ex)
    {
      Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
    }
    bToggleRealDefeats=false;
  }//GEN-LAST:event_jTFDefeatsFocusLost

  private void SelectColor(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectColor
    String strCouleur=((JCheckBox)evt.getSource()).getName();
		
    if(strCouleur.contains("Black"))
    {
      if(enemyColors.DeckColors.get(ma_Couleurs.NOIR)==false) enemyColors.DeckColors.set(ma_Couleurs.NOIR);
      else enemyColors.DeckColors.clear(ma_Couleurs.NOIR);
			
    }
    if(strCouleur.contains("Red"))
    {
      if(enemyColors.DeckColors.get(ma_Couleurs.ROUGE)==false) enemyColors.DeckColors.set(ma_Couleurs.ROUGE);
      else enemyColors.DeckColors.clear(ma_Couleurs.ROUGE);
    }
    if(strCouleur.contains("Green"))
    {
      if(enemyColors.DeckColors.get(ma_Couleurs.VERT)==false) enemyColors.DeckColors.set(ma_Couleurs.VERT);
      else enemyColors.DeckColors.clear(ma_Couleurs.VERT);
    }
    if(strCouleur.contains("Blue"))
    {
      if(enemyColors.DeckColors.get(ma_Couleurs.BLEU)==false) enemyColors.DeckColors.set(ma_Couleurs.BLEU);
      else enemyColors.DeckColors.clear(ma_Couleurs.BLEU);
    }
    if(strCouleur.contains("White"))
    {
      if(enemyColors.DeckColors.get(ma_Couleurs.BLANC)==false) enemyColors.DeckColors.set(ma_Couleurs.BLANC);
      else enemyColors.DeckColors.clear(ma_Couleurs.BLANC);
    }
    try
    {
      DisplayDatas(enemyColors.getInt());
			ModeleTableMatch.ClearDatas();
    }
    catch(SQLException ex)
    {
      Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
    }
  }//GEN-LAST:event_SelectColor

  private void SelectionBouton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectionBouton
		String selected=((JRadioButton)evt.getSource()).getText();
		
		if(Integer.valueOf(selected)>0)
		{
			try
			{
				if(LaConnection.isValid(1))				
				{
					Statement=LaConnection.createStatement();
					String SQLRequest="SELECT (SELECT Alias FROM Players WHERE Players.idPlayer=Matches.idPlayer),MatchColor,EnLvl,HisScore,MyScore,MyLvl FROM Matches WHERE idMatch="+selected;
					Statement.executeQuery(SQLRequest);
					Resultats=Statement.getResultSet();
					if(Resultats.first())
					{
						ModeleTableMatch.ClearDatas();
						LesMatches.clear();
						classMatch degraded=new classMatch();
						degraded.setName(Resultats.getString(1));
						degraded.setDBMatchID(Integer.valueOf(selected));
						
						BitSet tmpbs=new BitSet(5);
						int rep=Integer.valueOf(Resultats.getByte(2));
												
						if((rep&ma_Couleurs.VALNOIR)==ma_Couleurs.VALNOIR) tmpbs.set(ma_Couleurs.NOIR);
						if((rep&ma_Couleurs.VALROUGE)==ma_Couleurs.VALROUGE) tmpbs.set(ma_Couleurs.ROUGE);
						if((rep&ma_Couleurs.VALVERT)==ma_Couleurs.VALVERT) tmpbs.set(ma_Couleurs.VERT);
						if((rep&ma_Couleurs.VALBLEU)==ma_Couleurs.VALBLEU) tmpbs.set(ma_Couleurs.BLEU);
						if((rep&ma_Couleurs.VALBLANC)==ma_Couleurs.VALBLANC) tmpbs.set(ma_Couleurs.BLANC);
												
						degraded.setColors(tmpbs);
						String replvl=classMatch.Levels.get(Integer.valueOf(Resultats.getString(3)));
						degraded.setLevel(false,replvl);
						degraded.setScore(false, Resultats.getInt(4));
						degraded.setScore(true, Resultats.getInt(5));
						replvl=classMatch.Levels.get(Integer.valueOf(Resultats.getString(6)));
						degraded.setLevel(true, replvl);
						ModeleTableMatch.addRow(degraded);
						LesMatches.add(degraded);
					}
				}
			} 
			catch (SQLException ex)
			{
				Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
  }//GEN-LAST:event_SelectionBouton

  private void jTFVictoriesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTFVictoriesMouseClicked
  {//GEN-HEADEREND:event_jTFVictoriesMouseClicked
    try 
		{
			populateTableWith("Victories");
		} 
		catch (SQLException ex) 
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
  }//GEN-LAST:event_jTFVictoriesMouseClicked

  private void jTFConcedesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTFConcedesMouseClicked
  {//GEN-HEADEREND:event_jTFConcedesMouseClicked
    try 
		{
			populateTableWith("Concedes");
		} 
		catch (SQLException ex) 
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
  }//GEN-LAST:event_jTFConcedesMouseClicked

  private void jLBLWinsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLBLWinsMouseClicked
  {//GEN-HEADEREND:event_jLBLWinsMouseClicked
		try
		{
			populateTableWith("Level V");
		} 
		catch (SQLException ex)
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
  }//GEN-LAST:event_jLBLWinsMouseClicked

  private void jLBLDefMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLBLDefMouseClicked
  {//GEN-HEADEREND:event_jLBLDefMouseClicked
    try
		{
			populateTableWith("Level D");
		} 
		catch (SQLException ex)
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
  }//GEN-LAST:event_jLBLDefMouseClicked

  private void jLBLConMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLBLConMouseClicked
  {//GEN-HEADEREND:event_jLBLConMouseClicked
    try
		{
			populateTableWith("Level C");
		} 
		catch (SQLException ex)
		{
			Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
		}
  }//GEN-LAST:event_jLBLConMouseClicked
	
	
	
	private void DisplayDatas(int combinaison) throws SQLException
	{
		jLBLWinTotal.setText("0");
		jLBLConTotal.setText("0");
		jLBLLstTotal.setText("0");
		
		jTFVictories.setText("0");
		jTFConcedes.setText("0");
		jTFDefeats.setText("0");
		
		jTFVictories.setToolTipText("Victories");
		jTFDefeats.setToolTipText("Defeats (including concedes done by me)");
		jTFConcedes.setToolTipText("Concedes (by the ennemy)");
		
		int		Total;
		
		
		
		if(LaConnection.isValid(1) && combinaison!=0)
		{
			// couleur resultat 
			
			jTFVictories.setText(getStats(combinaison,'V'));
			jTFConcedes.setText(getStats(combinaison,'C'));
			jTFDefeats.setText(getStats(combinaison,'D'));
			
			Total=Integer.valueOf(jTFVictories.getText())+Integer.valueOf(jTFConcedes.getText())+Integer.valueOf(jTFDefeats.getText());
			
			float PercentVic;
			float PercentDef;
			float PercentCon;
			
			if(Total!=0)
			{
				PercentVic=(float)(Integer.valueOf(jTFVictories.getText())/(float)Total)*100.0f;
				PercentDef=(float)(Integer.valueOf(jTFDefeats.getText())/(float)Total)*100.0f;
				PercentCon=(float)(Integer.valueOf(jTFConcedes.getText())/(float)Total)*100.0f;
				
				jTFVictories.setToolTipText(jTFVictories.getToolTipText()+" ("+String.format("%.1f", PercentVic)+"% )");
				jTFDefeats.setToolTipText(jTFDefeats.getToolTipText()+" ("+String.format("%.1f", PercentDef)+"% )");
				jTFConcedes.setToolTipText(jTFConcedes.getToolTipText()+" ("+String.format("%.1f", PercentCon)+"% )");
			}
						
			jLBLWinTotal.setText(getTotals('V'));
			jLBLConTotal.setText(getTotals('C'));
			jLBLLstTotal.setText(getTotals('D'));
			
			jLBLWinTotal.setToolTipText("Total victories");
			jLBLLstTotal.setToolTipText("Total defeats (including my concedes)");
			jLBLConTotal.setToolTipText("Total concedes by the ennemy");
			
			Total=Integer.valueOf(jLBLWinTotal.getText())+Integer.valueOf(jLBLLstTotal.getText())+Integer.valueOf(jLBLConTotal.getText());
			
			PercentVic=(float)(Integer.valueOf(jLBLWinTotal.getText())/(float)Total)*100.0f;
			PercentDef=(float)(Integer.valueOf(jLBLLstTotal.getText())/(float)Total)*100.0f;
			PercentCon=(float)(Integer.valueOf(jLBLConTotal.getText())/(float)Total)*100.0f;
			
			jLBLWinTotal.setToolTipText(jLBLWinTotal.getToolTipText()+" ("+String.format("%.1f", PercentVic)+"% )");
			jLBLLstTotal.setToolTipText(jLBLLstTotal.getToolTipText()+" ("+String.format("%.1f", PercentDef)+"% )");
			jLBLConTotal.setToolTipText(jLBLConTotal.getToolTipText()+" ("+String.format("%.1f", PercentCon)+"% )");
			
			// En fonction des combinaisons...
			
			int difference=(Integer.valueOf(jTFConcedes.getText())+Integer.valueOf(jTFVictories.getText()))-Integer.valueOf(jTFDefeats.getText());
			if((combinaison&ma_Couleurs.VALNOIR)==ma_Couleurs.VALNOIR) jCBNoir.setToolTipText(String.valueOf(difference));
			else jCBNoir.setToolTipText(null);
			if((combinaison&ma_Couleurs.VALROUGE)==ma_Couleurs.VALROUGE) jCBRouge.setToolTipText(String.valueOf(difference));
			else jCBRouge.setToolTipText(null);
			if((combinaison&ma_Couleurs.VALVERT)==ma_Couleurs.VALVERT) jCBVert.setToolTipText(String.valueOf(difference));
			else jCBVert.setToolTipText(null);
			if((combinaison&ma_Couleurs.VALBLEU)==ma_Couleurs.VALBLEU) jCBBleu.setToolTipText(String.valueOf(difference));
			else jCBBleu.setToolTipText(null);
			if((combinaison&ma_Couleurs.VALBLANC)==ma_Couleurs.VALBLANC) jCBBlanc.setToolTipText(String.valueOf(difference));
			else jCBBlanc.setToolTipText(null);
		}
		else
		{
			// donc il n'y a pas de combinaison de couleurs
			jCBNoir.setToolTipText(null);
			jCBRouge.setToolTipText(null);
			jCBVert.setToolTipText(null);
			jCBBleu.setToolTipText(null);
			jCBBlanc.setToolTipText(null);
		}
	}
	
	private String getStats(int couleur,char type) throws SQLException
	{
		int tmp;
		
		String SQLRequest; // ="SELECT COUNT(idMatch) FROM Matches WHERE MatchColor&"+couleur+"="+couleur+" AND Result='"+type+"'";
		
		// L'outil permet de faire un check des combinaisons mais il 
		// + ignore les matches contre les decks à couleur unique 
		// + inclus les combinaisons dans celles plus larges (blanc noir vs blanc noir bleu vs blanc vs noir) <-- il va additionner tout 
		// :{
		
		SQLRequest="SELECT COUNT(idMatch) FROM Matches WHERE MatchColor="+couleur+" AND Result='"+type+"'";
				
		// System.err.println("Statement: "+SQLRequest);
			
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(!Resultats.first()) return null;
			tmp=Resultats.getInt(1);
			Resultats.close();
			Statement.close();
			return String.valueOf(tmp);
		}
		return null;
	}
	
	private String getTotals(char type) throws SQLException
	{
		int tmp;
		String SQLRequest="SELECT COUNT(idMatch) FROM Matches WHERE Result='"+type+"'";
		
		// System.err.println("Statement: "+SQLRequest);
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(!Resultats.first()) return null;
			tmp=Resultats.getInt(1);
			Resultats.close();
			Statement.close();
			return String.valueOf(tmp);
		}
		return null;
	}
	
	private String getRealDefeats(int couleur) throws SQLException
	{
		int tmp;
		String SQLRequest="SELECT COUNT(idMatch) FROM Matches WHERE MatchColor="+couleur+" AND Result='D' AND MyScore > 0";
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(!Resultats.first()) return null;
			tmp=Resultats.getInt(1);
			Resultats.close();
			Statement.close();
			return String.valueOf(tmp);
		}
		return null;
	}
	
	/**
	 * 
	 * @param level
	 * @return A string to be splitted on '|' (contains three strings)
	 * @throws SQLException 
	 */
	private String getEnemyLvlStats(int level) throws SQLException
	{
		// il faudra juste splitter sur '|'
		String tmp;
		String SQLRequest="SELECT (SELECT COUNT(idMatch) FROM Matches WHERE EnLvl ="+level+" AND Result = 'V'),"+
											"(SELECT COUNT(idMatch) FROM Matches WHERE EnLvl ="+level+" AND Result = 'D'),"+
											"(SELECT COUNT(idMatch) FROM Matches WHERE EnLvl ="+level+" AND Result = 'C')";
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(!Resultats.first()) return null;
			tmp=String.valueOf(Resultats.getInt(1))+"|"+String.valueOf(Resultats.getInt(2))+"|"+String.valueOf(Resultats.getInt(3));
			Resultats.close();
			Statement.close();
			return tmp;
		}
		return null;
	}
	
	private void getEfficencyIcons(int limit) throws SQLException
	{
		String SQLRequest="select Result,MyScore from Matches ORDER BY idMatch DESC LIMIT "+limit;
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(!Resultats.first()) return;
			do
			{
				String tmp=Resultats.getString(1);
				switch(tmp)
				{
					case "V":	jPanelForme.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/coeur32x32.png"))));
										break;
					case "D": int myscore=Resultats.getInt(2);
										if(myscore>0) jPanelForme.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/diarrhée32x32.png"))));
										else jPanelForme.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/death32x32.png"))));
										break;
					case "C": jPanelForme.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/caca32x32.png"))));
										break;
					case "E": jPanelForme.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/balance32x32.png"))));
										break;
				}
			}while(Resultats.next());
			Resultats.close();
			Statement.close();
		}
	}
	
	/**
	 * 
	 * @param param
	 * @return A splitable string on '-666-'
	 * @throws SQLException 
	 */
	
	private String getLast(char param) throws SQLException
	{
		// il faudra splitter sur '-666-'
		String tmp="";
		char copy=param;
		if(param=='T') param='C';
		if(param=='P') param='D';
		String SQLRequest="SELECT (SELECT Alias FROM Players WHERE Players.idPlayer=Matches.idPlayer), "+ 
											"EndTime, (SELECT (SELECT MAX(idMatch) FROM Matches)-idMatch) FROM Matches "+
											"WHERE Result='"+param+"' ";
		
		
		if(copy=='D') SQLRequest+="AND MyScore > 0 ";
		if(copy=='P') SQLRequest+="AND MyScore <= 0 ";
		if(copy=='T') SQLRequest+="AND HisScore=20 AND HisScore=MyScore AND Turns <= 1 ";								// un "concede" de tapette se passe généralement quand le nombre de tours est 0 ou 1																	
		SQLRequest+="ORDER BY EndTime DESC LIMIT 1";
		
		//System.err.println("getLast: param -> "+param);
		//System.err.println("getLast: copy -> "+copy);
		//System.err.println("getLast: "+SQLRequest);
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(Resultats.first())
			{
				tmp+=Resultats.getString(1)+"-666-"; // Alias
				String tmpdate[]=Resultats.getDate(2).toString().split("-"); // EndTime
				tmp+=String.format("%2s", tmpdate[2])+"/"+String.format("%2s", tmpdate[1])+"/"+String.format("%4s", tmpdate[0])+"-666-"; 
				tmp+=String.valueOf(Resultats.getInt(3)+1); // How far
								
			} 
			Resultats.close();
			Statement.close();
			
			if(tmp.isEmpty()) return null;
			return tmp;
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 * @throws SQLException 
	 */
	private String getScoresInfos(ScoreInfo param,ScoreInfo extract) throws SQLException
	{
		String tmp="";
		String SQLRequest="";
		
		switch(param)
		{
			case MAXPP: SQLRequest=	"SELECT Alias, Matches.MyScore, Matches.HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer AND MyScore=(SELECT MAX(MyScore) FROM Matches)) FROM Matches,Players "+
															"WHERE Players.idPlayer=(SELECT idPlayer FROM Matches WHERE MyScore=(SELECT MAX(MyScore) FROM Matches )) ORDER BY MyScore DESC LIMIT 1";
									break;
			case MINPP: SQLRequest=	"SELECT Alias, Matches.MyScore, Matches.HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer AND MyScore=(SELECT MIN(MyScore) FROM Matches)) FROM Matches,Players "+
															"WHERE Players.idPlayer=(SELECT idPlayer FROM Matches WHERE MyScore=(SELECT MIN(MyScore) FROM Matches )) ORDER BY MyScore LIMIT 1";
									break;
			case MAXPE: SQLRequest=	"SELECT Alias, Matches.MyScore, Matches.HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer AND HisScore=(SELECT MAX(HisScore) FROM Matches)) FROM Matches,Players "+
															"WHERE Players.idPlayer=(SELECT idPlayer FROM Matches WHERE HisScore=(SELECT MAX(HisScore) FROM Matches)) ORDER BY HisScore DESC LIMIT 1";
									break;
			case MINPE: SQLRequest=	"SELECT Alias, Matches.MyScore, Matches.HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer AND HisScore=(SELECT MIN(HisScore) FROM Matches)) FROM Matches,Players "+
															"WHERE Players.idPlayer=(SELECT idPlayer FROM Matches WHERE HisScore=(SELECT MIN(HisScore) FROM Matches)) ORDER BY HisScore LIMIT 1";
									break;
		}
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			if(!Resultats.first()) return null;
			//tmp=Resultats.getString(1)+"|"+Resultats.getInt(2)+"|"+Resultats.getInt(3)+"|"+Resultats.getInt(4);
			switch(extract)
			{
				case MID: tmp=String.format("%03d", Resultats.getInt(4));
									break;
				case SCMP: 
				case SCmP: tmp=String.format("%03d", Resultats.getInt(2));
										break;
				case SCME: 
				case SCmE: tmp=String.format("%03d", Resultats.getInt(3));
									 break;
				case Alias: tmp=Resultats.getString(1);
										 break;
			}
			Resultats.close();
			Statement.close();
			return tmp;
		}
		return null;
	}
	
	private String getInARow(char pattern) throws SQLException
	{
		if(LaConnection.isValid(1))
		{
			String tmp;
			int leplusgrand=-1;
			LinkedList<String> listedesresultats=new LinkedList<>();
			
			String SQLRequest="SELECT Result FROM Matches";
			
			Statement=LaConnection.createStatement();
			Resultats=Statement.executeQuery(SQLRequest);
			
			if(Resultats.first())
			{
				do
				{
					tmp=Resultats.getString(1);
					if(tmp.equals(String.valueOf(pattern))) 
					{
						listedesresultats.add(tmp);
					}
					else
					{
						if(listedesresultats.size()>leplusgrand) leplusgrand=listedesresultats.size();
						listedesresultats.clear();
					}
				}while(Resultats.next());
			}
			//System.err.println("getInaRow:");
			//System.err.println("Pattern '"+pattern+"'");
			//System.err.println("Found: "+leplusgrand);
			
			Resultats.close();
			Statement.close();
			return String.valueOf(leplusgrand);
		}
		return "0";
	}

	private void populateTableWith(String whattodo) throws SQLException 
	{
		String SQLRequest="";
		if(whattodo.equals("RealDefeats"))
		{
			SQLRequest="SELECT * FROM Matches WHERE Result='D' AND MyScore <= 0 AND MatchColor="+enemyColors.getInt()+" ORDER BY EndTime DESC"; 		
		}
		if(whattodo.equals("Defeats"))
		{
			SQLRequest="SELECT * FROM Matches WHERE Result='D' AND MyScore > 0 AND MatchColor="+enemyColors.getInt()+" ORDER BY EndTime DESC"; 		
		}
		if(whattodo.equals("Victories"))
		{
			SQLRequest="SELECT * FROM Matches WHERE Result='V' AND MatchColor="+enemyColors.getInt()+" ORDER BY EndTime DESC"; 		
		}
		if(whattodo.equals("Concedes"))
		{
			SQLRequest="SELECT * FROM Matches WHERE Result='C' AND MatchColor="+enemyColors.getInt()+" ORDER BY EndTime DESC"; 		
		}
		if(whattodo.contains("Level"))
		{
			int dbenlevel=jCBEnemyLevel.getSelectedIndex();
			String[] status=whattodo.split(" ");
			SQLRequest="SELECT * FROM Matches WHERE Result='"+status[1]+"' AND EnLvl="+dbenlevel+" ORDER BY EndTime DESC";
		}
		
		ModeleTableMatch.ClearDatas();
		LesMatches.clear();
		if(LaConnection.isValid(1))
		{
			Statement=LaConnection.createStatement();
			Resultats=Statement.executeQuery(SQLRequest);
			while(Resultats.next())
			{
				java.sql.ResultSetMetaData MetaDonnees=Resultats.getMetaData();
				int NbChamps=MetaDonnees.getColumnCount();
					
				for(int cptchamps=0;cptchamps<NbChamps;cptchamps++)
				{
					String unecolonne=ParseSQL(MetaDonnees.getColumnTypeName(cptchamps+1),Resultats,cptchamps+1);
					MDMatches.add(unecolonne);
				}
				PackDatasFromDB(MDMatches); // ListeDesMatches est modifié ici (je sais c'est opaque)
				MDMatches.clear();
			}
		}
		for(int cpt=0;cpt<LesMatches.size();cpt++) ModeleTableMatch.addRow(LesMatches.get(cpt));
	}
	
	public boolean PackDatasFromDB(LinkedList<Object> source) throws SQLException
	{
		classMatch tmp=new classMatch();
		BitSet tmpbs=new BitSet(5);
		String PlayerName=new String();
		
		int repint;
		
		tmp.setDBMatchID(Integer.valueOf((String)source.get(classMatch.IDMATCH)));
		tmp.setDBPlayerID(Integer.valueOf((String)source.get(classMatch.IDPLAYER)));
		
		if(LaConnection.isValid(1))
		{
			PlayerName=getPlayerNameDB(LaConnection,tmp.getDBPlayerID());
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
		
				
		return LesMatches.add(tmp);
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
	 * Truc bizarre java... à priori on crée une sous-classe de la classe UEPTable pour "transformer" l'objet UEPTable en
	 * "classe membre" (????)
	 */
	private class UEPTableImpl extends UEPTable 
	{

		public UEPTableImpl(TableModel TheModel, TableCellRenderer TheRenderer) 
		{
			super(TheModel, TheRenderer);
		}

		@Override
		public String getFromModel(int ligne, int colonne) 
		{
			switch(colonne)
			{
				case ma_tablemodelmatch.NAME:		return "Match: "+String.valueOf(LesMatches.get(ligne).getMatchID())+" "+LesMatches.get(ligne).getBeginDate();
				case ma_tablemodelmatch.COL:			return ((ma_Couleurs)ModeleTableMatch.getValueAt(ligne, ma_tablemodelmatch.COL)).getBinaryString();
				case ma_tablemodelmatch.ENLVL:		return Levels.get(Integer.valueOf(ModeleTableMatch.getValueAt(ligne, ma_tablemodelmatch.ENLVL).toString()));
				case ma_tablemodelmatch.MYLVL:		return Levels.get(Integer.valueOf(ModeleTableMatch.getValueAt(ligne, ma_tablemodelmatch.MYLVL).toString()));
				case ma_tablemodelmatch.SCP:		
																				try 
																				{
																					return LesMatches.get(ligne).getComments(LaConnection);
																				} 
																				catch(SQLException ex) 
																				{
																					Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
																				}
				default: return "";
			}
		}

		/*@Override
		public void PopulateTableWithDatas(LinkedList<Object> lesdonnees) throws SQLException
		{
			// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}*/
	}

	private ma_Couleurs enemyColors;
	private boolean bToggleRealDefeats=false;
	
	private LinkedList<String> Levels;
	private LinkedList<Object> MDMatches;
	private LinkedList<classMatch> LesMatches=new LinkedList<>();
	private final String strPostfixes[]=new String[]{"Tier 4", "Tier 3", "Tier 2", "Tier 1"};
	private final String strPrefixes[]=new String[]{"Bronze", "Silver", "Gold", "Platinium", "Diamond","Mystic"};   // platinium tier 1 <-> 15
	
	public enum ScoreInfo {MAXPP,MINPP,MAXPE,MINPE,MID,SCMP,SCmP,SCME,SCmE,Alias;};
	
	UEPTable LaTable;
	ma_tablemodelmatch ModeleTableMatch;
	defMatch RendererMatch;
	
			
	// SQL Related
	
	private java.sql.Connection LaConnection;
	private java.sql.ResultSet Resultats;
	private java.sql.Statement Statement;
	private java.sql.ResultSet SousRqte;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.ButtonGroup GroupeDeBoutons;
  public javax.swing.JCheckBox jCBBlanc;
  public javax.swing.JCheckBox jCBBleu;
  public javax.swing.JComboBox<String> jCBEnemyLevel;
  public javax.swing.JCheckBox jCBNoir;
  public javax.swing.JCheckBox jCBRouge;
  public javax.swing.JCheckBox jCBVert;
  public javax.swing.JLabel jLBLAliasMAXEScore;
  public javax.swing.JLabel jLBLAliasMAXPScore;
  public javax.swing.JLabel jLBLAliasMINEScore;
  public javax.swing.JLabel jLBLAliasMINPScore;
  public javax.swing.JLabel jLBLCon;
  public javax.swing.JLabel jLBLConTotal;
  public javax.swing.JLabel jLBLConcedesInaRow;
  public javax.swing.JLabel jLBLDef;
  public javax.swing.JLabel jLBLDefeatsInaRow;
  public javax.swing.JLabel jLBLLstTotal;
  public javax.swing.JLabel jLBLMAXEScore;
  public javax.swing.JLabel jLBLMAXPScore;
  public javax.swing.JLabel jLBLMINEScore;
  public javax.swing.JLabel jLBLMINPScore;
  public javax.swing.JLabel jLBLMatchOpponentScoreVSMAX;
  public javax.swing.JLabel jLBLMatchOpponentScoreVSMIN;
  public javax.swing.JLabel jLBLMatchScoreVSMAX;
  public javax.swing.JLabel jLBLMatchScoreVSMIN;
  public javax.swing.JLabel jLBLVictoriesInaRow;
  public javax.swing.JLabel jLBLWinTotal;
  public javax.swing.JLabel jLBLWins;
  public javax.swing.JPanel jPanelForme;
  public javax.swing.JPanel jPanelLastest;
  public javax.swing.JPanel jPanelSStats;
  public javax.swing.JPanel jPanelStatsScores;
  public javax.swing.JPanel jPanelSuperStats;
  public javax.swing.JRadioButton jRBMAXE;
  public javax.swing.JRadioButton jRBMAXP;
  public javax.swing.JRadioButton jRBMINE;
  public javax.swing.JRadioButton jRBMINP;
  public javax.swing.JScrollPane jScrollPanePourTable;
  public javax.swing.JTextField jTFConcedes;
  public javax.swing.JTextField jTFDefeats;
  public javax.swing.JTextField jTFLastConcede;
  public javax.swing.JTextField jTFLastConcedeByMe;
  public javax.swing.JTextField jTFLastCowardry;
  public javax.swing.JTextField jTFLastDraw;
  public javax.swing.JTextField jTFLastRealDefeat;
  public javax.swing.JTextField jTFLastRealWin;
  public javax.swing.JTextField jTFVictories;
  // End of variables declaration//GEN-END:variables


}
