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
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class VokabeltrainerGUI extends JFrame {

	private JPanel contentPane = null;
	private JTable faecherListe= null;
	private int num = 1;
	private JLabel pos = null;
	private JLabel vokabeltitel = null;
	private String[] columnNames = {"Beschreibung", "Zuletzt gelernt", "Fällig"};
	private JScrollPane scrollPane= null;

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
			if(JOptionPane.showConfirmDialog(this, "Sie besitzen noch keine Lernkartei. Wollen Sie eine neue anlegen?", "Willkommen", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
				NeueLernkartei nl = new NeueLernkartei(this);
				nl.setVisible(true);
				
				if(!nl.isSaved()){
					System.exit(0);
				}else{
					updateView();
				}
					
				nl.dispose();
			}else{
				System.exit(0);
			}
		}
		
		vokabeltitel = new JLabel("Vokabeltrainer");
		vokabeltitel.setHorizontalAlignment(SwingConstants.CENTER);
		vokabeltitel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		vokabeltitel.setBounds(10, 11, 555, 30);
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
					JOptionPane.showMessageDialog(VokabeltrainerGUI.this, "Die Lernkartei wurde erfolgreich gelöscht");
					VokabeltrainerDB.loeschenLernkartei(num);
				}
			}
		});
		contentPane.add(loeschen);

		JButton aendern = new JButton("\u00C4ndern");
		aendern.setBounds(219, 378, 89, 23);
		aendern.setFocusPainted(false);
		aendern.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AendernLernkartei aenderFenster = new AendernLernkartei(VokabeltrainerGUI.this, num);
				aenderFenster.setVisible(true);
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
				if(num < VokabeltrainerDB.getLernkarteien().size()){
					num++;
				}else{
					num = 1;
				}
				updateView();
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
				if(num > 1){
					num--;
				}else{
					num = VokabeltrainerDB.getLernkarteien().size();
				}
				updateView();
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
				nl.setVisible(true);
				
				num = 0;
				updateView();
			}
		});
		contentPane.add(neu);

		pos = new JLabel();
		pos.setHorizontalAlignment(SwingConstants.LEFT);
		pos.setBounds(79, 48, 57, 14);
		contentPane.add(pos);
		
		JButton button = new JButton("+");
		button.setBounds(423, 43, 50, 25);
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		contentPane.add(button);
		
		updateView();
	}

	protected void importData() {
		JFileChooser importFileChooser = new JFileChooser();

		importFileChooser.setMultiSelectionEnabled(false);
		importFileChooser.setFileFilter(new FileNameExtensionFilter("Textdateien", "txt"));
		importFileChooser.setAcceptAllFileFilterUsed(false);
		
		if(importFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			String path = importFileChooser.getSelectedFile().getAbsolutePath();
			
			if(path.endsWith(".txt")){
				File file = new File(path);
				
				if(!file.isDirectory() && file.isFile()){
					switch(VokabeltrainerDB.importierenKarten(num, path)){
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
		Lernkartei l = VokabeltrainerDB.getLernkartei(num);
		this.vokabeltitel.setText(l.getBeschreibung());
		this.pos.setText(String.valueOf(num) + "/" + String.valueOf(VokabeltrainerDB.getLernkarteien().size()));
		
		List<Fach> faecher = VokabeltrainerDB.getFaecher(num);
		String[][] faecher_desc = new String[faecher.size()][3];
		
		for(int i = 0; i < faecher.size(); i++){
			faecher_desc[i][0] = faecher.get(i).getBeschreibung();
			faecher_desc[i][1] = faecher.get(i).getGelerntAmEuropaeischString();
			if(faecher.get(i).getGelerntAm() != null){
				faecher_desc[i][2] = (faecher.get(i).getErinnerungFaellig()) ? "x" : "";
			}else{
				faecher_desc[i][2] = "";
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
            	
            }
	        }
	    }
		});
		scrollPane.setViewportView(faecherListe);
	}
}
