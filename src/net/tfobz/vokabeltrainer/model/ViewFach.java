package net.tfobz.vokabeltrainer.model;

import java.awt.Font;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;

public class ViewFach extends JDialog
{
	private JScrollPane scrollKarten = null;
	private JTable tableKarten = null;
	
	private String[] columnNames = {"Wort 1", "Wort 2", "Richtung", "Groﬂ-/Kleinschreibung beachten"};
	private int lnummer;
	private int fnummer;
	private JTextField textField;
	
	public ViewFach(JFrame owner, int numLernkartei, int numFach) {
		super(owner, "Vokabeltrainer: Fachansicht");
		getContentPane().setLayout(null);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		
		this.lnummer = numLernkartei;
		this.fnummer = numFach;
		
		JLabel lblFachBearbeiten = new JLabel("Fach bearbeiten");
		lblFachBearbeiten.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFachBearbeiten.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblFachBearbeiten);
		
		Fach f = VokabeltrainerDB.getFaecher(lnummer).get(fnummer);
		List<Karte> karten = VokabeltrainerDB.getKarten(f.getNummer());
		
		String[][] karten_desc = new String[karten.size()][4];
		
		for(int i = 0; i < karten.size(); i++){
			karten_desc[i][0] = karten.get(i).getWortEins();
			karten_desc[i][1] = karten.get(i).getWortZwei();
			karten_desc[i][2] = String.valueOf(karten.get(i).getRichtung());
			karten_desc[i][3] = String.valueOf(karten.get(i).getGrossKleinschreibung());
		}
		
		columnNames[0] = VokabeltrainerDB.getLernkartei(lnummer).getWortEinsBeschreibung();
		columnNames[1] = VokabeltrainerDB.getLernkartei(lnummer).getWortZweiBeschreibung();
		
		tableKarten = new JTable();
		tableKarten.setModel(new DefaultTableModel(karten_desc, columnNames){public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }});
		scrollKarten = new JScrollPane(tableKarten);
		scrollKarten.setBounds(0, 88, 494, 326);
		this.getContentPane().add(scrollKarten);
		
		JButton btnNewButton = new JButton("Speichern");
		btnNewButton.setBounds(385, 427, 97, 25);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Abbrechen");
		btnNewButton_1.setBounds(276, 427, 97, 25);
		getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(10, 49, 216, 22);
		textField.setText(f.getBeschreibung());
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblErinnerungsintervall = new JLabel("Erinnerungsintervall:");
		lblErinnerungsintervall.setHorizontalAlignment(SwingConstants.RIGHT);
		lblErinnerungsintervall.setBounds(282, 52, 158, 16);
		getContentPane().add(lblErinnerungsintervall);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(452, 49, 30, 22);
		spinner.setValue(f.getErinnerungsIntervall());
		getContentPane().add(spinner);
		
		JButton btnNeueKarte = new JButton("Neue Karte");
		btnNeueKarte.setBounds(10, 427, 97, 25);
		getContentPane().add(btnNeueKarte);
		
	}
}
