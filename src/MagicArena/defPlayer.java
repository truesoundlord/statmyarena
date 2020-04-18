package MagicArena;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.BitSet;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Dimitri "Hurukan" <soundlord@gmail.com>
 */
public class defPlayer extends javax.swing.JPanel implements TableCellRenderer
{
	/**
	 * Creates new form defcolonne
	 */
	public defPlayer() 
	{
		initComponents();
		
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setFont(new java.awt.Font("Liberation Mono", 1, 10)); // NOI18N
    setMaximumSize(new java.awt.Dimension(160, 40));
    setPreferredSize(new java.awt.Dimension(160, 40));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 160, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 40, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

	@Override
	public Component getTableCellRendererComponent(JTable LaTable, Object source, boolean isSelected, boolean hasFocus, int ligne, int colonne) 
	{
		removeAll();
		if(colonne==ma_tablemodel.COL) LaTable.getColumnModel().getColumn(colonne).setMinWidth(200);
		if(colonne==ma_tablemodel.NAME) 
		{
			LaTable.getColumnModel().getColumn(colonne).setMinWidth(250);
		}
		else
			LaTable.getColumnModel().getColumn(colonne).setMaxWidth(70exit;);
		
							
		setLayout(new FlowLayout(JLabel.CENTER));
		setBackground(new Color(31, 112, 121,255));
	
		
		
																							
		switch(colonne)
		{
			case ma_tablemodel.NAME:	add(new JLabel((String)source));
																this.getComponent(this.getComponentCount()-1).setForeground(new Color(255, 227, 126, 255));
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 16));	
																((FlowLayout)getLayout()).setVgap(baseline);
																
																break;
			case ma_tablemodel.COL:		BitSet valeur;
																valeur=(BitSet)((ma_Couleurs)source).DeckColors.clone(); 
																add(new ma_Couleurs(valeur));
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;

			case ma_tablemodel.VIC:		add(new JLabel((String)source));
																if(Integer.valueOf((String)source)!=0) this.getComponent(this.getComponentCount()-1).setForeground(new Color(208, 133, 67, 255));
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 15));
																largeur=((String)source).length();
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;
			case ma_tablemodel.DEF:		add(new JLabel((String)source));
																largeur=((String)source).length();
																if(Integer.valueOf((String)source)!=0) this.getComponent(this.getComponentCount()-1).setForeground(new Color(255, 227, 126, 255));
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 14));	
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;
			case ma_tablemodel.CON:		add(new JLabel((String)source));
																largeur=((String)source).length();
																if(Integer.valueOf((String)source)!=0) this.getComponent(this.getComponentCount()-1).setForeground(new Color(255, 227, 126, 255));	
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 14));
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;
			case ma_tablemodel.SCP:		add(new JLabel((String)source));
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 14));
																largeur=((String)source).length();
																this.getComponent(this.getComponentCount()-1).setForeground(new Color(121, 181, 181, 255));
																if(Integer.parseInt((String)source)<=0 || Integer.parseInt((String)source)>20)
																{
																	if(Integer.parseInt((String)source)<=0) this.getComponent(this.getComponentCount()-1).setForeground(new Color(255, 0, 0, 255));	
																	if(Integer.parseInt((String)source)>20) this.getComponent(this.getComponentCount()-1).setForeground(new Color(85, 85, 0, 255));	
																	setBackground(new Color(170, 170, 127, 255));
																}
																
																		
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;
			case ma_tablemodel.SCE:		add(new JLabel((String)source));
			
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 14));
																largeur=((String)source).length();
																this.getComponent(this.getComponentCount()-1).setForeground(new Color(121, 181, 181, 255));	
																if(Integer.parseInt((String)source)<=0 || Integer.parseInt((String)source)>20)
																{
																	if(Integer.parseInt((String)source)<=0) this.getComponent(this.getComponentCount()-1).setForeground(new Color(255, 0, 0, 255));	
																	if(Integer.parseInt((String)source)>20) this.getComponent(this.getComponentCount()-1).setForeground(new Color(85, 85, 0, 255));	
																	setBackground(new Color(170, 170, 127, 255));
																}
																	
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;
			case ma_tablemodel.MJ:		add(new JLabel((String)source));
																largeur=((String)source).length();
																if(Integer.valueOf((String)source)>1) 
																{
																	this.getComponent(this.getComponentCount()-1).setForeground(new Color(85, 255, 255, 255));
																	setBackground(new Color(255, 0, 0, 127));
																}
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 14));
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;
			case ma_tablemodel.DRWS:	add(new JLabel((String)source));
																largeur=((String)source).length();
																if(Integer.valueOf((String)source)!=0) this.getComponent(this.getComponentCount()-1).setForeground(new Color(255, 227, 126, 255));
																this.getComponent(this.getComponentCount()-1).setFont(new Font("Liberation Mono", Font.BOLD, 14));
																((FlowLayout)getLayout()).setVgap(baseline);
																((FlowLayout)getLayout()).setAlignment(FlowLayout.CENTER);
																break;														
	
																
		}
		this.validate();
		return this;
	}
	


	private int largeur;
	private final int baseline=12;

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
