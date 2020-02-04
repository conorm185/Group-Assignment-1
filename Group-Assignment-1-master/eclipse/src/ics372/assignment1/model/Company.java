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
	
	public List<Warehouse> ware_house_contents;
	//static Company instance;
	//String[] warehouseIds;
	//Warehouse[] warehouses;
	//String freightReceiptStatus;
	
	// what is company receiving/doing?
	public Company() {
		ware_house_contents = new ArrayList<Warehouse>();
	}
	
	public List<Warehouse> getWarehouseItems() {
		return ware_house_contents;
	}

	public void setWarehouseItems(ArrayList<Warehouse> warehouseItems) {
		this.warehouseItems = warehouseItems;
	}

	public static Company getInstance() {
		
		return instance;
	}
	
	public String[] getWarehouseIds() {
		
		return warehouseIds;
	}
	
	public Warehouse[] getWarehouses() {
		
		return warehouses;
	}
	
	//assumption code no idea how i'm receiving warehouse
	public Warehouse getWarehouse(String warehouseId) {
		Warehouse warehouse = null;
		for (Warehouse ware : this.warehouses) {
			//if(ware.getId.equals(warehouseId)) {
			//	warehouse = ware;
			//}
		}
		return warehouse;
	}
	
	public void addIncomingShipment(Shipment shipment) {
		
	}
	
	public void toggleFreightReciept(String warehouseId) {
		
	}
	
	public String getFreightReceiptStatus(String WarehouseId) {
		return freightReceiptStatus;
	}

	
	// fix json file path location
	// am i creating the arraylist through this?
	// source https://mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
	public void importShipments(File jsonShipmentList) {
		Gson gson = new Gson();
		try(Reader reader = new FileReader(jsonShipmentList)){
			Warehouse warehouse = gson.fromJson(reader, Warehouse.class);
		
		} catch (IOException e) {
			// can't read file. send to ui
			e.printStackTrace();
		}
	}
	
	// assumption code, need Warehouse declarations
	public void exportContentToJSON(String warehouseId) {
		// are we looping the arraylist for a certain id to export?
		// foreach warehouse id in warehouse arraylist
		// warehouse with warehouse id == warehouse
		Gson gson = new Gson();
		Warehouse warehouse = new Warehouse();
		
		try {
			gson.toJson(warehouse, new FileWriter("filepath"));
		} catch (JsonIOException | IOException e) {
			// can't write to file. send to ui.
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		File file = new File("example.json");
		//Company tester = new Company();
		//tester.importShipments(file);

		Gson gson = new Gson();
		Object object = null;
		try {
			object = gson.fromJson(new FileReader(file), Object.class);
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(object);
		
		String json = gson.toJson(object);
		System.out.println(json);
		Company teste = new Company();
		try {
			teste = gson.fromJson(new FileReader(file), Company.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(teste.getWarehouseItems().size());
		
		
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
