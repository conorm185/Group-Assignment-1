package ics372.assignment1.model;
import java.util.Arrays;

public class Warehouse {
	
	static int warehouse_id;
	private Shipment [] incoming_shipments;
	int many_items = 0;

	public Warehouse (int capacity, int warehouse_id) {
		incoming_shipments = new Shipment [capacity];
		this.warehouse_id = warehouse_id;
	} 
	
	public int getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(int warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	
	//***CHANGE*** use some sort of compaeTo to see if shipment has enabled freight recepit 
	public int compareTo (Shipment shipment) {
		int output = -1;
		if (this.getWarehouse_id() == shipment.getWarehouse_id())
			output = 0;
		if (this.getWarehouse_id() != shipment.getWarehouse_id())
			output = 1;
		return output;
	}
	
	//method: add incoming shipment
	
	//how do I add only the shipment_id and weight???
	
	
	//add newShipment if they have 1/4 shipping methods (is this a requirement)
	//use if/else to check if incoming shipment has an enabled recipet
	//associate the shipment with the specified warehouse_id
	
	public void adding_incoming_shipment (int warehouse_id, String shipment_method, String shipment_id, double weight, int receipt_date) {
		Shipment newshipment = new Shipment (warehouse_id, shipment_method, shipment_id, weight, receipt_date);
		
		if ()
		
	}
		
	//method: enable freight receipt
	
	//method: end freight receipt

}
