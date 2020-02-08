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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Company {
		

		public List<Warehouse> warehouse_contents;

		public List<Warehouse> getWarehouse_contents() {
			return warehouse_contents;
		}


		public void setWarehouse_contents(List<Warehouse> warehouse_contents) {
			this.warehouse_contents = warehouse_contents;
		}


		



	/**
	 * 
	 * @param entry
	 */
	public void log(String entry) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(company_log, true));) {
			String log_entry = String.format("[%s]\t[%s]\n", formatter.format(now), entry);
			writer.write(log_entry);
			System.out.print(log_entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param jsonShipmentList
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 * @throws FileNotFoundException
	 */
	public void importShipments(File jsonShipmentList)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();
		Warehouse temp = new Warehouse("tempuse");
		temp = gson.fromJson(new FileReader(jsonShipmentList), Warehouse.class);
		log(String.format("importing shipments from %s", jsonShipmentList.getName()));
		for (Shipment s : temp.getWarehouse_contents()) {
			addIncomingShipment(s);
		}
	}



	/**
	 * 
	 * @param warehouseId
	 * @throws IOException
	 */
	public void exportContentToJSON(String warehouseId) throws IOException {
		Warehouse warehouse = this.getWarehouse(warehouseId);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(String.format("%s.json", warehouseId));) {
			writer.write(gson.toJson(warehouse));
			log(String.format("warehouse%s: exported to %s.json", warehouseId, warehouseId));
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 * @return
	 */
	public String readWarehouseContent(String warehouse_id) {
		return this.getWarehouse(warehouse_id).toString();
	}
}
