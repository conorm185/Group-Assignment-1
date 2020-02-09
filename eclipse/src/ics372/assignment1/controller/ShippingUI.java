package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.Shipment;
import ics372.assignment1.model.Shipment.ShippingMethod;

/**
 * Main Shipping company UI composed of java swing components and listeners used
 * to interact with the model via a singleton instance of the company object.
 * 
 * @author conor murphy (github:conorm185)
 *
 */
public class ShippingUI {
	// Instance Variables
	private Company company;
	private ArrayList<String> warehouseIdList;

	// Swing Panels
	JFrame mainFrame;
	JPanel left_penal;
	JPanel top_panel;

	// Swing Components
	private JComboBox<String> warehouse_selector;
	private JTextField warehouse_id_field;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JButton btnImportJson;
	private JButton btnAddIncomingShipment;
	private JButton btnFreightRecieptToggle;
	private JButton btnExportContent;
	private JButton btnReadWarehouseContent;
	private JLabel lblWarehouseId;
	private JLabel lblAddNewWarehouse;

	/**
	 * public constructor to grab an instance of the company singleton and call the
	 * init() method to generate the ui.
	 */
	public ShippingUI() {
		company = Company.getInstance();
		init();
	}

	/**
	 * initialize all swing components, set the latout, add listeners, and add them
	 * to the main frame.
	 */
	private void init() {
		// Java Swing Components
		mainFrame = new JFrame();
		left_penal = new JPanel();
		top_panel = new JPanel();
		warehouse_selector = new JComboBox<String>();
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		btnImportJson = new JButton("Import JSON");
		btnAddIncomingShipment = new JButton("Add Incoming Shipment");
		btnFreightRecieptToggle = new JButton("Toggle Freight Reciept");
		btnExportContent = new JButton("Export Content to JSON");
		btnReadWarehouseContent = new JButton("Read Warehouse Content");
		lblWarehouseId = new JLabel("Warehouse Id");
		lblAddNewWarehouse = new JLabel("Add New Warehouse:");
		warehouse_id_field = new JTextField();
		warehouse_id_field.setColumns(10);

		// Frame and Panel layouts
		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		left_penal.setLayout(new GridLayout(5, 1, 0, 0));
		top_panel.setLayout(new GridLayout(0, 4, 0, 0));

		// Component Listeners
		btnImportJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listenerHelperImport();
			}
		});

		btnAddIncomingShipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listenerHelperAddShipment();
			}
		});

		btnReadWarehouseContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listenerHelperReadContent();
			}
		});

		btnExportContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listenerHelperExportContent();
			}
		});

		warehouse_id_field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listenerHelperAddWarehouse();
			}
		});

		btnFreightRecieptToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listenerHelperFreightToggle();
			}
		});

		// Add components to frame
		mainFrame.getContentPane().add(left_penal, BorderLayout.WEST);
		mainFrame.getContentPane().add(top_panel, BorderLayout.NORTH);
		mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		top_panel.add(lblWarehouseId);
		top_panel.add(warehouse_selector);
		top_panel.add(lblAddNewWarehouse);
		top_panel.add(warehouse_id_field);
		left_penal.add(btnImportJson);
		left_penal.add(btnAddIncomingShipment);
		left_penal.add(btnFreightRecieptToggle);
		left_penal.add(btnReadWarehouseContent);
		left_penal.add(btnExportContent);

		mainFrame.setSize(900, 500);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Private helper method to centralize listener action logic. Open a file
	 * chooser for the user to select a file to import, then attempt the import.
	 * alert the user of the outcome via log.
	 */
	private void listenerHelperImport() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Shipment Files", "json");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showOpenDialog(mainFrame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			log(String.format("File selected: %s", chooser.getSelectedFile().getName()));
			try {
				company.importShipments(chooser.getSelectedFile());
				log("file imported successfully!");
				comboBoxRefresh();
			} catch (Exception e1) {
				log("file not imported successfully!");
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Private helper method to centralize listener action logic. Open an
	 * AddShipmentUI and log the event.
	 */
	private void listenerHelperAddShipment() {
		if (warehouse_selector.getSelectedItem() != null) {
			log(String.format("adding Shipment to %s", warehouse_selector.getSelectedItem()));
			AddShipmentUI ui = new AddShipmentUI((String) warehouse_selector.getSelectedItem());
		} else {
			log(String.format("No warehouse selected."));
		}
	}

	/**
	 * Private helper method to centralize listener action logic. Grab the toString
	 * of the currently selected warehouse and append it to the textarea.
	 */
	private void listenerHelperReadContent() {
		if (warehouse_selector.getSelectedItem() != null) {
			textArea.append(company.readWarehouseContent((String) warehouse_selector.getSelectedItem()));
		} else {
			log("No warehouse selected.");
		}
	}

	/**
	 * Private helper method to centralize listener action logic. Attempt to Export
	 * the currently selected warehouse to a .json file. Alert the use via log
	 * whether the operation succeeds or fails.
	 */
	private void listenerHelperExportContent() {
		try {
			company.exportContentToJSON((String) warehouse_selector.getSelectedItem());
			log(String.format("warehouse: %s exported to .json file", warehouse_selector.getSelectedItem()));
		} catch (IOException e1) {
			log(String.format("warehouse: %s failed to export!", warehouse_selector.getSelectedItem()));
			e1.printStackTrace();
		}
	}

	/**
	 * Private helper method to centralize listener action logic. Add a warehouse to
	 * the company with a warehouse_id taken via text field. Alert the user if the
	 * action succeeds, or fails due to an id conflict with an existing warehouse.
	 * Clear text field.
	 */
	private void listenerHelperAddWarehouse() {
		if (company.addWarehouse(warehouse_id_field.getText())) {
			log(String.format("warehouse: %s added to company", warehouse_id_field.getText()));
			comboBoxRefresh();
		} else {
			log(String.format("warehouse: %s already in list", warehouse_id_field.getText()));
		}
		warehouse_id_field.setText("");
	}

	/**
	 * Private helper method to centralize listener action logic. Toggle the
	 * currently selected warehouses's freight status and alert the user via log.
	 */
	private void listenerHelperFreightToggle() {
		if (warehouse_selector.getSelectedItem() != null) {
			company.toggleFreightReciept((String) warehouse_selector.getSelectedItem());
			log(String.format("warehouse: %s freight status set to %b.", warehouse_id_field.getText(),
					company.getFreightReceiptStatus((String) warehouse_selector.getSelectedItem())));
		} else {
			log("No warehouse selected.");
		}
	}

	/**
	 * Private helper method to refresh the warehouse_selector whenever new
	 * warehouses are added, either manually, or via a file import.
	 */
	private void comboBoxRefresh() {
		warehouseIdList = company.getWarehouseIds();
		warehouse_selector.removeAllItems();
		for (String str : warehouseIdList) {
			warehouse_selector.addItem(str);
		}
	}

	/**
	 * private logging method to format and print user actions to the UI's text area
	 * and alert the user of whether operations were successful or not.
	 * 
	 * @param entry the action being logged and printed along with a time stamp.
	 */
	private void log(String entry) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String log_entry = String.format("\n[%s]\t[%s]", formatter.format(now), entry);
		textArea.append(log_entry);
	}

	/**
	 * Private UI Class to be used whenever the main UI needs to add a shipment to a
	 * given warehouse.
	 * 
	 * @author conor
	 *
	 */
	private class AddShipmentUI {

		// Swing components
		private JFrame mainFrame;
		private JPanel panel;
		private JLabel lblWarehouseId;
		private JLabel lblWarehouseId2;
		private JLabel lblShipmentId;
		private JTextField shipment_id_field;
		private JLabel lblShipmentMethod;
		private JComboBox<ShippingMethod> comboBox;
		private JLabel lblWeightlbs;
		private JTextField weight_field;

		/**
		 * Main Constructor of the AddShipmentUI. Initialize all swing panels and
		 * components, set layout, add listeners, and add components to the frames.
		 * 
		 * @param warehouse_id The warehouse id selected by the warehouse_selector at
		 *                     the time the UI was generated.
		 */
		public AddShipmentUI(String warehouse_id) {
			// Component Declarations
			mainFrame = new JFrame();
			panel = new JPanel();

			lblWarehouseId = new JLabel("Warehouse ID");
			lblWarehouseId2 = new JLabel(warehouse_id);

			lblShipmentId = new JLabel("Shipment ID");
			shipment_id_field = new JTextField();

			lblShipmentMethod = new JLabel("Shipment Method");
			comboBox = new JComboBox<ShippingMethod>();

			lblWeightlbs = new JLabel("Weight (lbs)");
			weight_field = new JTextField();

			JButton btnSubmit = new JButton("Submit");
			JButton btnCancel = new JButton("Cancel");

			// Component Attributes
			shipment_id_field.setColumns(10);
			weight_field.setColumns(10);
			comboBox.addItem(ShippingMethod.air);
			comboBox.addItem(ShippingMethod.rail);
			comboBox.addItem(ShippingMethod.ship);
			comboBox.addItem(ShippingMethod.truck);

			// Action Listeners
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addShipment(warehouse_id);
				}
			});

			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log("shipment canceled");
					mainFrame.dispose();
				}
			});

			// Panel Layouts
			mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(5, 2, 0, 0));

			// Add components
			panel.add(lblWarehouseId);
			panel.add(lblWarehouseId2);
			panel.add(lblShipmentId);
			panel.add(shipment_id_field);
			panel.add(lblShipmentMethod);
			panel.add(comboBox);
			panel.add(lblWeightlbs);
			panel.add(weight_field);
			panel.add(btnSubmit);
			panel.add(btnCancel);

			mainFrame.setSize(900, 500);
			mainFrame.setVisible(true);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		}

		/**
		 * Method to add a shipment to the corresponding warehouse. weight and
		 * shipment_id are assigned by the user via text fields. Warehouse ID is passed
		 * from previous UI depending on which warehouse ID is in the
		 * warehouse_selector. REceipt date is generated by the current time of the
		 * system. Log the outcome of the attempted add, and terminate the AddShipmentUI
		 * window after processing.
		 * 
		 * @param warehouse_id The id of the warehouse a shipment is being added to.
		 */
		private void addShipment(String warehouse_id) {
			try {
				double weight = Double.parseDouble(weight_field.getText());
				ShippingMethod method = (ShippingMethod) comboBox.getSelectedItem();
				String shipment_id = shipment_id_field.getText();
				Shipment thisShipment = new Shipment(warehouse_id, method, shipment_id, weight,
						System.currentTimeMillis());
				if (company.addIncomingShipment(thisShipment)) {
					log(String.format("shipment: %s added to warehouse: %s", shipment_id, warehouse_id));
				} else {
					log(String.format("shipment: %s denied reciept at warehouse: %s", shipment_id, warehouse_id));
				}
			} catch (NumberFormatException e1) {
				log(String.format("shipment denied invalid input."));
			} finally {
				mainFrame.dispose();
			}
		}
	}
}
