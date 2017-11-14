package net.tfobz.vokabeltrainer.model;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ViewFach extends JDialog
{
	private JScrollPane scrollKarten = null;
	private JTable tableKarten = null;
	private JTextField textField;
	private JSpinner spinner;
	private JFrame ownerFrame = null;
	
	private String[] columnNames = {"Wort 1", "Wort 2"};
	private int lnummer;
	private int fnummer;
	private boolean saved = false;
	
	private Fach f;
	private List<Karte> karten;
	
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
		this.ownerFrame = owner;
		
		JLabel lblFachBearbeiten = new JLabel("Fach bearbeiten");
		lblFachBearbeiten.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFachBearbeiten.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblFachBearbeiten);
		
		f = VokabeltrainerDB.getFaecher(lnummer).get(fnummer);
		
		scrollKarten = new JScrollPane();
		scrollKarten.setBounds(0, 88, 494, 326);
		this.getContentPane().add(scrollKarten);
		
		textField = new JTextField();
		textField.setBounds(10, 49, 216, 22);
		textField.setText(f.getBeschreibung());
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblErinnerungsintervall = new JLabel("Erinnerungsintervall:");
		lblErinnerungsintervall.setHorizontalAlignment(SwingConstants.RIGHT);
		lblErinnerungsintervall.setBounds(270, 52, 158, 16);
		getContentPane().add(lblErinnerungsintervall);
		
		spinner = new JSpinner();
		spinner.setBounds(440, 49, 42, 22);
		spinner.setValue(f.getErinnerungsIntervall());
		getContentPane().add(spinner);
		
		JButton btnNewButton = new JButton("Speichern");
		btnNewButton.setBounds(393, 428, 89, 24);
		getContentPane().add(btnNewButton);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		
		JButton btnNewButton_1 = new JButton("Abbrechen");
		btnNewButton_1.setBounds(288, 428, 93, 24);
		getContentPane().add(btnNewButton_1);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setVerticalAlignment(SwingConstants.TOP);
		btnNewButton_1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(f.getBeschreibung() != textField.getText() || f.getErinnerungsIntervall() != Integer.parseInt(spinner.getValue().toString())){
					switch(VokabeltrainerDB.aendernFach(new Fach(f.getNummer(), textField.getText(), Integer.parseInt(spinner.getValue().toString()), f.getGelerntAm()))){
						case -1:{
							JOptionPane.showMessageDialog(ViewFach.this, "Ein Datenbankfehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						case -2:{
							JOptionPane.showMessageDialog(ViewFach.this, "Ein Validierungsfehler ist aufgetreten! Überprüfen Sie ihre Eingaben.", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						default:{
							saved = true;
						}
					}
				}else{
					saved = true;
				}
				
				if(saved){
					setVisible(false);
				}
			}
		});
		
		updateView();
		
		setVisible(true);
	}
	
	private void updateView() {
		karten = VokabeltrainerDB.getKarten(f.getNummer());

		String[][] karten_desc = new String[karten.size()][2];
		final int[] kartenNummern = new int[karten.size()];

		for (int i = 0; i < karten.size(); i++) {
			karten_desc[i][0] = karten.get(i).getWortEins();
			karten_desc[i][1] = karten.get(i).getWortZwei();
			kartenNummern[i] = karten.get(i).getNummer();
		}

		columnNames[0] = VokabeltrainerDB.getLernkartei(lnummer)
				.getWortEinsBeschreibung();
		columnNames[1] = VokabeltrainerDB.getLernkartei(lnummer)
				.getWortZweiBeschreibung();

		tableKarten = new JTable();
		tableKarten.setModel(new DefaultTableModel(karten_desc, columnNames)
		{
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});
		tableKarten.setFillsViewportHeight(true);
		tableKarten.getTableHeader().setReorderingAllowed(false);
		tableKarten.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2) {
					if (row != -1) {
						AendernKarte ak = new AendernKarte(ownerFrame, kartenNummern[row]);
						if(ak.isSaved()){ 
							updateView();
						}
						ak.dispose();
					}
				}
			}
		});
		
		scrollKarten.setViewportView(tableKarten);
	}
	
	public boolean isSaved(){
		return this.saved;
	}
}
