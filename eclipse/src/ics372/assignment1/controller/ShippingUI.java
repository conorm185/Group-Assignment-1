package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import ics372.assignment1.model.Company;

public class ShippingUI {
	private Company company;
	private String[] warehouseIdList;

	public ShippingUI() {
		// company = Company.getInstance();

		JFrame mainFrame = new JFrame();
		mainFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		mainFrame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(5, 1, 0, 0));

		JTextArea textArea = new JTextArea();
		panel.add(textArea, BorderLayout.CENTER);

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
		btnImportJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Shipment Files", "json");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textArea.append("File selected: " + chooser.getSelectedFile().getName());
					// Company.importShipments();
				}
			}
		});
		panel_2.add(btnImportJson);

		mainFrame.setSize(900, 500);
		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

	}
}
