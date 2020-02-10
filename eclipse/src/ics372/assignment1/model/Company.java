package ics372.assignment1.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

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
	private File company_log;
	private ArrayList<Warehouse> warehouses;

	/**
	 * 
	 */
	private Company() {
		warehouses = new ArrayList<Warehouse>();
		company_log = new File("company_log.txt");
	}

	/**
	 * 
	 * @return
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
	 * 
	 * @return
	 */
	public ArrayList<String> getWarehouseIds() {
		ArrayList<String> ids = new ArrayList<String>();
		for (Warehouse warehouse : warehouses) {
			ids.add(warehouse.getWarehouse_id());
		}
		return ids;
	}

	/**
	 * 
	 * @param warehouseId
	 * @return
	 */
	private Warehouse getWarehouse(String warehouseId) {
		for (Warehouse warehouse : warehouses) {
			if (warehouse.getWarehouse_id().equals(warehouseId)) {
				return warehouse;
			}
		}
		return null;
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
			if (warehouse.addShipment(shipment)) { // if the add was succesful
				log(String.format("Shipment: %s added to Warehouse: %s", shipment.getShipment_id(),
						warehouse.getWarehouse_id()));
				return true;
			} else { // otherwise log the failure
				log(String.format("Shipment: %s denied at Warehouse: %s ", shipment.getShipment_id(),
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
	 * 
	 * @param shipment_id
	 * @param warehouse_id
	 */
	private boolean removeShipment(String shipment_id, String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			return warehouse.removeShipment(shipment_id);
		} else {
			log(String.format("Warehouse: %s not found", warehouse_id));
			return false;
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 */
	public synchronized boolean addWarehouse(String warehouse_id) {
		Warehouse warehouse = new Warehouse(warehouse_id);
		if (!(warehouses.contains(warehouse))) { // if the warehouse isn't already on the list
			warehouses.add(warehouse);
			log(String.format("Warehouse: %s added to warehouse list", warehouse_id));
			return true;
		} else {
			log(String.format("Warehouse: %s already on list", warehouse_id));
			return false;
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 */
	private boolean removeWarehouse(String warehouse_id) {
		Warehouse warehouse = new Warehouse(warehouse_id);
		if (!(warehouses.contains(warehouse))) { // if the warehouse isn't already on the list
			log(String.format("Warehouse: %s removed from warehouse list", warehouse_id));
			return warehouses.remove(warehouse);
		} else {
			log(String.format("Warehouse: %s not on list", warehouse_id));
			return false;
		}
	}

	/**
	 * 
	 * @param warehouseId
	 */
	public void toggleFreightReciept(String warehouseId) {
		Warehouse warehouse = this.getWarehouse(warehouseId);
		if (warehouse != null) {
			if (warehouse.isReceiving_freight()) {
				warehouse.setReceiving_freight(false);
			} else {
				warehouse.setReceiving_freight(true);
			}
			log(String.format("Warehouse: %s freight status set to %b", warehouseId, warehouse.isReceiving_freight()));
		} else {
			log(String.format("Warehouse: %s not found", warehouseId));
		}
	}

	/**
	 * 
	 * @param warehouseId
	 * @return
	 */
	public boolean getFreightReceiptStatus(String warehouseId) {
		Warehouse warehouse = this.getWarehouse(warehouseId);
		if (warehouse != null) {
			return warehouse.isReceiving_freight();
		} else {
			log(String.format("Warehouse: %s not found", warehouseId));
			return false;
		}
	}

	/**
	 * 
	 * @param entry
	 */
	private void log(String entry) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(company_log, true));) {
			String log_entry = String.format("[%s]\t[%s]\n", formatter.format(now), entry);
			writer.write(log_entry);
			System.out.print(log_entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param jsonShipmentList
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 */
	public void importShipments(File jsonShipmentList)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();
		Warehouse temp = new Warehouse("tempuse");
		temp = gson.fromJson(new FileReader(jsonShipmentList), Warehouse.class);
		log(String.format("importing shipments from %s", jsonShipmentList.getName()));
		if (temp != null) { // if the .json was not empty
			for (Shipment s : temp.getWarehouse_contents()) {
				addIncomingShipment(s);
			}
		} else {
			log("import empty");
		}

	}

	/**
	 * 
	 * @param warehouseId
	 * @throws IOException
	 */
	public void exportContentToJSON(String warehouseId) throws IOException {
		Warehouse warehouse = this.getWarehouse(warehouseId);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(String.format("%s.json", warehouseId));) {
			writer.write(gson.toJson(warehouse));
			log(String.format("warehouse%s: exported to %s.json", warehouseId, warehouseId));
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 * @return
	 */
	public String readWarehouseContent(String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		if (warehouse != null) {
			return warehouse.toString();
		} else {
			return "Warehouse not found!";
		}
	}
}
