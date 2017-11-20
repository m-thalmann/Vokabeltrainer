package net.tfobz.vokabeltrainer.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.tfobz.vokabeltrainer.model.*;

/**
 * Klasse, welche eine neue Karte für
 * ein Fach anlegt. Die Wörter werden so
 * übersetzt, wie in der Lernkartei angegeben
 * wurde. Hinzufügen von neuer Sprache
 * nur über das Fach
 */

public class NeueKarte extends JDialog
{
	private int lnummer = 0;
	private boolean saved = false;

	public NeueKarte(JFrame owner, int nummerLernkartei) {
		super(owner, "Vokabeltrainer: Karte hinzufügen");
		getContentPane().setLayout(null);
		setSize(397, 179);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(owner);

		this.lnummer = nummerLernkartei;

		JLabel titel, erstes, zweites;
		final JTextField erstesWort;
		final JTextField zweitesWort;
		JButton speichern, abbrechen;

		titel = new JLabel("Neues Wort");
		titel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		titel.setBounds(10, 11, 200, 25);
		this.getContentPane().add(titel);
		
		erstesWort = new JTextField();
		erstesWort.setBounds(120, 44, 258, 20);

		zweitesWort = new JTextField();
		zweitesWort.setBounds(120, 84, 258, 20);

		this.getContentPane().add(erstesWort);
		this.getContentPane().add(zweitesWort);
		
		erstes = new JLabel(VokabeltrainerDB.getLernkartei(lnummer)
				.getWortEinsBeschreibung() + ": ");
		erstes.setBounds(10, 44, 100, 20);

		zweites = new JLabel(VokabeltrainerDB.getLernkartei(lnummer)
				.getWortZweiBeschreibung() + ": ");
		zweites.setBounds(10, 84, 100, 20);

		this.getContentPane().add(erstes);
		this.getContentPane().add(zweites);

		speichern = new JButton("Speichern");
		speichern.setBounds(278, 115, 100, 25);
		speichern.setMnemonic(KeyEvent.VK_S);
		speichern.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				Karte k = new Karte(-1, erstesWort.getText(), zweitesWort.getText(),
						VokabeltrainerDB.getLernkartei(lnummer).getRichtung(),
						VokabeltrainerDB.getLernkartei(lnummer).getGrossKleinschreibung());
				switch (VokabeltrainerDB.hinzufuegenKarte(lnummer, k)) {
					case -1: {
						JOptionPane.showMessageDialog(NeueKarte.this,
								"Ein Datenbankfehler ist aufgetreten!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
						break;
					}
					case -2: {
						JOptionPane.showMessageDialog(NeueKarte.this,
								"Ein Validierungsfehler ist aufgetreten!", "Fehler",
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

		this.getContentPane().add(speichern);

		abbrechen = new JButton("Abbrechen");
		abbrechen.setBounds(173, 115, 100, 25);
		abbrechen.setMnemonic(KeyEvent.VK_A);
		abbrechen.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		this.getContentPane().add(abbrechen);
		
		setVisible(true);
	}

	public boolean isSaved() {
		return this.saved;
		
	}
}
