package ics372.assignment1.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.Shipment;

public class WarehouseUI {
	//model objects
	private Company company;
	private HashMap<String, Shipment> warehouse_contents;
	private String warehouse_id;
	
	// Swing components
	//Panels and frames
	private JFrame main_frame_warehouse;
	private JPanel panel;
	private JPanel panel_top;
	private JPanel panel_center;
	private JPanel panel_left;
	
	//top
	private JLabel lbl_warehouse_id;
	private JLabel lbl_warehouse_name;
	private JTextField text_warehouse_name;
	
	//left
	private JList list;
	private JScrollPane scrollpane_shipments;
	private DefaultListModel<String> listModel;
	
	//center
	private JLabel lbl_scrollpane;
	private JLabel lbl_shipment_id_1;
	private JLabel lbl_weight_1;
	private JLabel lbl_shipment_id_2;
	private JLabel lbl_weight_2;
	private JLabel lbl_warehouse_id_1;
	private JLabel lbl_warehouse_id_2;
	private JLabel lbl_method_1;
	private JLabel lbl_method_2;
	private JLabel lbl_date_1;
	private JLabel lbl_date_2;
	private JButton btn_close;

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
		panel_center = new JPanel();
		panel_left = new JPanel();

		lbl_warehouse_id = new JLabel(String.format("Warehouse ID: %s", warehouse_id));
		lbl_warehouse_name = new JLabel("Warehouse Name:");
		text_warehouse_name = new JTextField(company.getWarehouseName(warehouse_id));

		lbl_warehouse_id_1 = new JLabel("Warehouse ID:", SwingConstants.CENTER);
		lbl_warehouse_id_2 = new JLabel("n/a", SwingConstants.CENTER);
		lbl_shipment_id_1 = new JLabel("Shipment ID:", SwingConstants.CENTER);
		lbl_shipment_id_2 = new JLabel("n/a", SwingConstants.CENTER);
		lbl_method_1 = new JLabel("Shipping Method:", SwingConstants.CENTER);
		lbl_method_2 = new JLabel("n/a", SwingConstants.CENTER);
		lbl_weight_1 = new JLabel("Weight:", SwingConstants.CENTER);
		lbl_weight_2 = new JLabel("n/a", SwingConstants.CENTER);
		lbl_date_1 = new JLabel("Reciept Date", SwingConstants.CENTER);
		lbl_date_2 = new JLabel("n/a", SwingConstants.CENTER); 
		btn_close = new JButton("close");

		lbl_scrollpane = new JLabel("Shipment IDs:");
		list = new JList(listModel);
		scrollpane_shipments = new JScrollPane(list);

		// layouts
		panel.setLayout(new BorderLayout(0, 0));
		panel_center.setLayout(new GridLayout(6, 2, 0, 0));
		panel_left.setLayout(new GridLayout(2, 1, 0, 0));

		// configure components
		text_warehouse_name.setColumns(16);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		viewShipment((String) list.getSelectedValue());
		list.setVisibleRowCount(10);
		scrollpane_shipments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		lbl_warehouse_id_1.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_warehouse_id_2.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_shipment_id_1.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_shipment_id_2.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_method_1.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_method_2.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_weight_1.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_weight_2.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_date_1.setBorder(BorderFactory.createLineBorder(Color.black));
		lbl_date_2.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// Add listeners
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				viewShipment((String) list.getSelectedValue());
			}
		});

		text_warehouse_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeWarehouseName();
			}
		});

		btn_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main_frame_warehouse.dispose();
			}
		});
		// add components
		main_frame_warehouse.getContentPane().add(panel, BorderLayout.CENTER);
		panel.add(panel_top, BorderLayout.NORTH);
		panel.add(panel_left, BorderLayout.WEST);
		panel.add(panel_center, BorderLayout.CENTER);

		panel_top.add(lbl_warehouse_id);
		panel_top.add(lbl_warehouse_name);
		panel_top.add(text_warehouse_name);

		panel_center.add(lbl_warehouse_id_1);
		panel_center.add(lbl_warehouse_id_2);
		panel_center.add(lbl_shipment_id_1);
		panel_center.add(lbl_shipment_id_2);
		panel_center.add(lbl_method_1);
		panel_center.add(lbl_method_2);
		panel_center.add(lbl_weight_1);
		panel_center.add(lbl_weight_2);
		panel_center.add(lbl_date_1);
		panel_center.add(lbl_date_2);
		panel_center.add(btn_close);

		panel_left.add(lbl_scrollpane);
		panel_left.add(scrollpane_shipments);

		main_frame_warehouse.setSize(900, 500);
		main_frame_warehouse.setVisible(true);
		main_frame_warehouse.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame_warehouse.getContentPane().setLayout(new BorderLayout(0, 0));
	}

	/**
	 * private method to centralize listener logic. updates JLabels to the values of
	 * the currently selected Shipment.
	 * 
	 * @param shipment_id the id of the currently selected shipment
	 */
	private void viewShipment(String shipment_id) {
		if (shipment_id != null) {
			Date receipt = new Date(warehouse_contents.get(shipment_id).getReceipt_date());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			lbl_warehouse_id_2.setText(warehouse_contents.get(shipment_id).getWarehouse_id());
			lbl_shipment_id_2.setText(shipment_id);
			lbl_method_2.setText(warehouse_contents.get(shipment_id).getShipment_method().toString());
			lbl_weight_2.setText(String.format("%.2f lbs", warehouse_contents.get(shipment_id).getWeight()));
			lbl_date_2.setText(formatter.format(receipt));
		}
	}

	/**
	 * private method to centralize listener logic. Changes the name of the
	 * currently selected warehouse.
	 */
	private void changeWarehouseName() {
		company.setWarehouseName(warehouse_id, text_warehouse_name.getText());
	}
}
