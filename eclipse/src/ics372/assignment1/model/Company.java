package ics372.assignment1.model;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Model Constraints: 
 * Company constraints:
 * 
 * 		Only one Company Object may exist at a time (a singleton)
 * 
 * 		a company may not have two Warehouses with the same company_id (enforced by Company.addWarehouse())
 * 
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
 * Shipment constraints:
 * 
 * 		shipment_id may not be null.(enforced by Shipment.Shipment() and Shipment.setShipment_id())
 * 
 * 		shipment_id may not be changed, once set (private setter)
 * 
 * 		warehouse_id may not be null.(enforced by Shipment.Shipment() and Shipment.setWarehouse_id())
 * 
 * 		two Shipments are equal according to .equals() if they have the same shipment_id
 * 
 * Potential issues:
 * 		
 * 		not exhaustively tested for multi-user thread safety. (not yet a requirement)
 * 
 * 		does not enforce unique shipment_ids between warehouses  (will need fixing if moving shipments is allowed)
 */

/**
 * 
 * @author tom
 *
 */
public class Company {

	private static Company company_instance = null;
	private HashMap<Integer, Warehouse> warehouses;

	/**
	 * Company constructor
	 */
	private Company() {
		warehouses = CompanyIO.loadState();
	}

	/**
	 * method that returns an instance of a company
	 * 
	 * @return Company
	 */
	public static Company getInstance() {
		if (company_instance == null) {
			synchronized (Company.class) {
				if (company_instance == null) {
					company_instance = new Company();
				}
			}
		}
		return company_instance;
	}

	/**
	 * method that returns an ArrayList of warehouse ids
	 * 
	 * @return ids
	 */
	public ArrayList<String> getWarehouseIds() {
		ArrayList<String> ids = new ArrayList<String>();
		warehouses.forEach((hash, warehouse) -> {
			ids.add(warehouse.getWarehouse_id());
		});
		return ids;
	}

	/**
	 * method that takes in a warehouse id and compares it to the collection of
	 * warehouses to find a match
	 * 
	 * @param warehouse_id
	 * @return warehouse
	 */
	protected Warehouse getWarehouse(String warehouse_id) {
		return warehouses.get(warehouse_id.hashCode());
	}

	protected HashMap<Integer, Warehouse> getWarehouses() {
		return warehouses;
	}

	/**
	 * Method to attempt to add a shipment to a corresponding warehouse, or create a
	 * new warehouse if one does not currently exist. log the outcome.
	 * 
	 * @param shipment
	 * @return
	 */
	public boolean addIncomingShipment(Shipment shipment) {
		Warehouse warehouse = this.getWarehouse(shipment.getWarehouse_id());
		if (warehouse != null) { // if the warehouse exists
			if (warehouse.addShipment(shipment)) { // if the add was successful
				CompanyIO.saveState();
				CompanyIO.log(String.format("Shipment: %s added to Warehouse: %s", shipment.getShipment_id(),
						warehouse.getWarehouse_id()));
				return true;
			} else { // otherwise log the failure
				CompanyIO.log(String.format("Shipment: %s denied at Warehouse: %s ", shipment.getShipment_id(),
						warehouse.getWarehouse_id()));
				return false;
			}
		} else { // if the warehouse doesn't exist, create one and then attempt add again.
					// (necessary for importing)
			addWarehouse(shipment.getWarehouse_id());
			return addIncomingShipment(shipment);
		}
	}

	/**
	 * Method used to remove a shipment from a warehouse
	 * 
	 * @param shipment_id
	 * @param warehouse_id
	 */
	private boolean removeShipment(String shipment_id, String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			CompanyIO.saveState();
			return warehouse.removeShipment(shipment_id);
		} else {
			CompanyIO.log(String.format("Warehouse: %s not found", warehouse_id));
			return false;
		}
	}

	/**
	 * Method used to add a new warehouse
	 * 
	 * @param warehouse_id
	 */
	public synchronized boolean addWarehouse(String warehouse_id) {
		Warehouse warehouse = new Warehouse(warehouse_id);

		if (warehouses.putIfAbsent(warehouse.hashCode(), warehouse) == null) { // if the previous value was null
			CompanyIO.saveState();
			CompanyIO.log(String.format("Warehouse: %s added to warehouse list", warehouse_id));
			return true;
		} else {
			CompanyIO.log(String.format("Warehouse: %s already on list", warehouse_id));
			return false;
		}
	}

	/**
	 * Method that takes a warehouse id and removes that warehouse
	 * 
	 * @param warehouse_id
	 */
	private boolean removeWarehouse(String warehouse_id) {
		if (warehouses.remove(warehouse_id.hashCode()) == null) {
			CompanyIO.saveState();
			CompanyIO.log(String.format("Warehouse: %s removed from warehouse list", warehouse_id));
			return true;
		} else {
			CompanyIO.log(String.format("Warehouse: %s not on list", warehouse_id));
			return false;
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 */
	public void toggleFreightReciept(String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			if (warehouse.isReceiving_freight()) {
				warehouse.setReceiving_freight(false);
			} else {
				warehouse.setReceiving_freight(true);
			}
			CompanyIO.saveState();
			CompanyIO.log(String.format("Warehouse: %s freight status set to %b", warehouse_id,
					warehouse.isReceiving_freight()));
		} else {
			CompanyIO.log(String.format("Warehouse: %s not found", warehouse_id));
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 * @return
	 */
	public boolean getFreightReceiptStatus(String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			return warehouse.isReceiving_freight();
		} else {
			CompanyIO.log(String.format("Warehouse: %s not found, unable to toggle freight status", warehouse_id));
			return false;
		}
	}

	/**
	 * public method to retrieve the name of a given warehouse
	 * 
	 * @param warehouse_id the id of the warehouse being searched for
	 * @return the name of the warehouse as a string
	 */
	public String getWarehouseName(String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			return warehouse.getWarehouse_name();
		} else {
			CompanyIO.log(String.format("Warehouse: %s not found, unable to retrieve name", warehouse_id));
			return "unnamed warehouse";
		}
	}

	/**
	 * public method the change the name of a given warehouse
	 * 
	 * @param warehouse_id   the id of the warehouse being renamed
	 * @param warehouse_name the new name of the warehouse
	 */
	public void setWarehouseName(String warehouse_id, String warehouse_name) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			warehouse.setWarehouse_name(warehouse_name);
			CompanyIO.log(String.format("Warehouse: %s name changed to %s", warehouse_id, warehouse_name));
		} else {
			CompanyIO.log(String.format("Warehouse: %s not found, unable to change name", warehouse_id));
		}
	}

	/**
	 * method that returns a string of the all the contents/shipments in a warehouse
	 * 
	 * @param warehouse_id
	 * @return String
	 */
	public HashMap<String, Shipment> readWarehouseContent(String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		HashMap<String, Shipment> shipments_in_warehouse = new HashMap<String, Shipment>();
		if (warehouse != null) {
			for (Shipment shipment : warehouse.getWarehouse_contents()) {
				shipments_in_warehouse.put(shipment.getShipment_id(), (Shipment) shipment.clone());
			}
		}
		return shipments_in_warehouse;
	}

//	/**
//	 * reload the warehouses state before each data operation to make sure all users
//	 * are using the same data.
//	 */
//	private void refreshContent() {
//		warehouses = CompanyIO.loadState();
//	}
}
