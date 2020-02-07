package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ics372.assignment1.model.Company;

/**
 * Main Shipping company UI composed of java swing components and listeners.
 * 
 * @author conor murphy (github:conorm185)
 *
 */
public class ShippingUI {
	private Company company;
	private ArrayList<String> warehouseIdList;
	JComboBox<String> warehouse_selector;
	private JTextField textField;
	private JTextField warehouse_id_field;

	public ShippingUI() {
		company = Company.getInstance();

		// Java Swing Components
		JFrame mainFrame = new JFrame();
		JPanel panel = new JPanel();
		JPanel left_penal = new JPanel();
		JPanel top_panel = new JPanel();
		warehouse_selector = new JComboBox();
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		JButton btnImportJson = new JButton("Import JSON");
		JButton btnAddIncomingShipment = new JButton("Add Incoming Shipment");
		JToggleButton btnFreightRecieptToggle = new JToggleButton("Toggle Freight Reciept");
		JButton btnExportContent = new JButton("Export Content to JSON");
		JButton btnReadWarehouseContent = new JButton("Read Warehouse Content");
		textField = new JTextField();
		JLabel lblWarehouseId = new JLabel("Warehouse Id");
		JLabel lblAddNewWarehouse = new JLabel("Add New Warehouse:");
		warehouse_id_field = new JTextField();
		textField.setColumns(10);
		warehouse_id_field.setColumns(10);

		// Frame and Panel layouts
		mainFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		panel.setLayout(new BorderLayout(0, 0));
		left_penal.setLayout(new GridLayout(5, 1, 0, 0));
		top_panel.setLayout(new GridLayout(0, 4, 0, 0));

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
						company.importShipments(chooser.getSelectedFile());
						comboBoxRefresh();
					} catch (JsonSyntaxException e1) {
						textArea.append("File not imported successfully\n");
						e1.printStackTrace();
					} catch (JsonIOException e1) {
						textArea.append("File not imported successfully\n");
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						textArea.append("File not imported successfully\n");
						e1.printStackTrace();
					}
				}
			}
		});

		btnAddIncomingShipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.append(String.format("adding Shipment to %s\n", warehouse_selector.getSelectedItem()));
				AddShipmentUI ui = new AddShipmentUI((String) warehouse_selector.getSelectedItem());
			}
		});

		btnFreightRecieptToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				company.toggleFreightReciept((String) warehouse_selector.getSelectedItem());
			}
		});

		btnReadWarehouseContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.append(company.readWarehouseContent((String) warehouse_selector.getSelectedItem()));
			}
		});

		btnExportContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				company.exportContentToJSON((String) warehouse_selector.getSelectedItem());
			}
		});

		warehouse_id_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				company.addWarehouse(warehouse_id_field.getText());
				comboBoxRefresh();
			}
		});

		mainFrame.getContentPane().add(panel);
		panel.add(left_penal, BorderLayout.WEST);
		panel.add(top_panel, BorderLayout.NORTH);
		top_panel.add(lblWarehouseId);
		top_panel.add(warehouse_selector);
		top_panel.add(lblAddNewWarehouse);
		top_panel.add(warehouse_id_field);
		panel.add(scrollPane, BorderLayout.CENTER);
		left_penal.add(btnImportJson);
		left_penal.add(btnAddIncomingShipment);
		left_penal.add(btnFreightRecieptToggle);
		left_penal.add(btnReadWarehouseContent);
		left_penal.add(btnExportContent);
		panel.add(textField, BorderLayout.SOUTH);

		mainFrame.setSize(900, 500);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void listenerHelperImport() {

	}

	private void listenerHelperAddShipment() {

	}

	private void listenerHelperFreightToggle() {

	}

	private void listenerHelperReadContent() {

	}

	private void listenerHelperExportContent() {

	}

	private void comboBoxRefresh() {
		warehouseIdList = company.getWarehouseIds();
		warehouse_selector.removeAllItems();
		for (String str : warehouseIdList) {
			warehouse_selector.addItem(str);
		}
	}
}
