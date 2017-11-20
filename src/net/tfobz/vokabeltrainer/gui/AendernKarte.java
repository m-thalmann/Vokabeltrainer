package net.tfobz.vokabeltrainer.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.tfobz.vokabeltrainer.model.*;

/**
 * Klasse, welche eine Karte abändert. Dabei
 * können nur die Wörter geändert werden oder 
 * die gesamte Karte gelöscht werden. Der 
 * Benutzer kann nicht die Übersetzungsrichtung
 * oder Groß - und Kleinschreibung ändern
 */

public class AendernKarte extends JDialog
{
	
	private int num = 0;
	private int lnummer = 0;
	private boolean saved = false;
	private JFrame ownerFrame = null;
	private JButton ok = null;

	public AendernKarte(JFrame owner, int nummerLernkartei, int nummerKarte) {
		super(owner, "Vokabeltrainer: Karte bearbeiten");
		getContentPane().setLayout(null);
		setSize(397, 179);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(owner);
		
		this.num = nummerKarte;
		this.ownerFrame = owner;
		this.lnummer = nummerLernkartei;

		JLabel titel, first, second;
		JButton cancel;
		final JTextField firstText;
		final JTextField secondText;
		
		titel = new JLabel("Wort bearbeiten");
		titel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		titel.setBounds(10, 11, 200, 25);
		this.getContentPane().add(titel);

		first = new JLabel(VokabeltrainerDB.getLernkartei(nummerLernkartei).getWortEinsBeschreibung() + ": ");
		first.setBounds(10, 44, 100, 20);

		second = new JLabel(VokabeltrainerDB.getLernkartei(nummerLernkartei).getWortZweiBeschreibung() + ": ");
		second.setBounds(10, 84, 100, 20);
		
		this.getContentPane().add(first);
		this.getContentPane().add(second);

		firstText = new JTextField();
		firstText.setBounds(120, 44, 258, 20);
		firstText.setText(VokabeltrainerDB.getKarte(num).getWortEins());
		firstText.addKeyListener(new KeyAdapter()
		{

			//Ermöglicht springen zum zweiten Textfeld mit Enter
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					ok.doClick();
				}
			}
		});

		secondText = new JTextField();
		secondText.setBounds(120, 84, 258, 20);
		secondText.setText(VokabeltrainerDB.getKarte(num).getWortZwei());
		secondText.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					ok.doClick();
				}
			}
		});

		this.getContentPane().add(firstText);
		this.getContentPane().add(secondText);
		
		JButton loeschen = new JButton("Löschen");
		loeschen.setBounds(146, 116, 100, 23);
		loeschen.setFocusPainted(false);
		loeschen.setMnemonic(KeyEvent.VK_L);
		loeschen.addActionListener(new ActionListener()
		{

			//Löschvorgang mit Speicherung und Warnung
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(ownerFrame, "Wollen Sie wirklich diese Karte löschen?", "Warnung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					VokabeltrainerDB.loeschenKarte(num);
					saved = true;
					setVisible(false);
				}
			}
		});
		this.getContentPane().add(loeschen);

		ok = new JButton("Speichern");
		ok.setBounds(278, 115, 100, 25);
		ok.setFocusPainted(false);
		ok.setMnemonic(KeyEvent.VK_S);
		ok.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				Karte k = new Karte(num, firstText.getText(), secondText.getText(),
						VokabeltrainerDB.getLernkartei(lnummer).getRichtung(),
						VokabeltrainerDB.getLernkartei(lnummer).getGrossKleinschreibung());
				switch (VokabeltrainerDB.aendernKarte(k)) {
					case -1: {
						JOptionPane.showMessageDialog(AendernKarte.this,
								"Ein Datenbankfehler ist aufgetreten!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
						break;
					}
					case -2: {
						JOptionPane.showMessageDialog(AendernKarte.this,
								"Ein Validierungsfehler ist aufgetreten!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
						break;
					}
					case -4:{
						JOptionPane.showMessageDialog(AendernKarte.this,
								"Diese Karte ist bereits vorhanden!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
						break;
					}
					default: {
						saved = true;
						setVisible(false);
					}
				}
			}
		});

		this.getContentPane().add(ok);

		cancel = new JButton("Abbrechen");
		cancel.setBounds(10, 115, 100, 25);
		cancel.setFocusPainted(false);
		cancel.setMnemonic(KeyEvent.VK_A);
		cancel.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		this.getContentPane().add(cancel);
		
		setVisible(true);
	}
	
	//Gibt den "Gespeichert"- Status der jeweiligen Operation zurück
	public boolean isSaved() {
		return this.saved;
	}
}
