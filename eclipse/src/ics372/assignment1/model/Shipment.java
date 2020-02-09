package ics372.assignment1.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Shipment class composed of getters and setters to access the different
 * attributes of a shipment
 * 
 * @author nicole
 *
 */
public class Shipment {
	private String warehouse_id;
	private ShippingMethod shipment_method;
	private String shipment_id;
	private double weight;
	private Long receipt_date;

	/**
	 * ShippingMethod enum
	 * 
	 */
	public enum ShippingMethod {
		air, truck, ship, rail;
	}

	/**
	 * Shipment constructor
	 * 
	 * @param warehouse_id
	 * @param shipment_method
	 * @param shipment_id
	 * @param weight
	 * @param receipt_date
	 */
	public Shipment(String warehouse_id, ShippingMethod shipment_method, String shipment_id, double weight,
			Long receipt_date) {
		this.warehouse_id = (warehouse_id == null) ? "" : warehouse_id;
		this.shipment_method = shipment_method;
		this.shipment_id = (shipment_id == null) ? "" : shipment_id;
		this.weight = weight;
		this.receipt_date = receipt_date;
	}

	/**
	 * Default constructor
	 */
	public Shipment() {
		this(null, null, null, 0, (long) 0);
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
	public Long getReceipt_date() {
		return receipt_date;
	}

	/**
	 * @param receipt_date the receipt_date to set
	 */
	public void setReceipt_date(Long receipt_date) {
		this.receipt_date = receipt_date;
	}

	/**
	 * returns a String containing the information of a shipment
	 */
	public String toString() {
		Date receipt = new Date(receipt_date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String outputString = String.format(
				"\n\t\"warehouse_id\":\"%s\",\n" + "\t\"shipment_method\":\"%s\",\n" + "\t\"shipment_id\":\"%s\",\n"
						+ "\t\"weight\":\"%f\",\n" + "\t\"receipt_date\":\"%s\",\n",
				warehouse_id, shipment_method, shipment_id, weight, formatter.format(receipt));
		return outputString;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Shipment)) {
			return false;
		}
		Shipment shipment = (Shipment) obj;
		return this.shipment_id.equalsIgnoreCase(shipment.shipment_id);
	}
}
