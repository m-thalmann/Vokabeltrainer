package net.tfobz.vokabeltrainer.model;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NeuMenue extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NeuMenue(JFrame owner, int numLernkartei) {
		setTitle("Kartei/Fach erstellen");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 171);
		setLocationRelativeTo(owner);
		getContentPane().setLayout(null);

		JButton n1 = new JButton("Neue Kartei");
		n1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		JButton n2 = new JButton("Neues Fach");
		n2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		n1.setBounds(22, 49, 183, 25);
		n2.setBounds(22, 87, 183, 25);
		getContentPane().add(n1);
		getContentPane().add(n2);
		
		JLabel lblNeu = new JLabel("Neu:");
		lblNeu.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNeu.setBounds(12, 13, 138, 36);
		getContentPane().add(lblNeu);
		
		setVisible(true);
	}
}
