package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import ics372.assignment1.model.Company;

/**
 * Main Shipping company UI composed of java swing components and listeners.
 * 
 * @author conor murphy (github:conorm185)
 *
 */
public class ShippingUI {
	private Company company;
	private String[] warehouseIdList;
	private String currentWarehouse;
	private JTextField textField;

	public ShippingUI() {
		// company = Company.getInstance();

		// Java Swing Components
		JFrame mainFrame = new JFrame();
		JPanel panel = new JPanel();
		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();
		JComboBox<String> comboBox = new JComboBox();
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		JButton btnImportJson = new JButton("Import JSON");
		JButton btnAddIncomingShipment = new JButton("Add Incoming Shipment");
		JToggleButton btnFreightRecieptToggle = new JToggleButton("Toggle Freight Reciept");
		JButton btnExportContent = new JButton("Export Content to JSON");
		JButton btnReadWarehouseContent = new JButton("Read Warehouse Content");
		textField = new JTextField();
		textField.setColumns(10);

		// Frame and Panel layouts
		mainFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		panel.setLayout(new BorderLayout(0, 0));
		panel_1.setLayout(new GridLayout(5, 1, 0, 0));
		panel_2.setLayout(new GridLayout(0, 4, 0, 0));

		// Component Listeners
		btnImportJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Shipment Files", "json");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					textArea.append(String.format("File selected: %s\n", chooser.getSelectedFile().getName()));
					try {
						// company.importShipments(chooser.getSelectedFile());
						// warehouseIdList = company.getWarehouseIds();
						comboBox.removeAllItems();
						for (String str : warehouseIdList) {
							comboBox.addItem(str);
						}
					} catch (FileNotFoundException ex) {
						textArea.append("File not imported successfully\n");
						ex.printStackTrace();
					}
				}
			}
		});

		btnAddIncomingShipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.append(String.format("adding Shipment to %s\n", comboBox.getSelectedItem()));
			}
		});

		btnFreightRecieptToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((AbstractButton) e.getSource()).isSelected()) {
					textArea.append("selected\n");
				} else {
					textArea.append("deselected\n");
				}
			}
		});

		btnReadWarehouseContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		btnExportContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// all export files will output to the same location
				// company.exportContentToJSON(currentWarehouse);
			}
		});

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentWarehouse = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
				textArea.append(currentWarehouse);
				// btnFreightRecieptToggle.setSelected(company.getFreightRecieptStatus(currentWarehouse));
			}
		});

		mainFrame.getContentPane().add(panel);
		panel.add(panel_1, BorderLayout.WEST);
		panel.add(panel_2, BorderLayout.NORTH);
		panel_2.add(comboBox);
		panel.add(scrollPane, BorderLayout.CENTER);
		panel_1.add(btnImportJson);
		panel_1.add(btnAddIncomingShipment);
		panel_1.add(btnFreightRecieptToggle);
		panel_1.add(btnReadWarehouseContent);
		panel_1.add(btnExportContent);
		panel.add(textField, BorderLayout.SOUTH);

		mainFrame.setSize(900, 500);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
