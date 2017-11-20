package net.tfobz.vokabeltrainer.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.tfobz.vokabeltrainer.model.VokabeltrainerDB;

/**
 *
 * Diese Klasse stellt ein Menü dar,
 * in welchem man:
 * - Eine neue Lernkartei erstellen
 * - Eine Lernkartei bearbeiten
 * - Daten Importieren
 * - Daten Exportieren
 * 
 * kann,
 *
 */

public class OptionMenue extends JDialog
{
	//Variablen, welche die Unterscheidung des Dialogausganges erleichtern
	public static final int CHANGE = 0;
	public static final int DELETE = 1;
	public static final int IMPORT = 2;
	public static final int EXPORT = 3;
	
	private int saved_num = -1;
	private int lnummer = 0;
	
	private JFrame ownerFrame = null;

	public OptionMenue(JFrame owner, int nummerLernkartei) {
		setTitle("Optionen");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(163, 235);
		setLocationRelativeTo(owner);
		setModal(true);
		getContentPane().setLayout(null);
		setResizable(false);
		
		this.lnummer = nummerLernkartei;
		this.ownerFrame = owner;
		
		JLabel lblOptionen = new JLabel("Optionen");
		lblOptionen.setHorizontalAlignment(SwingConstants.CENTER);
		lblOptionen.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblOptionen.setBounds(22, 13, 110, 25);
		getContentPane().add(lblOptionen);
		
		//Importieren
		JButton importierenKnopf = new JButton("Importieren");
		importierenKnopf.setBounds(22, 123, 110, 23);
		importierenKnopf.setFocusPainted(false);
		importierenKnopf.setMnemonic(KeyEvent.VK_I);
		importierenKnopf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				importData();
			}
		});
		getContentPane().add(importierenKnopf);
		
		//Lernkartei ändern
		JButton aenderKnopf = new JButton("\u00C4ndern");
		aenderKnopf.setBounds(22, 51, 110, 23);
		aenderKnopf.setFocusPainted(false);
		aenderKnopf.setMnemonic(KeyEvent.VK_N);
		aenderKnopf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AendernLernkartei aenderFenster = new AendernLernkartei(ownerFrame, lnummer);
				if(aenderFenster.isSaved()){
					saved_num = OptionMenue.CHANGE;
				}
				aenderFenster.dispose();
				
				if(saved_num == OptionMenue.CHANGE){
					setVisible(false);
				}
			}
		});
		getContentPane().add(aenderKnopf);
		
		//Lernkartei ändern
		JButton loeschenKnopf = new JButton("L\u00F6schen");
		loeschenKnopf.setBounds(22, 87, 110, 23);
		loeschenKnopf.setFocusPainted(false);
		loeschenKnopf.setMnemonic(KeyEvent.VK_L);
		loeschenKnopf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int loeschenAnswer = JOptionPane.showConfirmDialog(ownerFrame,
						"Sie sind dabei diese Lernkartei samt ihren Fächern und Karten zu löschen. Wollen Sie diese Lernkartei wirklich löschen?",
						"Achtung", JOptionPane.YES_NO_OPTION);
				if (loeschenAnswer == JOptionPane.YES_OPTION) {
					int anzL = VokabeltrainerDB.getLernkarteien().size();
			
					if(VokabeltrainerDB.loeschenLernkartei(lnummer) == 0){
						JOptionPane.showMessageDialog(ownerFrame, "Die Lernkartei wurde erfolgreich gelöscht");
						if(anzL == 1){
							if(JOptionPane.showConfirmDialog(ownerFrame, "Keine Lernkarteien mehr vorhanden! Wollen Sie eine Neue erstellen?", "Warnung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
								NeueLernkartei nl = new NeueLernkartei(ownerFrame);
								nl.setVisible(true);
								if(!nl.isSaved()){
									System.exit(0);
								}
								nl.dispose();
							}else{
								System.exit(0);
							}
						}
					}
					
					saved_num = OptionMenue.DELETE;
					setVisible(false);
				}
			}
		});
		getContentPane().add(loeschenKnopf);
		
		//Exportieren
		JButton exportierenKnopf = new JButton("Exportieren");
		exportierenKnopf.setBounds(22, 159, 110, 23);
		exportierenKnopf.setFocusPainted(false);
		exportierenKnopf.setMnemonic(KeyEvent.VK_E);
		exportierenKnopf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int mitFaecher = 0; // 0 Abbruch, 1 mit, -1 ohne
				switch(JOptionPane.showConfirmDialog(ownerFrame, "Wollen Sie nur die Karten, ohne Fächer exportieren?", "Vokabeltrainer", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)){
					case JOptionPane.YES_OPTION:{
						mitFaecher = -1;
						break;
					}
					case JOptionPane.NO_OPTION:{
						mitFaecher = 1;
						break;
					}
				}
				
				if(mitFaecher == 1 || mitFaecher == -1){
					String path = getExportierenPath();
					
					if(path != null){
						if(VokabeltrainerDB.exportierenKarten(lnummer, path, mitFaecher == 1) == 0){
							JOptionPane.showMessageDialog(ownerFrame, "Erfolgreich exportiert", "Information", JOptionPane.INFORMATION_MESSAGE);
							saved_num = OptionMenue.EXPORT;
							setVisible(false);
							return;
						}else{
							JOptionPane.showMessageDialog(ownerFrame, "Fehler beim Exportieren!", "Fehler", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
			}
		});
		getContentPane().add(exportierenKnopf);
		
		setVisible(true);
	}
	
	/**
	 * Diese Methode öffnet einen JFileChooser, in welchem
	 * man eine Textdatei (*.txt) auswählen kann, aus welcher
	 * man Daten importieren kann
	 */
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
					switch(VokabeltrainerDB.importierenKarten(lnummer, path)){
						case -1:{
							JOptionPane.showMessageDialog(this, "Importfehler ist aufgetreten!", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						case -2:{
							JOptionPane.showMessageDialog(this, "Importfehler ist aufgetreten! Die Datei existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						case -3:{
							JOptionPane.showMessageDialog(this, "Importfehler ist aufgetreten! Die Lernkartei mit der Nummer " + String.valueOf(lnummer) + " existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
							break;
						}
						default:{
							JOptionPane.showMessageDialog(this, "Daten wurden erfolgreich importiert!", "Ok", JOptionPane.INFORMATION_MESSAGE);
							this.saved_num = OptionMenue.IMPORT;
							setVisible(false);
							return;
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
	
	/**
	 * Diese Methode öffnet einen JFileChooser, in welchem
	 * man eine Textdatei (*.txt) auswählen kann, in welche man
	 * exportieren will.
	 * @return den Pfad zum Speicherort oder null, falls keine Datei
	 *         gewählt wurde.
	 */
	private String getExportierenPath(){
		JFileChooser fileChooser = new JFileChooser();
		String path = null;
		
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Textdateien", "txt"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
			path = fileChooser.getSelectedFile().getAbsolutePath();
			
			if(!path.endsWith(".txt")){
				path += ".txt";
			}
			
			File file = new File(path);
			
			if(file.exists() && !file.isDirectory()){
				int ow = JOptionPane.showConfirmDialog(this, "Diese Datei existiert bereits. Überschreiben?", "Achtung", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				switch(ow){
					case JOptionPane.YES_OPTION:{
						file.delete();
						break;
					}
					case JOptionPane.NO_OPTION:{
						path = getExportierenPath();
						break;
					}
					default:{
						path = null;
					}
				}
			}
		}
		
		return path;
	}
	
	public boolean isSaved(){
		return this.saved_num != -1;
	}
	
	public int getSavedNum(){
		return this.saved_num;
	}
}