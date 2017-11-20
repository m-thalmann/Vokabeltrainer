package net.tfobz.vokabeltrainer.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import net.tfobz.vokabeltrainer.model.*;

//TODO Du Fotze do hon i wos geändert

public class VokabeltrainerGUI extends JFrame {

	private JPanel contentPane = null;
	private JTable faecherListe= null;
	private JCheckBox chckbxNurFlligeFcher = null;
	private int num = 0;
	private JLabel pos = null;
	private JLabel vokabeltitel = null;
	private String[] columnNames = {"Beschreibung", "Zuletzt gelernt", "Erinnerung (Tage)", "Fällig"};
	private JScrollPane scrollPane= null;
	private Lernkartei l = null;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new VokabeltrainerGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VokabeltrainerGUI() {
		super("Vokabeltrainer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 435);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		
		if(VokabeltrainerDB.getLernkarteien().size() == 0){
			JOptionPane.showMessageDialog(this, "Sie besitzen noch keine Lernkartei. Sie müssen eine Anlegen um fortfahren zu können.", "Willkommen", JOptionPane.INFORMATION_MESSAGE);
			
			NeueLernkartei nl = new NeueLernkartei(this);
			nl.setVisible(true);
			
			if(!nl.isSaved()){
				System.exit(0);
			}else{
				setNum(VokabeltrainerDB.getLernkarteien().size() - 1);
			}
				
			nl.dispose();
		}
		
		vokabeltitel = new JLabel("Vokabeltrainer");
		vokabeltitel.setHorizontalAlignment(SwingConstants.CENTER);
		vokabeltitel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		vokabeltitel.setBounds(20, 11, 545, 30);
		contentPane.add(vokabeltitel);

		chckbxNurFlligeFcher = new JCheckBox("Nur f\u00E4llige F\u00E4cher");
		chckbxNurFlligeFcher.setHorizontalAlignment(SwingConstants.RIGHT);
		chckbxNurFlligeFcher.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxNurFlligeFcher.setBounds(324, 43, 153, 25);
		contentPane.add(chckbxNurFlligeFcher);
		
		JButton start = new JButton("Start");
		start.setBounds(485, 44, 89, 23);
		start.setFocusPainted(false);
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] nummernFaecher = null;
				
	      if(!chckbxNurFlligeFcher.isSelected()){
	      	int nummerFachStart = -1;
	      	
	      	if(faecherListe.getSelectedRow() != -1){
		        switch(JOptionPane.showConfirmDialog(VokabeltrainerGUI.this, "Nur ab ausgewähltem Fach lernen?", "Frage", JOptionPane.YES_NO_CANCEL_OPTION)){
		            case JOptionPane.NO_OPTION:{
		                nummerFachStart = 0;
		                break;
		            }
		            case JOptionPane.YES_OPTION:{
		                nummerFachStart = faecherListe.getSelectedRow();
		                break;
		            }
		            default:{
		            	nummerFachStart = -1;
		            }
		        }
		      }else{
	          nummerFachStart = 0;
		      }
	      	
	      	if(nummerFachStart >= 0){
	      		List<Fach> faecher = VokabeltrainerDB.getFaecher(l.getNummer());
	      		
	      		int n = 0;
	      		for(int i = 0; i < faecher.size() - nummerFachStart; i++){
	      			if(VokabeltrainerDB.getKarten(faecher.get(i + nummerFachStart).getNummer()).size() > 0)
	      				n++;
	      		}
	      		
	      		if(n == 0){
	      			JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Diese Fächer enthalten keine Karten!", "Achtung", JOptionPane.WARNING_MESSAGE);
	      		}else{
	      			nummernFaecher = new int[n];
		      		
		      		int j = 0;
		      		for(int i = 0; i < faecher.size() - nummerFachStart; i++){
		      			if(VokabeltrainerDB.getKarten(faecher.get(i + nummerFachStart).getNummer()).size() > 0){
		      				nummernFaecher[j] = faecher.get(i + nummerFachStart).getNummer();
		      				j++;
		      			}
		      		}
	      		}
	      	}
	      }else{
	      	List<Fach> abgelaufene = VokabeltrainerDB.getFaecherErinnerung(l.getNummer());
	      	
	      	int n = 0;
	      	for(int i = 0; i < abgelaufene.size(); i++){
	      		if(VokabeltrainerDB.getKarten(abgelaufene.get(i).getNummer()).size() > 0)
	      			n++;
	      	}
	      	
	      	if(n == 0){
      			JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Diese Fächer enthalten keine Karten!", "Achtung", JOptionPane.WARNING_MESSAGE);
      		}else{
      			nummernFaecher = new int[n];
  	      	
  	      	int j = 0;
  	      	for(int i = 0; i < abgelaufene.size(); i++){
  	      		if(VokabeltrainerDB.getKarten(abgelaufene.get(i).getNummer()).size() > 0){
  	      			nummernFaecher[j] = abgelaufene.get(i).getNummer();
  		      		j++;
  	      		}
  	      	}
      		}
	      }
	      
	      if(nummernFaecher != null){
          new LernenGUI(VokabeltrainerGUI.this, l.getNummer(), nummernFaecher);
          updateView();
	      }
			}
		});
		contentPane.add(start);
		
		JLabel lernkartei = new JLabel("Lernkartei:");
		lernkartei.setBounds(10, 44, 65, 23);
		contentPane.add(lernkartei);

		JButton optionen = new JButton("Optionen");
		optionen.setBounds(103, 378, 100, 23);
		optionen.setFocusPainted(false);
		optionen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(optionen);

		JButton loeschen = new JButton("L\u00F6schen");
		loeschen.setBounds(103, 44, 89, 23);
		loeschen.setFocusPainted(false);
		loeschen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int loeschenAnswer = JOptionPane.showConfirmDialog(VokabeltrainerGUI.this,
						"Sie sind dabei diese Lernkartei samt ihren Fächern und Karten zu löschen. Wollen Sie diese Lernkartei wirklich löschen?",
						"Achtung", JOptionPane.YES_NO_OPTION);
				if (loeschenAnswer == JOptionPane.YES_OPTION) {
					int anzL = VokabeltrainerDB.getLernkarteien().size();
			
					if(VokabeltrainerDB.loeschenLernkartei(l.getNummer()) == 0){
						JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Die Lernkartei wurde erfolgreich gelöscht");
						if(anzL == 1){
							if(JOptionPane.showConfirmDialog(VokabeltrainerGUI.this, "Keine Lernkarteien mehr vorhanden! Wollen Sie eine Neue erstellen?", "Warnung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
								NeueLernkartei nl = new NeueLernkartei(VokabeltrainerGUI.this);
								nl.setVisible(true);
								if(nl.isSaved()){
									setNum(0);
								} else {
									System.exit(0);
								}
								
								nl.dispose();
							}else{
								System.exit(0);
							}
						}
						setNum(0);
					}
				}
			}
		});
		contentPane.add(loeschen);

		JButton vor = new JButton(">");
		vor.setBounds(533, 378, 41, 23);
		vor.setFocusPainted(false);
		vor.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(num < VokabeltrainerDB.getLernkarteien().size() - 1){
					setNum(num + 1);
				}else{
					setNum(0);
				}
			}
		});
		contentPane.add(vor);

		JButton zurueck = new JButton("<");
		zurueck.setBounds(485, 378, 41, 23);
		zurueck.setFocusPainted(false);
		zurueck.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(num >= 1){
					setNum(num - 1);
				}else{
					setNum(VokabeltrainerDB.getLernkarteien().size() - 1);
				}
			}
		});
		contentPane.add(zurueck);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 74, 564, 293);
		contentPane.add(scrollPane);

		JButton neu = new JButton("Neu");
		neu.setBounds(407, 378, 65, 23);
		neu.setFocusPainted(false);
		neu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NeuMenue nm = new NeuMenue(VokabeltrainerGUI.this, l.getNummer());
				if(nm.isSaved()){
					if(nm.getSaved() == NeuMenue.LERNKARTEI){
						setNum(VokabeltrainerDB.getLernkarteien().size() - 1);
					}else if(nm.getSaved() == NeuMenue.FACH){
						updateView();
					}
				}
				nm.dispose();
			}
		});
		contentPane.add(neu);

		pos = new JLabel();
		pos.setHorizontalAlignment(SwingConstants.LEFT);
		pos.setBounds(79, 48, 57, 14);
		contentPane.add(pos);
		
		JButton btnWrterAnzeigen = new JButton("Karten anzeigen");
		btnWrterAnzeigen.setBounds(210, 378, 127, 23);
		btnWrterAnzeigen.setFocusPainted(false);
		btnWrterAnzeigen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewKarten vk = new ViewKarten(VokabeltrainerGUI.this, VokabeltrainerDB.getLernkarteien().get(num).getNummer());
				vk.dispose();
			}
		});
		contentPane.add(btnWrterAnzeigen);
		
		setVisible(true);
		
		updateView();
	}
	
	private void setNum(int num){
		if(this.num != num){
			this.num = num;
			updateView();
		}
	}

	private void updateView() {
		l = VokabeltrainerDB.getLernkarteien().get(num);
		this.vokabeltitel.setText(l.getBeschreibung());
		this.pos.setText(String.valueOf(num + 1) + "/" + String.valueOf(VokabeltrainerDB.getLernkarteien().size()));
		
		List<Fach> faecher = VokabeltrainerDB.getFaecher(l.getNummer());
		String[][] faecher_desc = new String[faecher.size()][4];
		
		for(int i = 0; i < faecher.size(); i++){
			if(faecher.get(i) != null){
				String beschreibung = faecher.get(i).getBeschreibung();
				faecher_desc[i][0] = (beschreibung != null) ? beschreibung : "(keine)";
				
				String gelerntAm = faecher.get(i).getGelerntAmEuropaeischString();
				faecher_desc[i][1] = (gelerntAm != null && gelerntAm != "NULL") ? gelerntAm : "Nie";
				
				faecher_desc[i][2] = String.valueOf(faecher.get(i).getErinnerungsIntervall());
				
				if(faecher.get(i).getErinnerungsIntervall() > 0){
					if(faecher.get(i).getGelerntAm() != null){
						faecher_desc[i][3] = (faecher.get(i).getErinnerungFaellig()) ? "Ja" : "Nein";
					}else{
						faecher_desc[i][3] = "Ja";
					}
				}else{
					faecher_desc[i][3] = "Nie";
				}
			}
		}
		
		faecherListe = new JTable();
		faecherListe.setModel(new DefaultTableModel(faecher_desc, columnNames){public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }});
		faecherListe.setFillsViewportHeight(true);
		faecherListe.getTableHeader().setReorderingAllowed(false);
		faecherListe.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent mouseEvent) {
	        JTable table =(JTable) mouseEvent.getSource();
	        Point point = mouseEvent.getPoint();
	        int row = table.rowAtPoint(point);
	        if (mouseEvent.getClickCount() == 2) {
            if(row != -1){
            	ViewFach v = new ViewFach(VokabeltrainerGUI.this, l.getNummer(), row);
            	if(v.isSaved()){
            		updateView();
            	}
            	v.dispose();
            }
	        }
	    }
		});
		scrollPane.setViewportView(faecherListe);
	}
}
