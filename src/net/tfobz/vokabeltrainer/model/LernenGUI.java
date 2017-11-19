package net.tfobz.vokabeltrainer.model;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class LernenGUI extends JDialog
{
	private static final int WRONG = -1;
	private static final int NOTDONE = 0;
	private final int TIMEOUT = 1000;
	
	private int lnummer = 0;
	private int[] numFaecher = null;
	private int numFachCurrent = -1;
	private ArrayList<Integer> numKarten = null;
	private int numKarteCurrent = 0;
	private ArrayList<Integer> kartenStatus = null;
	private ArrayList<Integer> kartenDoneCorrect = new ArrayList<>();
			
	private JTextField textField;
	private JTextField textField_1;
	private JFrame ownerFrame = null;
	private JButton btnWeiter = null;
	private JButton btnBeenden = null;
	private JLabel labelCorrect;
	
	public LernenGUI(JFrame owner, int nummerLernkartei, int[] nummernFaecher){
		super(owner, "Vokabeltrainer: Lernen");
		getContentPane().setLayout(null);
		setSize(381, 199);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		
		this.ownerFrame = owner;
		this.lnummer = nummerLernkartei;
		this.numFaecher = nummernFaecher;
		
		JLabel lblLernen = new JLabel("Lernen");
		lblLernen.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLernen.setBounds(10, 11, 200, 25);
		this.getContentPane().add(lblLernen);
		
		JLabel lblWort = new JLabel(VokabeltrainerDB.getLernkartei(nummerLernkartei).getWortEinsBeschreibung() + ":");
		lblWort.setBounds(10, 49, 102, 16);
		getContentPane().add(lblWort);
		
		JLabel lblWort_1 = new JLabel(VokabeltrainerDB.getLernkartei(nummerLernkartei).getWortZweiBeschreibung() + ":");
		lblWort_1.setBounds(10, 94, 102, 16);
		getContentPane().add(lblWort_1);
		
		textField = new JTextField();
		textField.setBounds(124, 49, 231, 22);
		textField.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					btnWeiter.doClick();
				}
			}
		});
		getContentPane().add(textField);
		
		textField_1 = new JTextField();
		textField_1.setBounds(124, 91, 231, 22);
		textField_1.addKeyListener(new KeyAdapter()
		{

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					btnWeiter.doClick();
				}
			}
		});
		getContentPane().add(textField_1);
		
		btnBeenden = new JButton("Beenden");
		btnBeenden.setBounds(10, 126, 97, 25);
		btnBeenden.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnBeenden.setFocusPainted(false);
		getContentPane().add(btnBeenden);
		
		btnWeiter = new JButton("Weiter");
		btnWeiter.setBounds(258, 126, 97, 25);
		btnWeiter.setFocusPainted(false);
		btnWeiter.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(VokabeltrainerDB.getKarte(numKarten.get(numKarteCurrent)).getRichtig((textField.isEnabled()) ? textField.getText() : textField_1.getText())){
					if(kartenStatus.get(numKarteCurrent) == LernenGUI.NOTDONE){
						VokabeltrainerDB.setKarteRichtig(VokabeltrainerDB.getKarte(numKarten.get(numKarteCurrent)));
					}
					kartenStatus.remove(numKarteCurrent);
					numKarten.remove(numKarteCurrent);
					numKarteCurrent--;
					
					labelCorrect.setText("Richtig");
					textField.setEnabled(false);
					textField_1.setEnabled(false);
					btnBeenden.setEnabled(false);
					btnWeiter.setEnabled(false);
					
					Timer t = new Timer(TIMEOUT, new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) {
							labelCorrect.setText("");
							updateView();
							btnBeenden.setEnabled(true);
							btnWeiter.setEnabled(true);
						}
					});
					t.setRepeats(false);
					t.start();
				}else{
					if(kartenStatus.get(numKarteCurrent) == LernenGUI.NOTDONE){
						VokabeltrainerDB.setKarteFalsch(VokabeltrainerDB.getKarte(numKarten.get(numKarteCurrent)));
					}
					kartenStatus.set(numKarteCurrent, LernenGUI.WRONG);
					
					labelCorrect.setText("Falsch");
					textField.setEnabled(false);
					textField_1.setEnabled(false);
					btnBeenden.setEnabled(false);
					btnWeiter.setEnabled(false);
					
					Timer t = new Timer(TIMEOUT, new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) {
							labelCorrect.setText("");
							updateView();
							btnBeenden.setEnabled(true);
							btnWeiter.setEnabled(true);
						}
					});
					t.setRepeats(false);
					t.start();
				}
			}
		});
		getContentPane().add(btnWeiter);
		
		labelCorrect = new JLabel("");
		labelCorrect.setHorizontalAlignment(SwingConstants.CENTER);
		labelCorrect.setBounds(124, 130, 122, 16);
		getContentPane().add(labelCorrect);
		
		updateView();
		
		setVisible(true);
	}

	private void updateView() {
		textField.setEnabled(!VokabeltrainerDB.getLernkartei(lnummer).getRichtung());
		textField_1.setEnabled(VokabeltrainerDB.getLernkartei(lnummer).getRichtung());
		
		if(textField.isEnabled()){
			textField.requestFocus();
		}else{
			textField_1.requestFocus();
		}
		
		if(this.numKarten == null){
			if(this.numFachCurrent +1 == this.numFaecher.length){
				JOptionPane.showMessageDialog(ownerFrame, "Sie haben das Training abgeschlossen!", "Ende", JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
				return;
			}
			
			//Init
			this.kartenStatus = null;
			
			this.numFachCurrent++;
			int length = VokabeltrainerDB.getKarten(this.numFaecher[this.numFachCurrent]).size();
			
			this.numKarten = new ArrayList<>();
			this.kartenStatus = new ArrayList<>();
			this.numKarteCurrent = 0;
			
			for(int i = 0; i < length; i++){
				int num = -1;
				do{
					num = VokabeltrainerDB.getZufaelligeKarte(this.lnummer, this.numFaecher[numFachCurrent]).getNummer();
				}while(numKarten.contains(num) || kartenDoneCorrect.contains(num));
				
				this.numKarten.add(num);
				this.kartenStatus.add(LernenGUI.NOTDONE);
			}
			updateView();
		}else{
			if(this.numKarten.size() == 0){
				this.numKarten = null;
				updateView();
			}else{
				if(this.numKarteCurrent +1 == this.numKarten.size()){
					this.numKarteCurrent = 0;
				}else{
					this.numKarteCurrent++;
				}
				
				if(!textField.isEnabled()){
					textField.setText(VokabeltrainerDB.getKarte(this.numKarten.get(numKarteCurrent)).getWortEins());
					textField_1.setText("");
				}else{
					textField.setText("");
					textField_1.setText(VokabeltrainerDB.getKarte(this.numKarten.get(numKarteCurrent)).getWortZwei());
				}
			}
		}
	}
}
