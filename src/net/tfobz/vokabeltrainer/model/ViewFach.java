package net.tfobz.vokabeltrainer.model;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewFach extends JDialog
{
	private JScrollPane scrollKarten = null;
	private JTable tableKarten = null;
	
	private String[] columnNames = {"Beschreibung", "Zuletzt gelernt", "Erinnerung (Tage)", "Fällig"};
	
	public ViewFach(JFrame owner, int num) {
		super(owner, "Vokabeltrainer: Fachansicht");
		getContentPane().setLayout(null);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		
		String[][] karten = new String[VokabeltrainerDB.getKarten(num).size()][4];
		
		tableKarten = new JTable();
		tableKarten.setModel(new DefaultTableModel(karten, columnNames){public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }});
		scrollKarten = new JScrollPane(tableKarten);
	}
}
