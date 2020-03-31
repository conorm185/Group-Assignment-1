/**
 * 
 */
package ics372.assignment1.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ics372.assignment1.model.Company;
import ics372.assignment1.model.CompanyIO;
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
	@Test				//ok
	void testJSONFileWasReadContainingShipmentInformation() {
		String jsonFileName = "test.json";
		String shipment_idToCheck = "1ad2f4";
		try {
			CompanyIO.importShipments(new File(jsonFileName));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<String> warehouseIDs =  company.getWarehouseIds();
	
		for(String id : warehouseIDs) {
			 Warehouse warehouse = CompanyIO.getWarehouse(id);
			 
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
	
	@Test				//ok
	void testFourShippingMethodsAreSupported() {
		
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
	
	@Test			//ok
	void testShipmentIDAndGrossWeightAreAssociatedWithSpecifiedWarehouseID() {
		// CHeck the sample shipment that the warehouse it belongs to actually has it in its record 
		String wareHouseIDThatSampleShipmentBelongsTo = sampleShipment.getWarehouse_id();
		Warehouse warehouse = CompanyIO.getWarehouse(wareHouseIDThatSampleShipmentBelongsTo);
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
	

	//@Test	   //need to change or update the shipping_id of test3.json & junittest1.json. 
	void testWarehouseCommands() {
		try {
			CompanyIO.importShipments(new File("test3.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Confirm that warehouse can add incoming shipment,
		// get the number of Shipments in warehouse
		String warehouseID = "33333";
		Warehouse warehouse = CompanyIO.getWarehouse(warehouseID);
		List<Shipment> shipments = warehouse.getWarehouse_contents();
		int initialShipmentCount = shipments.size();
		// add a shipment to the warehouse
		try {
			CompanyIO.importShipments(new File("junittest1.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//assert that the number of shipment in warehouse is more than before
		Warehouse warehouse2 = CompanyIO.getWarehouse(warehouseID);
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
	//@Test			 //need to change or update the shipping_id of test.json & junittest2.json.
	void testAddingShipmentToFreightReceiptEnabledWarehouse() {
		try {
			CompanyIO.importShipments(new File("test.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// find a warehouse that  has enabled freight receipt.
		String warehouse_id1 = "12513";
		String warehouse_id2 =  "15566";
		
		Warehouse warehouse1 = CompanyIO.getWarehouse(warehouse_id1);
		List<Shipment> shipments1 = warehouse1.getWarehouse_contents();
		int ShipmentCountWarehouse1 = shipments1.size();
		
		Warehouse warehouse2 = CompanyIO.getWarehouse(warehouse_id2);
		List<Shipment> shipments2 = warehouse2.getWarehouse_contents();
		int ShipmentCountWarehouse2 = shipments2.size();
		
		warehouse1.setReceiving_freight(false);
		warehouse2.setReceiving_freight(true);
		
		// add shipments to the warehouses
				try {
					CompanyIO.importShipments(new File("junittest2.json"));
				} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				warehouse1 = CompanyIO.getWarehouse(warehouse_id1);
				shipments1 = warehouse1.getWarehouse_contents();
				int newShipmentCountWarehouse1 = shipments1.size();
				
				 warehouse2 = CompanyIO.getWarehouse(warehouse_id2);
				shipments2 = warehouse2.getWarehouse_contents();
				int newShipmentCountWarehouse2 = shipments2.size();
				
				//confirms that warehouse 1 didn't increase its shipment, while warehouse2 did
				assertTrue(ShipmentCountWarehouse1 == newShipmentCountWarehouse1);
				// confirms that incoming shipments can only be added to a warehouse that has enabled freight receipt.
				assertTrue(newShipmentCountWarehouse2 > ShipmentCountWarehouse2);
		
	}
	

	@Test				//ok
	void testShipmentsFromWarehouseAreExportedToJSONFile() {
		try {
			CompanyIO.importShipments(new File("test.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//export all shipments from a warehouse into a single JSON file.15566.json
		String warehouse_id =  "15566";
		Warehouse warehouse = CompanyIO.getWarehouse(warehouse_id);
		List<Shipment> shipments1 = warehouse.getWarehouse_contents();
		int shipmentCount = shipments1.size();
		try {
			CompanyIO.exportContentToJSON(warehouse_id);
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
	
	@Test			//ok
	void testWhichWarehouseAShipmentIsLocated() {
		String test1ShipmentID = "49214j";
		//Import a Shipment
		try {
			CompanyIO.importShipments(new File("test1.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//For Each Warehouse, inquire if they contain the imported shipment
		Shipment foundShipment = findShipment(test1ShipmentID);
		//We expect one of our warehouses to have that shipment
		assertTrue(foundShipment != null);
	}
	
	//@Test     //need to change or update the shipping_id of test2.json & the variable test2ShipmentID
	void testCompanyStateIsSavedAfterDataEntry() {
		Gson gson = new Gson();
		String test2ShipmentID = "51nn47j";
		//Check if shipment previously existed in company
		Shipment existed = findShipment(test2ShipmentID);
		assert(existed == null);
		//We add import a shipment into the Company
		try {
			CompanyIO.importShipments(new File("test2.json"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//We reload the company.json file to view its new state and inquire if it
		//contains the new Shipment added.
		String jsonString = readFileContents("company.json");
		Company companyClone = gson.fromJson(jsonString, Company.class);
		Shipment foundShipment = findShipmentFromCompanyClone(test2ShipmentID, companyClone);
		//We expect it to contain the new Shipment data
		assertTrue(foundShipment != null);
	}
	
	@Test      //ok
	void testIncomingXMLIsImportedIntoCompanyData() {
		String test3ShipmentID = "16wde";
		//Import XML file containing shipment
		try {
			CompanyIO.importShipments(new File("test1.xml"));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		//Verify that that Shipment is present in the Company data
		Shipment foundShipment = findShipment(test3ShipmentID);
		assert(foundShipment != null);
	}
	
	String readFileContents(String fileName) {
		File file = new File(fileName);
		StringBuffer lines = new StringBuffer();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);		
			String line;
		
			while((line = br.readLine()) != null){
			    //process the line
			   lines.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines.toString();
	}
	
	Shipment findShipment(String shipmentID) {
		Shipment shipmentFound = null;
		ArrayList<String> warehouseIDs = company.getWarehouseIds();
		for(String warehouseID : warehouseIDs) {
			Warehouse warehouse = CompanyIO.getWarehouse(warehouseID);
			if(warehouse != null) {
				ArrayList<Shipment> shipments = warehouse.getWarehouse_contents();
				for(Shipment shipment : shipments) {
					if(shipment.getShipment_id().equals(shipmentID)) {
						shipmentFound = shipment;
						break;
					}
				}
			}
		}
		return shipmentFound;
	}
	
	Shipment findShipmentFromCompanyClone(String shipmentID, Company companyClone) {
		Shipment shipmentFound = null;
		ArrayList<String> warehouseIDs = companyClone.getWarehouseIds();
		for(String warehouseID : warehouseIDs) {
			Warehouse warehouse = CompanyIO.getWarehouse(warehouseID);
			if(warehouse != null) {
				ArrayList<Shipment> shipments = warehouse.getWarehouse_contents();
				for(Shipment shipment : shipments) {
					if(shipment.getShipment_id().equals(shipmentID)) {
						shipmentFound = shipment;
						break;
					}
				}
			}
		}
		return shipmentFound;
	}

}
