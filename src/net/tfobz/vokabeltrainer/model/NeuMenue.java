package net.tfobz.vokabeltrainer.model;

import javax.swing.JButton;
import javax.swing.JFrame;

public class NeuMenue extends JFrame
{

	public NeuMenue(JFrame owner, int numLernkartei) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(owner);
		getContentPane().setLayout(null);

		JButton n1 = new JButton("Neue Kartei");
		JButton n2 = new JButton("Neues Fach");
		n1.setBounds(50, 50, 50, 25);
		n2.setBounds(50, 80, 50, 25);
		getContentPane().add(n1);
		getContentPane().add(n2);
		
		setVisible(true);
	}
}
