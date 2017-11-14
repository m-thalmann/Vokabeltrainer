package net.tfobz.vokabeltrainer.model;

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

public class VokabeltrainerGUI extends JFrame {

	private JPanel contentPane = null;
	private JTable faecherListe= null;
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
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		
		VokabeltrainerDB.erstellenTabellen();
		
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
		vokabeltitel.setBounds(10, 11, 555, 30);
		contentPane.add(vokabeltitel);

		JButton start = new JButton("Start");
		start.setBounds(485, 44, 89, 23);
		start.setFocusPainted(false);
		start.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Not working yet, cunt");
			}
		});
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
				importData();
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

		JButton aendern = new JButton("\u00C4ndern");
		aendern.setBounds(219, 378, 89, 23);
		aendern.setFocusPainted(false);
		aendern.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AendernLernkartei aenderFenster = new AendernLernkartei(VokabeltrainerGUI.this, l.getNummer());
				if(aenderFenster.isSaved()){
					updateView();
				}
				aenderFenster.dispose();
			}
		});
		contentPane.add(aendern);

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
				NeueLernkartei nl = new NeueLernkartei(VokabeltrainerGUI.this);
				if(nl.isSaved()){
					setNum(VokabeltrainerDB.getLernkarteien().size() - 1);
				}
				nl.dispose();
			}
		});
		contentPane.add(neu);

		pos = new JLabel();
		pos.setHorizontalAlignment(SwingConstants.LEFT);
		pos.setBounds(79, 48, 57, 14);
		contentPane.add(pos);
		
		JButton btnNeuesFach = new JButton("Neues Fach");
		btnNeuesFach.setBounds(373, 44, 100, 23);
		btnNeuesFach.setFocusPainted(false);
		btnNeuesFach.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				NeuesFach nf = new NeuesFach(VokabeltrainerGUI.this, l.getNummer());
				if(nf.isSaved()){
					updateView();
				}
				nf.dispose();
			}
		});
		contentPane.add(btnNeuesFach);
		
		JButton btnWrterAnzeigen = new JButton("Karten anzeigen");
		btnWrterAnzeigen.setBounds(234, 43, 127, 25);
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

	private void importData() {
		JFileChooser importFileChooser = new JFileChooser();

		importFileChooser.setMultiSelectionEnabled(false);
		importFileChooser.setFileFilter(new FileNameExtensionFilter("Textdateien", "txt"));
		importFileChooser.setAcceptAllFileFilterUsed(false);
		
		if(importFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			String path = importFileChooser.getSelectedFile().getAbsolutePath();
			
			if(path.endsWith(".txt")){
				File file = new File(path);
				
				if(!file.isDirectory() && file.isFile()){
					switch(VokabeltrainerDB.importierenKarten(l.getNummer(), path)){
						case -1:{
							JOptionPane.showMessageDialog(this, "Importfehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						case -2:{
							JOptionPane.showMessageDialog(this, "Importfehler ist aufgetreten! Die Datei existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						case -3:{
							JOptionPane.showMessageDialog(this, "Importfehler ist aufgetreten! Die Lernkartei mit der Nummer " + String.valueOf(num + 1) + " existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						default:{
							JOptionPane.showMessageDialog(this, "Daten wurden erfolgreich importiert!", "Ok", JOptionPane.INFORMATION_MESSAGE);
							updateView();
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(this, "Die Datei \"" + path + "\" existiert nicht.", "Achtung", JOptionPane.WARNING_MESSAGE);
				}
			}else{
				JOptionPane.showMessageDialog(this, "Die Datei \"" + path + "\" ist keine Textdatei (*.txt)", "Achtung", JOptionPane.WARNING_MESSAGE);
			}
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
