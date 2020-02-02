package MagicArena;

import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

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
	/*public superStats() 
	{
		initComponents();
		setVisible(true);
		enemyColors=new ma_Couleurs();
	}*/
	
	public superStats(java.sql.Connection param)
	{
		initComponents();
		setVisible(true);
		enemyColors=new ma_Couleurs();
		LaConnection=param;
		Levels=new LinkedList<>();
		
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
			
			jLBLVictoriesInaRow.setText(getInARow('V'));
			jLBLConcedesInaRow.setText(getInARow('C'));
			jLBLDefeatsInaRow.setText(getInARow('D'));
			
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
  private void initComponents() {

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
    jPanelInfos = new javax.swing.JPanel();

    setMinimumSize(new java.awt.Dimension(1336, 836));
    setResizable(false);
    setSize(new java.awt.Dimension(1392, 836));

    jPanelSuperStats.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    jPanelSuperStats.setMaximumSize(new java.awt.Dimension(1300, 800));
    jPanelSuperStats.setMinimumSize(new java.awt.Dimension(1300, 800));
    jPanelSuperStats.setPreferredSize(new java.awt.Dimension(1300, 800));

    jPanelSStats.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statistiques", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Liberation Mono", 1, 10))); // NOI18N

    jCBBlanc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manablanc.png"))); // NOI18N
    jCBBlanc.setName("White"); // NOI18N
    jCBBlanc.setRolloverEnabled(false);
    jCBBlanc.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manablanc.png"))); // NOI18N
    jCBBlanc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectColor(evt);
      }
    });

    jCBBleu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manableu.png"))); // NOI18N
    jCBBleu.setName("Blue"); // NOI18N
    jCBBleu.setRolloverEnabled(false);
    jCBBleu.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manableu.png"))); // NOI18N
    jCBBleu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectColor(evt);
      }
    });

    jCBVert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manavert.png"))); // NOI18N
    jCBVert.setName("Green"); // NOI18N
    jCBVert.setRolloverEnabled(false);
    jCBVert.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manavert.png"))); // NOI18N
    jCBVert.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectColor(evt);
      }
    });

    jCBRouge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_manarouge.png"))); // NOI18N
    jCBRouge.setName("Red"); // NOI18N
    jCBRouge.setRolloverEnabled(false);
    jCBRouge.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/manarouge.png"))); // NOI18N
    jCBRouge.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectColor(evt);
      }
    });

    jCBNoir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/dis_mananoir.png"))); // NOI18N
    jCBNoir.setName("Black"); // NOI18N
    jCBNoir.setRolloverEnabled(false);
    jCBNoir.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/MagicArena/images/mananoir.png"))); // NOI18N
    jCBNoir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectColor(evt);
      }
    });

    jTFVictories.setEditable(false);
    jTFVictories.setColumns(3);
    jTFVictories.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jTFVictories.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFVictories.setText("0");
    jTFVictories.setToolTipText("Victories");

    jLBLWinTotal.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLWinTotal.setText("0");
    jLBLWinTotal.setToolTipText("Total victories");

    jTFDefeats.setEditable(false);
    jTFDefeats.setColumns(3);
    jTFDefeats.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jTFDefeats.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFDefeats.setText("0");
    jTFDefeats.setToolTipText("Defeats");
    jTFDefeats.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTFDefeatsFocusLost(evt);
      }
    });
    jTFDefeats.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
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
    jTFConcedes.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jTFConcedes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    jTFConcedes.setText("0");
    jTFConcedes.setToolTipText("Concedes");

    jCBEnemyLevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    jCBEnemyLevel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jCBEnemyLevelActionPerformed(evt);
      }
    });

    jLBLWins.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLWins.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLBLWins.setText("0");
    jLBLWins.setMaximumSize(new java.awt.Dimension(7, 24));
    jLBLWins.setMinimumSize(new java.awt.Dimension(7, 24));
    jLBLWins.setPreferredSize(new java.awt.Dimension(7, 24));

    jLBLDef.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLDef.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLBLDef.setText("0");
    jLBLDef.setMaximumSize(new java.awt.Dimension(7, 24));
    jLBLDef.setMinimumSize(new java.awt.Dimension(7, 24));
    jLBLDef.setPreferredSize(new java.awt.Dimension(7, 24));

    jLBLCon.setFont(new java.awt.Font("Liberation Mono", 1, 12)); // NOI18N
    jLBLCon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLBLCon.setText("0");
    jLBLCon.setMaximumSize(new java.awt.Dimension(7, 24));
    jLBLCon.setMinimumSize(new java.awt.Dimension(7, 24));
    jLBLCon.setPreferredSize(new java.awt.Dimension(7, 24));

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

    jLBLAliasMAXPScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLAliasMAXPScore.setText("Rien");

    jLBLAliasMINPScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLAliasMINPScore.setText("Rien");

    jLBLAliasMAXEScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLAliasMAXEScore.setText("Rien");

    jLBLAliasMINEScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLAliasMINEScore.setText("Rien");

    jLBLMAXPScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMAXPScore.setText("Rien");
    jLBLMAXPScore.setToolTipText("");

    jLBLMINPScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMINPScore.setText("Rien");

    jLBLMAXEScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMAXEScore.setText("Rien");

    jLBLMINEScore.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMINEScore.setText("Rien");

    jRBMAXP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectionBouton(evt);
      }
    });

    jRBMINP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectionBouton(evt);
      }
    });

    jRBMINE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectionBouton(evt);
      }
    });

    jRBMAXE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        SelectionBouton(evt);
      }
    });

    jLBLMatchOpponentScoreVSMAX.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMatchOpponentScoreVSMAX.setText("Rien");

    jLBLMatchOpponentScoreVSMIN.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMatchOpponentScoreVSMIN.setText("Rien");

    jLBLMatchScoreVSMAX.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMatchScoreVSMAX.setText("Rien");

    jLBLMatchScoreVSMIN.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLMatchScoreVSMIN.setText("Rien");

    jLBLVictoriesInaRow.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLVictoriesInaRow.setText("Rien");
    jLBLVictoriesInaRow.setToolTipText("Number of victories  in a row");

    jLBLDefeatsInaRow.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    jLBLDefeatsInaRow.setText("Rien");
    jLBLDefeatsInaRow.setToolTipText("Number of defeats in a row");

    jLBLConcedesInaRow.setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
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
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
          .addComponent(jCBEnemyLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(68, 68, 68)
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
                .addContainerGap(12, Short.MAX_VALUE))
              .addGroup(jPanelSStatsLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jCBRouge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBVert)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBBleu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCBBlanc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    jPanelInfos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

    javax.swing.GroupLayout jPanelInfosLayout = new javax.swing.GroupLayout(jPanelInfos);
    jPanelInfos.setLayout(jPanelInfosLayout);
    jPanelInfosLayout.setHorizontalGroup(
      jPanelInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanelInfosLayout.setVerticalGroup(
      jPanelInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 456, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout jPanelSuperStatsLayout = new javax.swing.GroupLayout(jPanelSuperStats);
    jPanelSuperStats.setLayout(jPanelSuperStatsLayout);
    jPanelSuperStatsLayout.setHorizontalGroup(
      jPanelSuperStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelSuperStatsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanelSuperStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanelInfos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanelSStats, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    jPanelSuperStatsLayout.setVerticalGroup(
      jPanelSuperStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanelSuperStatsLayout.createSequentialGroup()
        .addComponent(jPanelSStats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanelInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanelSuperStats, javax.swing.GroupLayout.PREFERRED_SIZE, 1294, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanelSuperStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(25, 25, 25))
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

      }
      catch (SQLException ex)
      {
        Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }//GEN-LAST:event_jCBEnemyLevelActionPerformed

  private void jTFDefeatsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFDefeatsMouseClicked
    if(bToggleRealDefeats==false)
    {
      try
      {
        jTFDefeats.setText(getRealDefeats(enemyColors.getInt()));
        jTFDefeats.setBackground(new Color(152,182,182,255));
				jTFDefeats.setToolTipText(	"Matches conceded by me... ("+jTFDefeats.getText()+"/"+getStats(enemyColors.getInt(), 'D')+") --> "+
																		String.format("%.1f",Float.valueOf(jTFDefeats.getText())/Float.valueOf(getStats(enemyColors.getInt(), 'D'))*100.0f)+" %");
      }
      catch (SQLException ex)
      {
        Logger.getLogger(superStats.class.getName()).log(Level.SEVERE, null, ex);
      }
      bToggleRealDefeats=true;
    }
    else
    {
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
    System.err.println("Value: "+enemyColors.DeckColors.toString()+"("+enemyColors.getBinaryString()+")");
    try
    {
      DisplayDatas(enemyColors.getInt());
    }
    catch(SQLException ex)
    {
      Logger.getLogger(FenetrePrincipale.class.getName()).log(Level.SEVERE, null, ex);
    }
  }//GEN-LAST:event_SelectColor

  private void SelectionBouton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectionBouton
    System.err.println("Bouton selected");
		System.err.println("Bouton: "+((JRadioButton)evt.getSource()).getText());
		
  }//GEN-LAST:event_SelectionBouton
	
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
		
		// 	if(couleur==ma_Couleurs.VALNOIR || couleur==ma_Couleurs.VALROUGE || couleur==ma_Couleurs.VALVERT || couleur==ma_Couleurs.VALBLEU || couleur==ma_Couleurs.VALBLANC)
		
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
	
	/*private String getEfficiency(int limit) throws SQLException
	{
		String tmp="";
		String SQLRequest="select Result from Matches ORDER BY idMatch DESC LIMIT "+limit;
		
		Statement=LaConnection.createStatement();
		Resultats=Statement.executeQuery(SQLRequest);
			
		if(Resultats!=null)
		{
			Resultats.first();
			do
			{
				tmp=tmp+"["+Resultats.getString(1)+"]";
			}while(Resultats.next());
			Resultats.close();
			Statement.close();
			return tmp;
		}
		return null;
	}*/
	
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
		// ce n'est pas suffisant (surtout quand on a sélectionné un joueur dans la liste ^^ -> MatchColor sera différent de 0 normalement)
 		if(copy=='T') SQLRequest+="AND HisScore=20 AND HisScore=MyScore OR MatchColor = 0 ";																									
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
		
		// Attention si j'ai deux fois les mêmes scores cette requête va planter :{ faudra mette limit 1 à mon avis
		
		switch(param)
		{
			case MAXPP: SQLRequest=	"SELECT Alias, MyScore, HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer) FROM Players "+
															"WHERE idPlayer=(SELECT idPlayer FROM Matches WHERE MyScore=(SELECT MAX(MyScore) FROM Matches) ORDER BY EndTime DESC LIMIT 1)";
									break;
			case MINPP: SQLRequest=	"SELECT Alias, MyScore, HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer) FROM Players "+
															"WHERE idPlayer=(SELECT idPlayer FROM Matches WHERE MyScore=(SELECT MIN(MyScore) FROM Matches) ORDER BY EndTime DESC LIMIT 1)";
									break;
			case MAXPE: SQLRequest=	"SELECT Alias, MyScore, HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer) FROM Players "+
															"WHERE idPlayer=(SELECT idPlayer FROM Matches WHERE HisScore=(SELECT MAX(HisScore) FROM Matches) ORDER BY EndTime DESC LIMIT 1)";
									break;
			case MINPE: SQLRequest=	"SELECT Alias, MyScore, HisScore, (SELECT idMatch FROM Matches WHERE Matches.idPlayer=Players.idPlayer) FROM Players "+
															"WHERE idPlayer=(SELECT idPlayer FROM Matches WHERE HisScore=(SELECT MIN(HisScore) FROM Matches) ORDER BY EndTime DESC LIMIT 1)";
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
			String tmp="-1";
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

	private ma_Couleurs enemyColors;
	private boolean bToggleRealDefeats=false;
	
	private LinkedList<String> Levels;
	private final String strPostfixes[]=new String[]{"Tier 4", "Tier 3", "Tier 2", "Tier 1"};
	private final String strPrefixes[]=new String[]{"Bronze", "Silver", "Gold", "Platinium", "Diamond","Mystic"};   // platinium tier 1 <-> 15
	
	public enum ScoreInfo {MAXPP,MINPP,MAXPE,MINPE,MID,SCMP,SCmP,SCME,SCmE,Alias;};
			
	// SQL Related
	
	private java.sql.Connection LaConnection;
	private java.sql.ResultSet Resultats;
	private java.sql.Statement Statement;

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
  public javax.swing.JPanel jPanelInfos;
  public javax.swing.JPanel jPanelLastest;
  public javax.swing.JPanel jPanelSStats;
  public javax.swing.JPanel jPanelStatsScores;
  public javax.swing.JPanel jPanelSuperStats;
  public javax.swing.JRadioButton jRBMAXE;
  public javax.swing.JRadioButton jRBMAXP;
  public javax.swing.JRadioButton jRBMINE;
  public javax.swing.JRadioButton jRBMINP;
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
