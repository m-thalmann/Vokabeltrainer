package net.tfobz.vokabeltrainer.model;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LernenGUI extends JDialog
{
	private JFrame ownerFrame = null;
	private int lnummer = 0;
	private int[] numFaecher = null;
	private JTextField textField;
	private JTextField textField_1;
	private boolean changed = false;
	
	public LernenGUI(JFrame owner, int nummerLernkartei, int[] nummernFaecher){
		super(owner, "Vokabeltrainer: Lernen");
		getContentPane().setLayout(null);
		setSize(381, 222);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		
		this.ownerFrame = owner;
		this.lnummer = nummerLernkartei;
		this.numFaecher = nummernFaecher;
		
		JLabel lblLernen = new JLabel("Lernen");
		lblLernen.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLernen.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblLernen);
		
		JLabel lblWort = new JLabel(VokabeltrainerDB.getLernkartei(nummerLernkartei).getWortEinsBeschreibung() + ":");
		lblWort.setBounds(10, 49, 102, 16);
		getContentPane().add(lblWort);
		
		JLabel lblWort_1 = new JLabel(VokabeltrainerDB.getLernkartei(nummerLernkartei).getWortZweiBeschreibung() + ":");
		lblWort_1.setBounds(10, 94, 102, 16);
		getContentPane().add(lblWort_1);
		
		textField = new JTextField();
		textField.setBounds(124, 49, 231, 22);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(124, 91, 231, 22);
		getContentPane().add(textField_1);
		
		setVisible(true);
		
		
	}

	public boolean hasChanges() {
		return this.changed;
	}
}
