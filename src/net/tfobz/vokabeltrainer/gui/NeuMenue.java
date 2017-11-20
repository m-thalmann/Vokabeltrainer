package net.tfobz.vokabeltrainer.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Klasse, welche dem Benutzer die M�glichkeit
 * bietet, eine neue Lernkartei oder ein neues
 * Fach zum Lernen zu erstellen
 */

public class NeuMenue extends JDialog {
	
	private int saved_num = -1;
	private int lnummer = 0;
	private JFrame ownerFrame = null;
	
	public static final int LERNKARTEI = 0;
	public static final int FACH = 1;
	
	public NeuMenue(JFrame owner, int numLernkartei) {
		setTitle("Kartei/Fach erstellen");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 171);
		setLocationRelativeTo(owner);
		getContentPane().setLayout(null);
		setModal(true);

		this.ownerFrame = owner;
		this.lnummer = numLernkartei;
		
		//Erstellt eine neue Lernkartei
		JButton n1 = new JButton("Lernkartei");
		n1.setBounds(22, 49, 183, 25);
		n1.setFocusPainted(false);
		n1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NeueLernkartei nl = new NeueLernkartei(ownerFrame);
				if(nl.isSaved()){
					saved_num = NeuMenue.LERNKARTEI;
				}
				nl.dispose();
				setVisible(false);
			}
		});
		getContentPane().add(n1);
		
		//Erstellt ein neues Fach
		JButton n2 = new JButton("Fach");
		n2.setBounds(22, 87, 183, 25);
		n2.setFocusPainted(false);
		n2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NeuesFach nf = new NeuesFach(ownerFrame, lnummer);
				if(nf.isSaved()){
					saved_num = NeuMenue.FACH;
				}
				nf.dispose();
				setVisible(false);
			}
		});
		getContentPane().add(n2);
		
		JLabel lblNeu = new JLabel("Neu");
		lblNeu.setHorizontalAlignment(SwingConstants.CENTER);
		lblNeu.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNeu.setBounds(22, 13, 183, 25);
		getContentPane().add(lblNeu);
		
		setVisible(true);
	}
	
	/**
	 * Gibt an, ob eine Option gew�hlt wurde
	 * @return true, wenn ein Fach oder eine Lernkartei angelegt wurde
	 */
	public boolean isSaved(){
		return this.saved_num != -1;
	}
	
	/**
	 * Gibt die Nummer zur�ck, welche Option gew�hlt wurde
	 * @return 0 f�r eine Lernkartei, 1 f�r ein Fach
	 */
	public int getSaved(){
		return this.saved_num;
	}
}
