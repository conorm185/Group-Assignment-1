package ics372.assignment1.model;

import java.util.ArrayList;

/**
 * 
 * @author nicole
 *
 */
public class Warehouse {
	private ArrayList<Shipment> warehouse_contents;
	private transient String warehouse_id;
	private transient boolean receiving_freight;

	/**
	 * 
	 * @param warehouse_id
	 */
	public Warehouse(String warehouse_id) {
		warehouse_contents = new ArrayList<Shipment>();
		this.warehouse_id = warehouse_id;
		this.receiving_freight = true;
	}

	/**
	 * @return the warehouse_contents
	 */
	public ArrayList<Shipment> getWarehouse_contents() {
		return warehouse_contents;
	}

	/**
	 * @param warehouse_contents the warehouse_contents to set
	 */
	public void setWarehouse_contents(ArrayList<Shipment> warehouse_contents) {
		this.warehouse_contents = warehouse_contents;
	}

	/**
	 * @return the warehouse_id
	 */
	public String getWarehouse_id() {
		return warehouse_id;
	}

	/**
	 * @param warehouse_id the warehouse_id to set
	 */
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	/**
	 * @return the receiving_freight
	 */
	public boolean isReceiving_freight() {
		return receiving_freight;
	}

	/**
	 * @param receiving_freight the receiving_freight to set
	 */
	public void setReceiving_freight(boolean receiving_freight) {
		this.receiving_freight = receiving_freight;
	}

	/**
	 * 
	 * @param shipment
	 * @return
	 */
	public boolean addShipment(Shipment shipment) {
		if (receiving_freight == true) {
			warehouse_contents.add(shipment);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param shipment_id
	 * @return
	 */
	public boolean removeShipment(String shipment_id) {
		for (Shipment s : warehouse_contents) {
			if (s.getShipment_id().equalsIgnoreCase(shipment_id)) {
				warehouse_contents.remove(s);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	public String toString() {
		String outputString = "";

		return outputString;
	}
}
