package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.Shipment;

public class WarehouseUI {
	// Swing components
	private Company company;
	private HashMap<String, Shipment> warehouse_contents;
	private String warehouse_id;

	private JFrame main_frame_warehouse;
	private JPanel panel;
	private JPanel panel_top;
	private JLabel lblWarehouseId;
	private JLabel lblWarehouseName;
	private JTextField textField;
	private JList list;
	private JScrollPane scrollpane_shipments;

	private DefaultListModel<String> listModel;
	private JPanel panel_center;
	private JPanel panel_left;
	private JLabel lbl_scrollpane;
	private JLabel lbl_shipment_id_1;
	private JLabel lbl_weight_1;
	private JLabel lbl_shipment_id_2;
	private JLabel lbl_weight_2;
	private JLabel lbl_warehouse_id_1;
	private JLabel lbl_warehouse_id_2;
	private JLabel lbl_method_1;
	private JLabel lbl_method_2;

	public WarehouseUI(String warehouse_id) {
		// Logic Objects declarations
		company = Company.getInstance();
		warehouse_contents = company.readWarehouseContent(warehouse_id);
		this.warehouse_id = warehouse_id;

		listModel = new DefaultListModel<String>();
		warehouse_contents.forEach((k, v) -> {
			listModel.addElement(k);
		});

		// Component Declarations
		main_frame_warehouse = new JFrame();
		panel = new JPanel();
		panel_top = new JPanel();
		lblWarehouseId = new JLabel(String.format("Warehouse ID: %s", warehouse_id));
		lblWarehouseName = new JLabel("Warehouse Name:");
		textField = new JTextField(company.getWarehouseName(warehouse_id));
		panel_center = new JPanel();
		lbl_warehouse_id_1 = new JLabel("Warehouse ID:");
		lbl_warehouse_id_2 = new JLabel("warehouse_id");
		lbl_shipment_id_1 = new JLabel("Shipment ID:");
		lbl_shipment_id_2 = new JLabel("id");
		lbl_method_1 = new JLabel("Shipping Method:");
		lbl_method_2 = new JLabel("method");
		lbl_weight_1 = new JLabel("Weight:");
		lbl_scrollpane = new JLabel("Shipment IDs:");
		list = new JList(listModel);
		scrollpane_shipments = new JScrollPane(list);
		lbl_weight_2 = new JLabel("weight");

		panel_left = new JPanel();

		// layouts
		panel.setLayout(new BorderLayout(0, 0));
		panel_center.setLayout(new GridLayout(5, 2, 0, 0));
		panel_left.setLayout(new GridLayout(2, 1, 0, 0));

		// configure components
		textField.setColumns(10);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		viewShipment((String) list.getSelectedValue());
		list.setVisibleRowCount(10);
		scrollpane_shipments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		// scrollpane_shipments.set
		// Add listeners
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				viewShipment((String) list.getSelectedValue());
			}
		});

		// add components
		main_frame_warehouse.getContentPane().add(panel, BorderLayout.CENTER);
		panel.add(panel_top, BorderLayout.NORTH);
		panel.add(panel_left, BorderLayout.WEST);
		panel.add(panel_center, BorderLayout.CENTER);

		panel_top.add(lblWarehouseId);
		panel_top.add(lblWarehouseName);
		panel_top.add(textField);

		panel_center.add(lbl_warehouse_id_1);
		lbl_warehouse_id_1.setBorder(BorderFactory.createLineBorder(Color.black));
		panel_center.add(lbl_warehouse_id_2);
		panel_center.add(lbl_shipment_id_1);
		panel_center.add(lbl_shipment_id_2);
		panel_center.add(lbl_method_1);
		panel_center.add(lbl_method_2);
		panel_center.add(lbl_weight_1);
		panel_center.add(lbl_weight_2);

		panel_left.add(lbl_scrollpane);
		panel_left.add(scrollpane_shipments);

		main_frame_warehouse.setSize(900, 500);
		main_frame_warehouse.setVisible(true);
		main_frame_warehouse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame_warehouse.getContentPane().setLayout(new BorderLayout(0, 0));
	}

	private void viewShipment(String shipment_id) {
		lbl_warehouse_id_2.setText(warehouse_contents.get(shipment_id).getWarehouse_id());
		lbl_shipment_id_2.setText(shipment_id);
		lbl_method_2.setText(warehouse_contents.get(shipment_id).getShipment_method().toString());
		lbl_weight_2.setText(String.format("%.2f lbs", warehouse_contents.get(shipment_id).getWeight()));

	}
}
