package test;

import javax.swing.*;

import tabler.components.server.ServerView;




public class ServerTest extends JFrame{
	
	public static void main (String[] args){


	
		ServerView window = new ServerView();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(700, 700);
		window.setVisible(true);
		
	}
}