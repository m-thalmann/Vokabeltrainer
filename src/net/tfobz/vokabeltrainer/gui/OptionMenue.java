package net.tfobz.vokabeltrainer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.tfobz.vokabeltrainer.model.Fach;
import net.tfobz.vokabeltrainer.model.VokabeltrainerDB;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/*
 * TODO UpdateView für importData(), setNum für Löschen,
 * "l" bzw. "num" einfügen, exportieren-FKT, explizit
 * in Konstruktor nomol nochschaugn und Anordnung fa
 * Knöpfe mochn weil is folschr nit ausführn gekennt hon
 */

public class OptionMenue extends JDialog
{

	public OptionMenue(JFrame owner, int l) {
		setTitle("Vokabeltrainer: Optionen");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 250);
		setLocationRelativeTo(owner);
		setModal(true);
		getContentPane().setLayout(null);
		
		JButton importierenKnopf = new JButton("Importieren");
		importierenKnopf.setBounds(10, 178, 89, 23);
		importierenKnopf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				importData();
			}
		});
		getContentPane().add(importierenKnopf);
		
		JButton aenderKnopf = new JButton("\u00C4ndern");
		aenderKnopf.setBounds(216, 178, 89, 23);
		aenderKnopf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AendernLernkartei aenderFenster = new AendernLernkartei(OptionMenue.this, l.getNummer());
				if(aenderFenster.isSaved()){
					updateView();
				}
				aenderFenster.dispose();
			}
		});
		getContentPane().add(aenderKnopf);
		
		JButton loeschenKnopf = new JButton("L\u00F6schen");
		loeschenKnopf.setBounds(315, 178, 89, 23);
		loeschenKnopf.addActionListener(new ActionListener() {

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
						VokabeltrainerGUI.setNum(0);
					}
				}
			}
		});
		getContentPane().add(loeschenKnopf);
		
		JButton exportierenKnopf = new JButton("Exportieren");
		exportierenKnopf.setBounds(109, 178, 89, 23);
		exportierenKnopf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		getContentPane().add(exportierenKnopf);
	}
	
	private void setNum1(int num){
		if(this.num != num){
			this.num = num;
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
							VokabeltrainerGUI.updateView();
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
}