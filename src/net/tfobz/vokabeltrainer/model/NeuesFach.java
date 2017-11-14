package net.tfobz.vokabeltrainer.model;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JButton;

public class NeuesFach extends JDialog
{
	private int lnummer = 0;
	private boolean saved = false;
	
	private JTextField textField;
	private JSpinner spinner;
	
	public NeuesFach(JFrame owner, int numLernkartei) {
		super(owner, "Vokabeltrainer: Fach hinzufügen");
		getContentPane().setLayout(null);
		setSize(439, 242);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(owner);
		
		this.lnummer = numLernkartei;
		
		JLabel lblFachHinzufuegen = new JLabel("Fach hinzufügen");
		lblFachHinzufuegen.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFachHinzufuegen.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblFachHinzufuegen);
		
		JLabel label = new JLabel("Beschreibung: ");
		label.setBounds(10, 60, 200, 20);
		getContentPane().add(label);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(10, 85, 414, 20);
		getContentPane().add(textField);
		
		JLabel lblErinnerungsintervalltage = new JLabel("Erinnerungsintervall (Tage):");
		lblErinnerungsintervalltage.setBounds(10, 119, 200, 16);
		getContentPane().add(lblErinnerungsintervalltage);
		
		spinner = new JSpinner();
		spinner.setBounds(191, 116, 30, 22);
		getContentPane().add(spinner);
		
		JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.setBounds(327, 169, 97, 25);
		btnSpeichern.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Fach f = new Fach(-1, textField.getText(), Integer.parseInt(spinner.getValue().toString()), null);
				if(VokabeltrainerDB.hinzufuegenFach(lnummer, f) == 0){
					saved = true;
					setVisible(false);
				}else{
					JOptionPane.showMessageDialog(NeuesFach.this, "Dieses Fach existiert bereits!", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		getContentPane().add(btnSpeichern);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setBounds(222, 169, 97, 25);
		btnAbbrechen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		getContentPane().add(btnAbbrechen);
		
		if(VokabeltrainerDB.getLernkartei(numLernkartei) == null){
			JOptionPane.showMessageDialog(NeuesFach.this, "Diese Lernkartei existiert nicht!", "Achtung", JOptionPane.WARNING_MESSAGE);
			setVisible(false);
		}else{
			setVisible(true);
		}
	}
	
	public boolean isSaved(){
		return this.saved;
	}
}
