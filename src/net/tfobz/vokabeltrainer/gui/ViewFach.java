package net.tfobz.vokabeltrainer.gui;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.tfobz.vokabeltrainer.model.*;

/**
 *
 * Diese Klasse zeigt alle Karten in einem
 * gewissen Fach,in einer gewissen Lernkartei
 *
 */

public class ViewFach extends JDialog
{
	private JScrollPane scrollKarten = null;
	private JTable tableKarten = null;
	private JTextField textField;
	private JSpinner spinner;
	private JFrame ownerFrame = null;
	private JButton btnKarteBearbeiten = null;
	
	private String[] columnNames = {"Wort 1", "Wort 2"};
	
	private int lnummer;
	private int fnummer;
	private boolean saved = false;
	private int[] kartenNummern = null;
	
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
		
		//Speichern
		JButton btnNewButton = new JButton("Speichern");
		btnNewButton.setBounds(393, 428, 89, 24);
		btnNewButton.setMnemonic(KeyEvent.VK_S);
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
		getContentPane().add(btnNewButton);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		
		//Abbrechen
		JButton btnNewButton_1 = new JButton("Abbrechen");
		btnNewButton_1.setBounds(288, 428, 93, 24);
		btnNewButton_1.setMnemonic(KeyEvent.VK_A);
		btnNewButton_1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(btnNewButton_1);
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setVerticalAlignment(SwingConstants.TOP);
		
		//Karte bearbeiten
		btnKarteBearbeiten = new JButton("Karte bearbeiten");
		btnKarteBearbeiten.setBounds(10, 428, 136, 25);
		btnKarteBearbeiten.setFocusPainted(false);
		btnKarteBearbeiten.setMnemonic(KeyEvent.VK_B);
		btnKarteBearbeiten.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableKarten.getSelectedRow() != -1){
					AendernKarte ak = new AendernKarte(ownerFrame, lnummer, kartenNummern[tableKarten.getSelectedRow()]);
					if(ak.isSaved()){ 
						updateView();
					}
					ak.dispose();
				}
			}
		});
		getContentPane().add(btnKarteBearbeiten);
		
		updateView();
		
		setVisible(true);
	}
	
	/**
	 * Diese Methode holt sich die Informationen aus der Datenbank
	 * und füllt die Tabelle und die Labels dementsprechend
	 */
	private void updateView() {
		karten = VokabeltrainerDB.getKarten(f.getNummer());

		String[][] karten_desc = new String[karten.size()][2];
		kartenNummern = new int[karten.size()];

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
						AendernKarte ak = new AendernKarte(ownerFrame, lnummer, kartenNummern[row]);
						if(ak.isSaved()){ 
							updateView();
						}
						ak.dispose();
					}
				}
			}
		});
		
		scrollKarten.setViewportView(tableKarten);
		
		if(tableKarten.getSelectedRow() == -1){
			btnKarteBearbeiten.setEnabled(false);
		}else{
			btnKarteBearbeiten.setEnabled(true);
		}
		
		tableKarten.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
      public void valueChanged(ListSelectionEvent event) {
      	if(tableKarten.getSelectedRow() == -1){
    			btnKarteBearbeiten.setEnabled(false);
    		}else{
    			btnKarteBearbeiten.setEnabled(true);
    		}
      }
		});
	}
	
	public boolean isSaved(){
		return this.saved;
	}
}
