package Main;

import javax.swing.JFrame;

public class Main { 
	
	public static void main(String[] args) {
		 
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Fetch Adventure");
		
		GamePanel gamePanel = new GamePanel();
		// show homepage first; HomePage will switch to gamePanel when Play is clicked
		HomePage home = new HomePage(window, gamePanel);
		window.add(home);
		
		window.pack();  // the pack() causes the window to be sized to fit the preferred size and layouts of iys subcomponents(=GamePanel)
		
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		// game setup and thread are started when the player clicks Play on the HomePage
		
		
		
	}

}