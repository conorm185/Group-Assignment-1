package ics372.assignment1.model;

import java.util.ArrayList;

/*
 * Warehouse constraints:
 * 
 * 		warehouse_id may not be null. (enforced by Warehouse.Warehouse() and Warehouse.setWarehouse_id())
 * 
 * 		warehouse_id may not be changed once set. (private setter)
 * 
 * 		a warehouse may not contain two Shipments with the same shpiment_id. (enforced by Warehouse.addShipment())
 * 
 * 		a warehouse may not receive a shipment if receiving_freight is set to false. (enforced by Warehouse.addShipment())
 * 
 * 		two Warehouses are equal according to .equals() if they have the same warehouse_id
 * 
 */

/**
 * Warehouse class that holds a collection of shipments
 * 
 * @author nicole
 *
 */
public class Warehouse {
	private ArrayList<Shipment> warehouse_contents;
	private String warehouse_id;
	private boolean receiving_freight;

	/**
	 * Warehouse constructor
	 * 
	 * @param warehouse_id
	 */
	public Warehouse(String warehouse_id) {
		warehouse_contents = new ArrayList<Shipment>();
		this.warehouse_id = (warehouse_id == null) ? "" : warehouse_id;
		this.receiving_freight = true;
	}

	/**
	 * Default constructor
	 */
	public Warehouse() {
		this(null);
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
	private void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = (warehouse_id == null) ? "" : warehouse_id;
	}

	/**
	 * receiving flag for a new shipment
	 * 
	 * @return the receiving_freight
	 */
	public boolean isReceiving_freight() {
		return receiving_freight;
	}

	/**
	 * @param receiving_freight the receiving_freight to be set
	 */
	public void setReceiving_freight(boolean receiving_freight) {
		this.receiving_freight = receiving_freight;
	}

	/**
	 * method used to add a shipment to the warehouse_contents collection
	 * 
	 * @param shipment
	 * @return true if shipment was added to the warehouse_contents, and false if
	 *         the shipment was not added
	 */
	public boolean addShipment(Shipment shipment) {
		if (receiving_freight == true && !(warehouse_contents.contains(shipment))) {
			warehouse_contents.add(shipment);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * method used to remove a shipment from the warehouse_contents collection
	 * 
	 * @param shipment_id
	 * @return true if the shipment was removed from the warehouse_contents, and
	 *         false if the shipment was not removed
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
	 * method that returns a String of the contents within a warehouse
	 */
	@Override
	public String toString() {
		String outputString = String.format(
				"\n\"warehouse_id\":\"%s\",\n" + "\"receiving_freight\":\"%s\",\n" + "\"warehouse_contents\":[\n",
				warehouse_id, receiving_freight);
		for (Shipment s : warehouse_contents) {
			outputString += String.format("{%s},", s.toString());
		}
		outputString.substring(0, outputString.length() - 1);// remove last comma
		outputString += "]";
		return outputString;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Warehouse)) {
			return false;
		}
		Warehouse warehouse = (Warehouse) obj;
		return this.warehouse_id.equalsIgnoreCase(warehouse.warehouse_id);
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return warehouse_id.hashCode();
	}
}
