package ics372.assignment1.model;

public class Shipment {
	int warehouse_id;
	String shipment_method;
	String shipment_id;
	double weight;
	int receipt_date;
	
	public Shipment (int warehouse_id, String shipment_method, String shipment_id, double weight, int receipt_date) {
		this.warehouse_id = warehouse_id;
		this.shipment_method = shipment_method;
		this.shipment_id = shipment_id;
		this.weight = weight;
		this.receipt_date = receipt_date;
	}

	public int getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id (int warehouse_id) {
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getReceipt_date() {
		return receipt_date;
	}

	public void setReceipt_date(int receipt_date) {
		this.receipt_date = receipt_date;
	}

}
