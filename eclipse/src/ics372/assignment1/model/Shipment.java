package ics372.assignment1.model;

import java.time.LocalDateTime;

/**
 * 
 * @author nicole
 *
 */
public class Shipment {
	String warehouse_id;
	ShippingMethod shipment_method;
	String shipment_id;
	double weight;
	LocalDateTime receipt_date;

	/**
	 * 
	 * 
	 */
	public enum ShippingMethod {
		air, truck, ship, rail;
	}

	/**
	 * 
	 * @param warehouse_id
	 * @param shipment_method
	 * @param shipment_id
	 * @param weight
	 * @param receipt_date
	 */
	public Shipment(String warehouse_id, ShippingMethod shipment_method, String shipment_id, double weight,
			LocalDateTime receipt_date) {
		this.warehouse_id = warehouse_id;
		this.shipment_method = shipment_method;
		this.shipment_id = shipment_id;
		this.weight = weight;
		this.receipt_date = receipt_date;
	}

	/**
	 * @return the warehouse_id
	 */
	public String getWarehouse_id() {
		return warehouse_id;
	}

	/**
	 * @param warehouse_id the warehouse_id to set
	 */
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	/**
	 * @return the shipment_method
	 */
	public ShippingMethod getShipment_method() {
		return shipment_method;
	}

	/**
	 * @param shipment_method the shipment_method to set
	 */
	public void setShipment_method(ShippingMethod shipment_method) {
		this.shipment_method = shipment_method;
	}

	/**
	 * @return the shipment_id
	 */
	public String getShipment_id() {
		return shipment_id;
	}

	/**
	 * @param shipment_id the shipment_id to set
	 */
	public void setShipment_id(String shipment_id) {
		this.shipment_id = shipment_id;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the receipt_date
	 */
	public LocalDateTime getReceipt_date() {
		return receipt_date;
	}

	/**
	 * @param receipt_date the receipt_date to set
	 */
	public void setReceipt_date(LocalDateTime receipt_date) {
		this.receipt_date = receipt_date;
	}

	/**
	 * 
	 */
	public String toString() {
		String outputString = "";

		return outputString;
	}
}
