package ics372.assignment1.model;

public class Warehouse {

	public String warehouse_id;
	public String shipment_method;
	public String shipment_id;
	public String weight;
	public String receipt_date;
	

	public String getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public String getShipment_method() {
		return shipment_method;
	}

	public void setShipment_method(String shipment_method) {
		this.shipment_method = shipment_method;
	}

	public String getShipment_id() {
		return shipment_id;
	}

	public void setShipment_id(String shipment_id) {
		this.shipment_id = shipment_id;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getReceipt_date() {
		return receipt_date;
	}

	public void setReceipt_date(String receipt_date) {
		this.receipt_date = receipt_date;
	}
	
}
