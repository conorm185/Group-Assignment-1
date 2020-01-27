package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ShippingUI {

	public ShippingUI() {
		JFrame mainFrame = new JFrame();
		mainFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		mainFrame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(5, 1, 0, 0));

		JButton btnAddIncomingShipment = new JButton("Add Incoming Shipment");
		panel_1.add(btnAddIncomingShipment);

		JButton btnEnableFreightReciept = new JButton("Enable Freight Reciept");
		panel_1.add(btnEnableFreightReciept);

		JButton btnEndFreightReciept = new JButton("End Freight Reciept");
		panel_1.add(btnEndFreightReciept);

		JButton btnExportContentTo = new JButton("Export Content to JSON");
		panel_1.add(btnExportContentTo);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new GridLayout(0, 4, 0, 0));

		JComboBox comboBox = new JComboBox();
		panel_2.add(comboBox);

		JButton btnImportJson = new JButton("Import JSON");
		panel_2.add(btnImportJson);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
	}

}
