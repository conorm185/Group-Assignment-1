package ics372.assignment1.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author tom
 *
 */
public class Company {

	private static Company company_instance = null;
	private File company_log;
	PrintWriter output;
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
		return ids; // maybe .clone()
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
	 * 
	 * @param shipment
	 */
	public void addIncomingShipment(Shipment shipment) {
		Warehouse warehouse = this.getWarehouse(shipment.getWarehouse_id());
		if (warehouse != null) {
			if (warehouse.isReceiving_freight()) {
				warehouse.addShipment(shipment);
				log(String.format("Shipment: %s added to Warehouse: %s", shipment.getShipment_id(),
						warehouse.getWarehouse_id()));
			} else {
				log(String.format("Shipment: %s denied at Warehouse: %s", shipment.getShipment_id(),
						warehouse.getWarehouse_id()));
				throw new IllegalArgumentException();
			}
		} else {
			addWarehouse(shipment.getWarehouse_id());
			addIncomingShipment(shipment);
		}
	}

	/**
	 * 
	 * @param shipment_id
	 * @param warehouse_id
	 */
	private void removeShipment(String shipment_id, String warehouse_id) {
		this.getWarehouse(warehouse_id).removeShipment(shipment_id);
	}

	/**
	 * 
	 * @param warehouse_id
	 */
	public synchronized void addWarehouse(String warehouse_id) {
		if (this.getWarehouse(warehouse_id) == null) {
			warehouses.add(new Warehouse(warehouse_id));
			log(String.format("Warehouse: %s added to warehouse list", warehouse_id));
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 */
	private void removeWarehouse(String warehouse_id) {
		Warehouse warehouse = this.getWarehouse(warehouse_id);
		warehouses.remove(warehouse);

	}

	/**
	 * 
	 * @param warehouseId
	 */
	public void toggleFreightReciept(String warehouseId) {
		Warehouse warehouse = this.getWarehouse(warehouseId);
		if (warehouse.isReceiving_freight()) {
			warehouse.setReceiving_freight(false);
		} else {
			warehouse.setReceiving_freight(true);
		}
		log(String.format("Warehouse: %s freight status set to %b", warehouseId, warehouse.isReceiving_freight()));
	}

	/**
	 * 
	 * @param warehouseId
	 * @return
	 */
	public boolean getFreightReceiptStatus(String warehouseId) {
		return this.getWarehouse(warehouseId).isReceiving_freight();
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
		for (Shipment s : temp.getWarehouse_contents()) {
			addIncomingShipment(s);
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
		return this.getWarehouse(warehouse_id).toString();
	}
}
