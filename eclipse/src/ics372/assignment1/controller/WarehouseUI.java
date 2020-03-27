package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.Shipment;

public class WarehouseUI {
	// Swing components
	private Company company;
	private HashMap<String, Shipment> warehouse_contents;

	private JFrame main_frame_add;
	private JPanel panel;
	private JPanel panel_1;
	private JLabel lblWarehouseId;
	private JLabel lblWarehouseName;
	private JTextField textField;
	private JList list;
	private JScrollPane scrollPane;

	private DefaultListModel<String> listModel;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblShipmentIds;

	public WarehouseUI(String warehouse_id) {
		company = Company.getInstance();
		warehouse_contents = company.readWarehouseContent(warehouse_id);

		// Component Declarations
		main_frame_add = new JFrame();

		main_frame_add.setSize(900, 500);
		main_frame_add.setVisible(true);
		main_frame_add.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame_add.getContentPane().setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		main_frame_add.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		lblWarehouseId = new JLabel(String.format("Warehouse ID: %s", warehouse_id));
		panel_1.add(lblWarehouseId);

		lblWarehouseName = new JLabel("Warehouse Name:");
		panel_1.add(lblWarehouseName);

		textField = new JTextField(company.getWarehouseName(warehouse_id));
		panel_1.add(textField);
		textField.setColumns(10);

		listModel = new DefaultListModel<String>();
		warehouse_contents.forEach((k, v) -> {
			listModel.addElement(k);
		});

		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);

		panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.WEST);

		// Create the list and put it in a scroll pane.
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(null);
		panel_3.setLayout(new GridLayout(1, 1, 0, 0));

		lblShipmentIds = new JLabel("Shipment IDs:");
		panel_3.add(lblShipmentIds);
		list.setVisibleRowCount(5);

		scrollPane = new JScrollPane(list);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_3.add(scrollPane);

		main_frame_add.setSize(900, 500);
		main_frame_add.setVisible(true);
		main_frame_add.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame_add.getContentPane().setLayout(new BorderLayout(0, 0));
	}
}
