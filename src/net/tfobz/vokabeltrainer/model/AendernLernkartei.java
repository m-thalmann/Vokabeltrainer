package net.tfobz.vokabeltrainer.model;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AendernLernkartei extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Create the dialog.
	 * @param neu 
	 * @param vokabeltrainerGUI 
	 */
	public AendernLernkartei(JFrame owner) {
		super(owner, "Vokabeltrainer: Lernkartei bearbeiten");
		setBounds(100, 100, 450, 400);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblLernkarteiBearbeiten = new JLabel("Lernkartei bearbeiten");
			lblLernkarteiBearbeiten.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblLernkarteiBearbeiten.setBounds(10, 11, 200, 25);
			contentPanel.add(lblLernkarteiBearbeiten);
		}
		{
			JLabel lblBeschreibung = new JLabel("Beschreibung: ");
			lblBeschreibung.setBounds(10, 60, 200, 20);
			contentPanel.add(lblBeschreibung);
		}
		
		textField = new JTextField();
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
						dispose();
					}
				});
				buttonPane.add(abbrechButton);
			}
			{
				JButton aenderButton = new JButton("Ändern");
				aenderButton.setActionCommand("Ändern");
				aenderButton.setFocusPainted(false);
				buttonPane.add(aenderButton);
			}
			JPanel field = new JPanel();
			field.setBounds(10, 129, 414, 100);
			field.setBorder(BorderFactory.createTitledBorder("Wort"));
			contentPanel.add(field);
			
			ButtonGroup sprache = new ButtonGroup();
			
			{
				JRadioButton erstesWort = new JRadioButton("Erstes Wort -> Zweites Wort");
				erstesWort.setBounds(10, 238, 200, 23);
				contentPanel.add(erstesWort);
				sprache.add(erstesWort);
			}
			{
				JRadioButton zweitesWort = new JRadioButton("Zweites Wort -> Erstes Wort");
				zweitesWort.setBounds(10, 264, 200, 23);
				contentPanel.add(zweitesWort);
				sprache.add(zweitesWort);
			}
			
			{
				JCheckBox grossKleinBox = new JCheckBox("Gro\u00DF/Kleinschreibung");
				grossKleinBox.setBounds(259, 238, 165, 23);
				contentPanel.add(grossKleinBox);
			}
		}
	}
}
