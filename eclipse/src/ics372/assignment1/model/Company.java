package ics372.assignment1.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.*;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Company {
		

		public List<Warehouse> warehouse_contents;
		//static Company instance;
		//String[] warehouseIds;
		//Warehouse[] warehouses;
		//String freightReceiptStatus;

		public List<Warehouse> getWarehouse_contents() {
			return warehouse_contents;
		}


		public void setWarehouse_contents(List<Warehouse> warehouse_contents) {
			this.warehouse_contents = warehouse_contents;
		}


		// what is company receiving/doing?
		public Company() {
			warehouse_contents = new ArrayList<Warehouse>();
		}

		// what do
		public static Company getInstance() {
			//TODO
			return instance;
		}

		// assumption code. change return type to warehouseId type
		// returns an array containing all warehouse ID's
		public String[] getWarehouseIds() {
			String[] ids = new String[warehouse_contents.size()];
			int count = 0; 
			for (Warehouse warehouse : warehouse_contents) {
				ids[count] = warehouse.getWarehouse_id();
				count++;
			}
			return ids;
		}

		// fills an array with Warehouse objects
		// probably unnecessary since the warehouses are already stored as instance variable with gson
		public Warehouse[] getWarehouses() {
			Warehouse[] warehouses = new Warehouse[warehouse_contents.size()];
			int count = 0;
			for (Warehouse warehouse : warehouse_contents) {
				warehouses[count] = warehouse;
				count++;
			}
			return warehouses;
		}

		//assumption code, change parameter to id type 
		//some warehouses have same ID. what do.
		//returns Warehouse object with ID. Last Warehouse if there are multiple.
		public Warehouse getWarehouse(String warehouseId) {
			Warehouse returnWarehouse = new Warehouse();
			for (Warehouse warehouse : this.warehouse_contents) {
				if(warehouse.getWarehouse_id().equals(warehouseId)) {
					returnWarehouse = warehouse;
					System.out.println("Warehouse id match found");
				}
			}
			return returnWarehouse;
		}
		
		// what do
		public void addIncomingShipment(Shipment shipment) {
			//TODO
		}

		// what do
		public void toggleFreightReciept(String warehouseId) {
			//TODO
		}

		// what do
		public String getFreightReceiptStatus(String WarehouseId) {
			//TODO
			return freightReceiptStatus;
		}


		// Requires a file location. example.json is in scope. Anything else needs to be moved into scope or full path required.
		// static filename should be added.
		// creates a new Company instance to be used and reads into it.
		// assumption code, good baseline, works as is.
		// source https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
		public Company importShipments(File jsonShipmentList) {
			Gson gson = new Gson();
			Company company = new Company();
			try {
				company = gson.fromJson(new FileReader(jsonShipmentList), Company.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// send to ui
				e.printStackTrace();
			}
			return company;
		}

		// assumption code. good baseline
		// can be condensed but works fine as is.
		public void exportContentToJSON(String warehouseId) {

			Gson gson = new Gson(); // declare
			List<Warehouse> shipmentsById = new ArrayList<Warehouse>(); // declare. can probably be condensed seeing as its member variable
			Company containWarehouses = new Company(); // declare
			
			for (Warehouse warehouse : warehouse_contents) {
				if(warehouse.getWarehouse_id().equals(warehouseId)){
					shipmentsById.add(warehouse);
					System.out.println(warehouse.getWarehouse_id());
				}
			}
			containWarehouses.setWarehouse_contents(shipmentsById);
			//https://stackoverflow.com/questions/45995067/writer-not-working-for-json-file-using-gson-json-file-is-blank-after-code-execu
			// ^ for flush and close
			try {
				FileWriter writer = new FileWriter("shipmentsbywarehouse.json");
				gson.toJson(containWarehouses, writer);
				writer.flush();
				writer.close();
			}catch(IOException e) {
				e.printStackTrace();
				// send to UI
			}
			// GsonBuilder().setPrettyPrinting().create(); for same format as other file otherwise it compacts as so
			// am I putting this into a new file? static creating one called shipmentbywarehouse
		}


		public static void main(String[] args) {
			File file = new File("example.json");

			Gson gson = new Gson();
			
			// pointless example testing gson on Objects
			Object object = null;
			try {
				object = gson.fromJson(new FileReader(file), Object.class);
			}catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println(object);
			
			// gson can return everything into a String to use
			String json = gson.toJson(object);
			System.out.println(json);
			
			// testing Company classes ability to read in json with gson
			Company teste = new Company();
			try {
				teste = gson.fromJson(new FileReader(file), Company.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// 
				e.printStackTrace();
			}
			// testing exportContentToJSON. 
			//teste.exportContentToJSON("15566");
			
			// testing importShipments. Currently return type Company. 
			Company j = teste.importShipments(file);
			System.out.println(j.getWarehouse_contents().get(0).getShipment_id());
			
			// testing getWarehouse(warehouseId), returns Warehouse
			System.out.println(teste.getWarehouse("15566").getShipment_id());
			
			// testing getWarehouseIds
//			String[] getIds = teste.getWarehouseIds();
//			for (int i = 0; i < getIds.length; i++) {
//				System.out.println(getIds[i]);
//			}
			
			// testing getWarehouses. basically does same thing as getWarehouse_contents
//			Warehouse[] getWares = teste.getWarehouses();
//			for (int i = 0; i < getWares.length; i++) {
//				//System.out.println(i);
//				System.out.println(getWares[i].getWeight());
//			}
			
		}
		/*
		 * ----------Needs-----------
		 * 
		 * private constructor
		 * 
		 * --------variables--------
		 * 
		 * ArrayList of warehouse objects
		 * 
		 * ---------methods----------
		 * 
		 * for every method that can generate an exception, throw it forward to the UI,
		 * so I can generate an error text.
		 * 
		 * public static Company getInstance()
		 * 
		 * public String[] getWarehouseIds()
		 * 
		 * public Warehouse[] getWarehouses()
		 * 
		 * public Warehouse getWarehouse(String warehouseId)
		 * 
		 * public void addIncomingShipment(Shipment shipment)
		 * 
		 * public void toggleFreightReciept(String warehouseId)
		 * 
		 * public String getFreightRecieptStatus(String WarehouseId)
		 * 
		 * public void importShipments(File jsonShipmentList)
		 * 
		 * public void exportContentToJSON(String warehouseId)
		 */
	

}
