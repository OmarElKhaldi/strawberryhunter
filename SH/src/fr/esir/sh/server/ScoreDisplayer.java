package fr.esir.sh.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ScoreDisplayer extends JFrame{

	JLabel labelWinner= new JLabel();
	JLabel labelScore= new JLabel();
	
	public ScoreDisplayer(int playerId, Color color, int score){
		
		GridLayout layout= new GridLayout(2, 1);
		this.setLayout(layout);
		
		this.labelWinner.setText("And the winner is: \n\t Player "+playerId);
		this.labelWinner.setForeground(color);
		this.labelWinner.setFont((new Font("Serif", Font.PLAIN, 40)));
		this.add(this.labelWinner, 0);
		
		this.labelScore.setText("The player got the best score:"+score);
		this.labelScore.setFont((new Font("Serif", Font.PLAIN, 40)));
		this.add(this.labelScore, 1);
		
		this.setLocation(600,0);
		this.pack();
		this.setVisible(true);
	}
}