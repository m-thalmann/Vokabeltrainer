package net.tfobz.vokabeltrainer.model;

import java.awt.FlowLayout;
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
import javax.swing.JPanel;
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
	
	private String[] columnNames = {"Wort 1", "Wort 2", "Richtung", "Groﬂ-/Kleinschreibung beachten"};
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
		
		JLabel lblFachBearbeiten = new JLabel("Fach bearbeiten");
		lblFachBearbeiten.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFachBearbeiten.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblFachBearbeiten);
		
		f = VokabeltrainerDB.getFaecher(lnummer).get(fnummer);
		karten = VokabeltrainerDB.getKarten(f.getNummer());
		
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
		tableKarten.setFillsViewportHeight(true);
		tableKarten.getTableHeader().setReorderingAllowed(false);
		tableKarten.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent mouseEvent) {
	        JTable table =(JTable) mouseEvent.getSource();
	        Point point = mouseEvent.getPoint();
	        int row = table.rowAtPoint(point);
	        if (mouseEvent.getClickCount() == 2) {
            if(row != -1){
            	//TODO
            }
	        }
	    }
		});
		scrollKarten = new JScrollPane(tableKarten);
		scrollKarten.setBounds(0, 88, 494, 326);
		this.getContentPane().add(scrollKarten);
		
		JButton btnNewButton = new JButton("Speichern");
		btnNewButton.setFocusPainted(false);
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
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
							JOptionPane.showMessageDialog(ViewFach.this, "Ein Validierungsfehler ist aufgetreten! ‹berpr¸fen Sie ihre Eingaben.", "Fehler", JOptionPane.ERROR_MESSAGE);
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
		
		JButton btnNewButton_1 = new JButton("Abbrechen");
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.setVerticalAlignment(SwingConstants.TOP);
		btnNewButton_1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		textField = new JTextField();
		textField.setBounds(10, 49, 216, 22);
		textField.setText(f.getBeschreibung());
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblErinnerungsintervall = new JLabel("Erinnerungsintervall:");
		lblErinnerungsintervall.setHorizontalAlignment(SwingConstants.RIGHT);
		lblErinnerungsintervall.setBounds(282, 52, 158, 16);
		getContentPane().add(lblErinnerungsintervall);
		
		spinner = new JSpinner();
		spinner.setBounds(452, 49, 30, 22);
		spinner.setValue(f.getErinnerungsIntervall());
		getContentPane().add(spinner);
		
		JButton btnNeueKarte = new JButton("Neue Karte");
		btnNeueKarte.setFocusPainted(false);
		btnNeueKarte.setVerticalAlignment(SwingConstants.TOP);
		btnNeueKarte.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		
		JButton btnLschen = new JButton("Karten l\u00F6schen");
		btnLschen.setFocusPainted(false);
		btnLschen.setVerticalAlignment(SwingConstants.TOP);
		btnLschen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(ViewFach.this, "Wollen Sie wirklich dieses Fach mit all seinen Karten lˆschen?", "Achtung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					List<Karte> karten = VokabeltrainerDB.getKarten(f.getNummer());
					
					for(int i = 0; i < karten.size(); i++){
						VokabeltrainerDB.loeschenKarte(karten.get(i).getNummer());
					}
					
					JOptionPane.showMessageDialog(ViewFach.this, "Karten wurden erfolgreich gelˆscht!", "Information", JOptionPane.INFORMATION_MESSAGE);
					
					setVisible(false);
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(10, 420, 472, 35);
		
		getContentPane().add(buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(btnNeueKarte);
		buttonPanel.add(btnLschen);
		buttonPanel.add(btnNewButton_1);
		buttonPanel.add(btnNewButton);
		
	}
	
	public boolean isSaved(){
		return this.saved;
	}
}
