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
				// throw exception
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
	public void log(String entry) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		try {
			String log_entry = String.format("[%s]\t[%s]\n", formatter.format(now), entry);
			BufferedWriter writer = new BufferedWriter(new FileWriter(company_log, true));
			writer.write(log_entry);
			writer.close();
			System.out.print(log_entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void importShipments(File jsonShipmentList)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();
		Warehouse temp = new Warehouse("tempuse");
		temp = gson.fromJson(new FileReader(jsonShipmentList), Warehouse.class);
		for (Shipment s : temp.getWarehouse_contents()) {
			System.out.println(s.toString());
		}
	}

	public void exportContentToJSON(String warehouseId) {

		Gson gson = new Gson(); // declare
		Warehouse warehouse = this.getWarehouse(warehouseId);

//		List<Warehouse> shipmentsById = new ArrayList<Warehouse>(); // declare. can probably be condensed seeing as its
//																	// member variable
//		Company containWarehouses = new Company(); // declare
//
//		for (Warehouse warehouse : warehouses) {
//			if (warehouse.getWarehouse_id().equals(warehouseId)) {
//				shipmentsById.add(warehouse);
//				System.out.println(warehouse.getWarehouse_id());
//			}
//		}
//		containWarehouses.setWarehouse_contents(shipmentsById);
		// https://stackoverflow.com/questions/45995067/writer-not-working-for-json-file-using-gson-json-file-is-blank-after-code-execu
		// ^ for flush and close
		try {
			FileWriter writer = new FileWriter(String.format("%s.json", warehouseId));
			gson.toJson(warehouse, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			// send to UI
		}
		// GsonBuilder().setPrettyPrinting().create(); for same format as other file
		// otherwise it compacts as so
		// am I putting this into a new file? static creating one called
		// shipmentbywarehouse
	}

	/**
	 * 
	 * @param warehouse_id
	 * @return
	 */
	public String readWarehouseContent(String warehouse_id) {
		return this.getWarehouse(warehouse_id).toString();
	}
//	public static void main(String[] args) {
//		File file = new File("example.json");
//
//		Gson gson = new Gson();
//
//		// pointless example testing gson on Objects
//		Object object = null;
//		try {
//			object = gson.fromJson(new FileReader(file), Object.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(object);
//
//		// gson can return everything into a String to use
//		String json = gson.toJson(object);
//		System.out.println(json);
//
//		// testing Company classes ability to read in json with gson
//		Company teste = new Company();
//		try {
//			teste = gson.fromJson(new FileReader(file), Company.class);
//		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
//			//
//			e.printStackTrace();
//		}
//		// testing exportContentToJSON.
//		// teste.exportContentToJSON("15566");
//
//		// testing importShipments. Currently return type Company.
//		Company j = teste.importShipments(file);
//		System.out.println(j.getWarehouse_contents().get(0).getShipment_id());
//
//		// testing getWarehouse(warehouseId), returns Warehouse
//		System.out.println(teste.getWarehouse("15566").getShipment_id());
//
//		// testing getWarehouseIds
////			String[] getIds = teste.getWarehouseIds();
////			for (int i = 0; i < getIds.length; i++) {
////				System.out.println(getIds[i]);
////			}
//
//		// testing getWarehouses. basically does same thing as getWarehouse_contents
////			Warehouse[] getWares = teste.getWarehouses();
////			for (int i = 0; i < getWares.length; i++) {
////				//System.out.println(i);
////				System.out.println(getWares[i].getWeight());
////			}
//
//	}
}
