package ics372.assignment1.model;

public class Warehouse {
	
	
	private String warehouse_id;
	private boolean receiving_freight;
	private ArrayList<Shipment> warehouse_contents;
	
	// auto generated constructor and getters/setters
	public Warehouse(String warehouse_id, String shipment_method, String shipment_id, String weight,
			String receipt_date) {
		super();
		this.warehouse_id = warehouse_id;
		this.receiving_freight = true;
	}

	/**
	 * 
	 */
	public Warehouse() {
		this(null);
	}

	/**
	 * @return the warehouse_contents
	 */
	public ArrayList<Shipment> getWarehouse_contents() {
		return warehouse_contents;
	}
	

	/**
	 * 
	 */
	public String toString() {
		String outputString = String.format(
				"\n\"warehouse_id\":\"%s\",\n" + "\"receiving_freight\":\"%s\",\n" + "\"warehouse_contents\":[\n",
				warehouse_id, receiving_freight);
		for (Shipment s : warehouse_contents) {
			outputString += String.format("{%s},", s.toString());
		}
		outputString.substring(0, outputString.length() - 1);// remove last comma
		outputString += "]";
		return outputString;
	}
	
}
