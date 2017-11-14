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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewKarten extends JDialog
{
	private JScrollPane scrollPane = null;
	private JTable table = null;
	
	private String[] columnNames = {"Fach", "Wort 1", "Wort 2"};
	private int lnummer = 0;
	private JFrame ownerFrame = null;
	
	public ViewKarten(JFrame owner, int nummerLernkartei) {
		super(owner, "Vokabeltrainer: Kartenansicht");
		getContentPane().setLayout(null);
		setSize(500, 464);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(owner);
		
		this.lnummer = nummerLernkartei;
		this.ownerFrame = owner;
		
		JLabel lblKartenAnsicht = new JLabel("Kartenansicht");
		lblKartenAnsicht.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblKartenAnsicht.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblKartenAnsicht);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 49, 494, 326);
		this.getContentPane().add(scrollPane);
		
		JButton btnSchlieen = new JButton("Schlie\u00DFen");
		btnSchlieen.setBounds(385, 388, 97, 25);
		btnSchlieen.setFocusPainted(false);
		btnSchlieen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(btnSchlieen);
		
		JButton btnNeueKarte = new JButton("Neue Karte");
		btnNeueKarte.setBounds(10, 388, 97, 25);
		btnNeueKarte.setFocusPainted(false);
		btnNeueKarte.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				NeueKarte nk = new NeueKarte(ownerFrame, lnummer);
				if(nk.isSaved()){
					updateView();
				}
				nk.dispose();
			}
		});
		getContentPane().add(btnNeueKarte);
		
		columnNames[1] = VokabeltrainerDB.getLernkartei(lnummer).getWortEinsBeschreibung();
		columnNames[2] = VokabeltrainerDB.getLernkartei(lnummer).getWortZweiBeschreibung();
		
		updateView();
		
		setVisible(true);
	}
	
	private void updateView(){
		List<Fach> fachList = VokabeltrainerDB.getFaecher(lnummer);
		
		int n = 0;
		
		for(int i = 0; i < fachList.size(); i++){
			List<Karte> kartenList = VokabeltrainerDB.getKarten(fachList.get(i).getNummer());
			for(int j = 0; j < kartenList.size(); j++){
				n++;
			}
		}
		
		String[][] karten_desc = new String[n][3];
		final int[] kartenNummern = new int[n];
		
		for(int i = 0; i < fachList.size(); i++){
			List<Karte> kartenList = VokabeltrainerDB.getKarten(fachList.get(i).getNummer());

			for(int j = 0; j < kartenList.size(); j++){
				n--;
				Karte k = kartenList.get(j);
				
				karten_desc[n][0] = fachList.get(i).getBeschreibung();
				karten_desc[n][1] = k.getWortEins();
				karten_desc[n][2] = k.getWortZwei();
				kartenNummern[n] = k.getNummer();
			}
		}
		
		table = new JTable();
		table.setModel(new DefaultTableModel(karten_desc, columnNames){public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }});
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent mouseEvent) {
	        JTable table =(JTable) mouseEvent.getSource();
	        Point point = mouseEvent.getPoint();
	        int row = table.rowAtPoint(point);
	        if (mouseEvent.getClickCount() == 2) {
            if(row != -1){
            	AendernKarte ak = new AendernKarte(ownerFrame, lnummer, kartenNummern[row]);
  						if(ak.isSaved()){ 
  							updateView();
  						}
  						ak.dispose();
            }
	        }
	    }
		});
		scrollPane.setViewportView(table);
	}
}
