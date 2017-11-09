package net.tfobz.vokabeltrainer.model;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VokabeltrainerGUI extends JFrame {

	private JPanel contentPane;
	JList<String> faecherListe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VokabeltrainerGUI frame = new VokabeltrainerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VokabeltrainerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel vokabeltitel = new JLabel("Vokabeltrainer");
		vokabeltitel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		vokabeltitel.setBounds(235, 11, 120, 30);
		contentPane.add(vokabeltitel);

		JButton start = new JButton("Start");
		start.setBounds(485, 44, 89, 23);
		start.setFocusPainted(false);
		contentPane.add(start);

		JLabel lernkartei = new JLabel("Lernkartei:");
		lernkartei.setBounds(10, 44, 65, 23);
		contentPane.add(lernkartei);

		JButton importieren = new JButton("Importieren");
		importieren.setBounds(10, 378, 100, 23);
		importieren.setFocusPainted(false);
		importieren.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser importFileChooser = new JFileChooser();

				importFileChooser.setMultiSelectionEnabled(false);
				importFileChooser.setFileFilter(new FileNameExtensionFilter("Textdateien", "txt"));
				importFileChooser.setAcceptAllFileFilterUsed(false);
				
				if(importFileChooser.showOpenDialog(VokabeltrainerGUI.this) == JFileChooser.APPROVE_OPTION){
					String path = importFileChooser.getSelectedFile().getAbsolutePath();
					
					if(path.endsWith(".txt")){
						File file = new File(path);
						
						if(!file.isDirectory() && file.isFile()){
							
						}
						else{
							JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Die Datei \"" + path + "\" existiert nicht.", "Achtung", JOptionPane.WARNING_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Die Datei \"" + path + "\" ist keine Textdatei (*.txt)", "Achtung", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		contentPane.add(importieren);

		JButton loeschen = new JButton("L\u00F6schen");
		loeschen.setBounds(120, 378, 89, 23);
		loeschen.setFocusPainted(false);
		loeschen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int loeschenAnswer = JOptionPane.showConfirmDialog(VokabeltrainerGUI.this,
						"Sie sind dabei diese Datei zu löschen.Wollen Sie diese Lernkartei wirklich löschen?",
						"Achtung", JOptionPane.OK_CANCEL_OPTION);
				//System.out.println(loeschenAnswer);
				if (loeschenAnswer == 2) {
					dispose();
				} else {
					//Löschen
				}
			}
		});
		contentPane.add(loeschen);

		JButton aendern = new JButton("\u00C4ndern");
		aendern.setBounds(219, 378, 89, 23);
		aendern.setFocusPainted(false);
		aendern.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AendernLernkartei aenderFenster = new AendernLernkartei(VokabeltrainerGUI.this);
				aenderFenster.setVisible(true);

			}
		});
		contentPane.add(aendern);

		JButton vor = new JButton(">");
		vor.setBounds(533, 378, 41, 23);
		vor.setFocusPainted(false);
		contentPane.add(vor);

		JButton zurueck = new JButton("<");
		zurueck.setBounds(485, 378, 41, 23);
		zurueck.setFocusPainted(false);
		contentPane.add(zurueck);

		JScrollPane scrollPane = new JScrollPane(faecherListe);
		scrollPane.setBounds(10, 74, 564, 293);
		contentPane.add(scrollPane);

		JButton neu = new JButton("Neu");
		neu.setBounds(407, 378, 65, 23);
		neu.setFocusPainted(false);
		neu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(VokabeltrainerGUI.this,
						"Wollen Sie eine neue Lernkarte anlegen?", "Vokabeltrainer: Lernkartei anlegen",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
				/*
				 * int test = JOptionPane.YES_OPTION; int test2 =
				 * JOptionPane.NO_OPTION;
				 * 
				 * System.out.println(test + " " + test2);
				 */
				if (answer == 0) {
					dispose();
				} else {
					// Neuanlegen
				}
			}
		});
		contentPane.add(neu);

		JLabel abgearbeitet = new JLabel("0");
		abgearbeitet.setHorizontalAlignment(SwingConstants.CENTER);
		abgearbeitet.setBounds(66, 48, 31, 14);
		contentPane.add(abgearbeitet);
	}
}
