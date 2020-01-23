package ics372.assignment1.controller;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ShippingUI {

	public ShippingUI() {
		JFrame mainFrame = new JFrame();
		mainFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JTextArea textArea = new JTextArea();
		mainFrame.getContentPane().add(textArea);
	}

}
