/**
 * 
 */
package ics372.assignment1.test;

import static org.junit.jupiter.api.Assertions.*;
 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.Shipment;
import ics372.assignment1.model.Shipment.ShippingMethod;
import ics372.assignment1.model.Warehouse;

/**
 * @author gyan
 *
 */
class ShippingTest {
	static private Company company;
	static private Shipment sampleShipment;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		company = Company.getInstance();
	}

		// to test if the particular shipment exists in the Json file
	@Test
	void testJSONFileWasReadContainingShipmentInformation() {
		String jsonFileName = "test.json";
		String shipment_idToCheck = "1adf4";
		try {
			company.importShipments(new File(jsonFileName));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> warehouseIDs =  company.getWarehouseIds();
	
		for(String id : warehouseIDs) {
			 Warehouse warehouse = company.getWarehouse(id);
			 
			 // Get the shipments for each warehouse and check if has our shipment 1adf4
			 ArrayList<Shipment> shipments = warehouse.getWarehouse_contents();
			 for(Shipment shipment : shipments) {
				 if(shipment.getShipment_id().equals(shipment_idToCheck)) {
					 sampleShipment = shipment;
					 break;
				 }
			 }
		}
		assertNotNull(sampleShipment);
			
	}
	
	@Test
	void testFourShippingMethodsAreSupported() {
		/*
		 * 	public enum ShippingMethod {
				air, truck, ship, rail;
			}
		 */
		//Check new Shipment().getShipment_method() OR print the contents of enum ShippingMethodshowing each frieght type
		boolean containsAirFreight = false;
		boolean containsTruckFreight = false;
		boolean containsShipFreight = false;
		boolean containsRailFreight = false;
		ShippingMethod[] availableShippingMethods = Shipment.ShippingMethod.values();
		for(ShippingMethod sm : availableShippingMethods) {
			if(sm.equals(ShippingMethod.air)) {
				containsAirFreight = true;
			}
			else if(sm.equals(ShippingMethod.rail)) {
				containsRailFreight = true;
			}
			else if(sm.equals(ShippingMethod.truck)) {
				containsTruckFreight = true;
			}
			else if(sm.equals(ShippingMethod.ship)) {
				containsShipFreight = true;
			}
		}
	
		assertTrue(containsAirFreight);
		assertTrue(containsTruckFreight);
		assertTrue(containsShipFreight);
		assertTrue(containsRailFreight);
	}
	
	// Check the sample shipment that the warehouse it belongs to actually has it in its record 
	@Test
	void testShipmentIDAndGrossWeightAreAssociatedWithSpecifiedWarehouseID() {
		// CHeck the sample shipment that the warehouse it belongs to actually has it in its record 
		String wareHouseIDThatSampleShipmentBelongsTo = sampleShipment.getWarehouse_id();
		Warehouse warehouse = company.getWarehouse(wareHouseIDThatSampleShipmentBelongsTo);
		boolean warehouseContainsSampleShipmentID = false;
		boolean warehouseContainsSampleShipmentGrossWeight = false;
		
		 ArrayList<Shipment> shipments = warehouse.getWarehouse_contents();
		 Shipment matchingShipment = null;
		 for(Shipment shipment : shipments) {
			 if(shipment.getShipment_id().equals(sampleShipment.getShipment_id())) {
				 matchingShipment = shipment;
				 warehouseContainsSampleShipmentID = true;
				 break;
			 }
		 }
		 
		 if(matchingShipment.getWeight() == sampleShipment.getWeight()) {
			 warehouseContainsSampleShipmentGrossWeight = true;
		 }
		 assertTrue(warehouseContainsSampleShipmentID);
		 assertTrue(warehouseContainsSampleShipmentGrossWeight);
		 
	}
	

	@Test
	void testWarehouseCommands() {
		try {
			company.importShipments(new File("33333.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Confirm that warehouse can add incoming shipment,
		// get the number of Shipments in warehouse
		String warehouseID = "33333";
		Warehouse warehouse = company.getWarehouse(warehouseID);
		List<Shipment> shipments = warehouse.getWarehouse_contents();
		int initialShipmentCount = shipments.size();
		// add a shipment to the warehouse
		try {
			company.importShipments(new File("junittest1.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//assert that the number of shipment in warehouse is more than before
		Warehouse warehouse2 = company.getWarehouse(warehouseID);
		List<Shipment> shipments2 = warehouse2.getWarehouse_contents();
		int newShipmentCount = shipments2.size();
		assertTrue(newShipmentCount > initialShipmentCount);
		
		// can enable freight receipt, 
		// enable freight receipt for warehouse and check its status is enabled
		warehouse2.setReceiving_freight(true);
		assertTrue(warehouse2.isReceiving_freight());
		// can end freight receipt.
		// disable freight receipt for warehouse and check its status is disabled
		warehouse2.setReceiving_freight(false);
		assertFalse(warehouse2.isReceiving_freight());
		
	}
	@Test
	void testAddingShipmentToFreightReceiptEnabledWarehouse() {
		try {
			company.importShipments(new File("test.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// find a warehouse that  has enabled freight receipt.
		String warehouse_id1 = "12513";
		String warehouse_id2 =  "15566";
		
		Warehouse warehouse1 = company.getWarehouse(warehouse_id1);
		List<Shipment> shipments1 = warehouse1.getWarehouse_contents();
		int ShipmentCountWarehouse1 = shipments1.size();
		
		Warehouse warehouse2 = company.getWarehouse(warehouse_id2);
		List<Shipment> shipments2 = warehouse2.getWarehouse_contents();
		int ShipmentCountWarehouse2 = shipments2.size();
		
		warehouse1.setReceiving_freight(false);
		warehouse2.setReceiving_freight(true);
		
		// add shipments to the warehouses
				try {
					company.importShipments(new File("junittest2.json"));
				} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				warehouse1 = company.getWarehouse(warehouse_id1);
				shipments1 = warehouse1.getWarehouse_contents();
				int newShipmentCountWarehouse1 = shipments1.size();
				
				 warehouse2 = company.getWarehouse(warehouse_id2);
				shipments2 = warehouse2.getWarehouse_contents();
				int newShipmentCountWarehouse2 = shipments2.size();
				
				//confirms that warehouse 1 didn't increase its shipment, while warehouse2 did
				assertTrue(ShipmentCountWarehouse1 == newShipmentCountWarehouse1);
				// confirms that incoming shipments can only be added to a warehouse that has enabled freight receipt.
				assertTrue(newShipmentCountWarehouse2 > ShipmentCountWarehouse2);
		
	}
	

	@Test
	void testShipmentsFromWarehouseAreExportedToJSONFile() {
		try {
			company.importShipments(new File("test.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//export all shipments from a warehouse into a single JSON file.15566.json
		String warehouse_id =  "15566";
		Warehouse warehouse = company.getWarehouse(warehouse_id);
		List<Shipment> shipments1 = warehouse.getWarehouse_contents();
		int shipmentCount = shipments1.size();
		try {
			company.exportContentToJSON(warehouse_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//import from that JSON file into a Warehouse object
		Gson gson = new Gson();
		Warehouse temp = new Warehouse(warehouse_id);
		try {
			temp = gson.fromJson(new FileReader(String.format("%s.json", warehouse_id)), Warehouse.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Shipment> shipments2 = temp.getWarehouse_contents();
		int newShipmentCount = shipments2.size();
		System.out.println("New shipment count is " + newShipmentCount);
		
		assertTrue(shipmentCount == newShipmentCount);
		
	}
	
}
