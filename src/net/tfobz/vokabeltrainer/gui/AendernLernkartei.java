package net.tfobz.vokabeltrainer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.tfobz.vokabeltrainer.model.*;

/**
 * Klasse, welche eine Lernkartei ab�ndern kann.
 * Der Benutzer kann nach wie vor zwischen
 * Ersten Wort -> Zweites Wort oder umgekehrt
 * sowie Gro� - und Kleinschreibung ausw�hlen.
 * Auch die W�rter sind ver�nderbar
 */

public class AendernLernkartei extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	private boolean saved = false;
	private JTextField textField_1;
	private JTextField textField_2;
	
	private JRadioButton erstesWort = null;
	private JRadioButton zweitesWort = null;
	private JCheckBox grossKleinBox = null;
	
	private int num = 0;
	
	public AendernLernkartei(JFrame owner, int pos) {
		super(owner, "Vokabeltrainer: Lernkartei �ndern");
		this.num = pos;
		setBounds(100, 100, 439, 400);
		setModal(true);
		setLocationRelativeTo(owner);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblLernkarteiBearbeiten = new JLabel("Lernkartei �ndern");
			lblLernkarteiBearbeiten.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblLernkarteiBearbeiten.setBounds(10, 11, 200, 25);
			contentPanel.add(lblLernkarteiBearbeiten);
		}
		{
			JLabel lblBeschreibung = new JLabel("Beschreibung: ");
			lblBeschreibung.setBounds(10, 60, 200, 20);
			lblBeschreibung.setLabelFor(textField);
			lblBeschreibung.setDisplayedMnemonic(KeyEvent.VK_B);
			contentPanel.add(lblBeschreibung);
		}
		
		textField = new JTextField(VokabeltrainerDB.getLernkartei(num).getBeschreibung());
		textField.setBounds(10, 85, 414, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton abbrechButton = new JButton("Abbrechen");
				abbrechButton.setActionCommand("Abbrechen");
				abbrechButton.setFocusPainted(false);
				abbrechButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				buttonPane.add(abbrechButton);
			}
			{
				JButton aenderButton = new JButton("�ndern");
				aenderButton.setActionCommand("�ndern");
				aenderButton.setFocusPainted(false);
				aenderButton.setMnemonic(KeyEvent.VK_N);
				aenderButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {
						switch(VokabeltrainerDB.aendernLernkartei((new Lernkartei(num, textField.getText(), textField_1.getText(), textField_2.getText(), erstesWort.isSelected(), grossKleinBox.isSelected())))){
							case -1:{
								JOptionPane.showMessageDialog(AendernLernkartei.this, "Ein Datenbankfehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
								break;
							}
							case -2:{
								JOptionPane.showMessageDialog(AendernLernkartei.this, "Ein Validierungsfehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
								break;
							}
							default:{
								saved = true;
								setVisible(false);
							}
						}
					}
				});
				buttonPane.add(aenderButton);
			}
			JPanel field = new JPanel();
			field.setBounds(10, 129, 414, 90);
			field.setBorder(BorderFactory.createTitledBorder("Wort"));
			contentPanel.add(field);
			field.setLayout(null);
			
			JLabel lblBeschreibungErstesWort = new JLabel("Beschreibung erstes Wort");
			lblBeschreibungErstesWort.setBounds(14, 26, 181, 16);
			lblBeschreibungErstesWort.setHorizontalAlignment(SwingConstants.LEFT);
			lblBeschreibungErstesWort.setLabelFor(textField_1);
			lblBeschreibungErstesWort.setDisplayedMnemonic(KeyEvent.VK_E);
			field.add(lblBeschreibungErstesWort);
			
			textField_1 = new JTextField(VokabeltrainerDB.getLernkartei(num).getWortEinsBeschreibung());
			textField_1.setBounds(191, 23, 209, 22);
			field.add(textField_1);
			textField_1.setColumns(20);
			
			JLabel lblBeschreibungZweitesWort = new JLabel("Beschreibung zweites Wort");
			lblBeschreibungZweitesWort.setBounds(14, 53, 181, 16);
			lblBeschreibungZweitesWort.setHorizontalAlignment(SwingConstants.LEFT);
			lblBeschreibungZweitesWort.setLabelFor(textField_2);
			lblBeschreibungZweitesWort.setDisplayedMnemonic(KeyEvent.VK_Z);
			field.add(lblBeschreibungZweitesWort);
			
			textField_2 = new JTextField(VokabeltrainerDB.getLernkartei(num).getWortZweiBeschreibung());
			textField_2.setBounds(191, 50, 209, 22);
			field.add(textField_2);
			textField_2.setColumns(20);
			
			ButtonGroup sprache = new ButtonGroup();
			
			{
				erstesWort = new JRadioButton("Erstes Wort -> Zweites Wort");
				erstesWort.setBounds(10, 238, 200, 23);
				erstesWort.setSelected(VokabeltrainerDB.getLernkartei(num).getRichtung());
				erstesWort.setEnabled(false);
				contentPanel.add(erstesWort);
				sprache.add(erstesWort);
			}
			{
				zweitesWort = new JRadioButton("Zweites Wort -> Erstes Wort");
				zweitesWort.setBounds(10, 264, 200, 23);
				zweitesWort.setSelected(!VokabeltrainerDB.getLernkartei(num).getRichtung());
				zweitesWort.setEnabled(false);
				contentPanel.add(zweitesWort);
				sprache.add(zweitesWort);
			}
			
			{
				//Aktiviert/Deaktiviert Gro�- und Kleinschreibung
				grossKleinBox = new JCheckBox("Gro\u00DF/Kleinschreibung");
				grossKleinBox.setBounds(259, 238, 165, 23);
				grossKleinBox.setSelected(VokabeltrainerDB.getLernkartei(num).getGrossKleinschreibung());
				grossKleinBox.setEnabled(false);
				grossKleinBox.setMnemonic(KeyEvent.VK_O);
				contentPanel.add(grossKleinBox);
			}
		}
		
		setVisible(true);
	}
	
	public boolean isSaved(){
		return this.saved;
	}
}
