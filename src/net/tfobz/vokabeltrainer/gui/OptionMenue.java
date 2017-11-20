package net.tfobz.vokabeltrainer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class OptionMenue extends JDialog
{

	public OptionMenue(JFrame owner) {
		setTitle("Vokabeltrainer: Optionen");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 250);
		setLocationRelativeTo(owner);
		setModal(true);
		getContentPane().setLayout(null);
		
		JButton btnImportieren = new JButton("Importieren");
		btnImportieren.setBounds(10, 178, 89, 23);
		getContentPane().add(btnImportieren);
		
		JButton btnndern = new JButton("\u00C4ndern");
		btnndern.setBounds(216, 178, 89, 23);
		getContentPane().add(btnndern);
		
		JButton btnLschen = new JButton("L\u00F6schen");
		btnLschen.setBounds(315, 178, 89, 23);
		getContentPane().add(btnLschen);
		
		JButton btnExportieren = new JButton("Exportieren");
		btnExportieren.setBounds(109, 178, 89, 23);
		getContentPane().add(btnExportieren);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 414, 21);
		getContentPane().add(menuBar);
		
		JMenu mnOptionen = new JMenu("Optionen");
		menuBar.add(mnOptionen);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mnOptionen.add(mntmImport);
		
		JMenuItem mntmExport = new JMenuItem("Export");
		mnOptionen.add(mntmExport);
		
		JMenuItem mntmndern = new JMenuItem("\u00C4ndern");
		mnOptionen.add(mntmndern);
		
		JMenuItem mntmLschen = new JMenuItem("L\u00F6schen");
		mnOptionen.add(mntmLschen);
		
		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);
	
	}
}
