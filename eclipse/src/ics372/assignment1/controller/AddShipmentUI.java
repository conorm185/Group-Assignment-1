package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.Shipment;
import ics372.assignment1.model.Shipment.ShippingMethod;

/**
 * 
 * @author conor
 *
 */
public class AddShipmentUI {
	private JTextField shipment_id_field;
	private JTextField weight_field;
	JFrame mainFrame;
	JComboBox<ShippingMethod> comboBox;
	Company company;

	/**
	 * 
	 * @param company
	 * @param warehouse_id
	 */
	public AddShipmentUI(String warehouse_id) {
		company = Company.getInstance();
		// Component Declarations
		mainFrame = new JFrame();
		JPanel panel = new JPanel();

		JLabel lblWarehouseId = new JLabel("Warehouse ID");
		JLabel lblWarehouseId2 = new JLabel(warehouse_id);

		JLabel lblShipmentId = new JLabel("Shipment ID");
		shipment_id_field = new JTextField();

		JLabel lblShipmentMethod = new JLabel("Shipment Method");
		comboBox = new JComboBox();

		JLabel lblWeightlbs = new JLabel("Weight (lbs)");
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
				mainFrame.dispose();
			}
		});

		// Panel Layouts
		mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(5, 2, 0, 0));

		panel.add(lblWarehouseId);
		panel.add(lblWarehouseId2);
		// panel.add(warehouse_id_field);
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

	private void addShipment(String warehouse_id) {
		try {

			double weight = Double.parseDouble(weight_field.getText());
			ShippingMethod method = (ShippingMethod) comboBox.getSelectedItem();
			String shipment_id = shipment_id_field.getText();
			Shipment thisShipment = new Shipment(warehouse_id, method, shipment_id, weight, LocalDateTime.now());
			company.addIncomingShipment(thisShipment);
			mainFrame.dispose();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
