package net.tfobz.vokabeltrainer.model;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class AendernFach extends JDialog
{

	private boolean saved = false;
	
	public AendernFach(JFrame owner, int num) {
		super(owner, "Vokabeltrainer: Fach bearbeiten");
		setLayout(null);
		setSize(500, 500);
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(owner);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		setVisible(true);
	}
	
	public boolean isSaved(){
		return this.saved;
	}

}
