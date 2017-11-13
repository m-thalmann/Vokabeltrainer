package net.tfobz.vokabeltrainer.model;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NeueKarte extends JDialog
{
	private int lnummer = 0;
	private boolean saved = false;
	
	public NeueKarte(JFrame owner, int nummerLernkartei) {
		super(owner, "Vokabeltrainer: Karte hinzufügen");
		getContentPane().setLayout(null);
		setSize(439, 242);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(owner);
		
		this.lnummer = nummerLernkartei;
	}
	
	public boolean isSaved(){
		return this.saved;
	}
}
