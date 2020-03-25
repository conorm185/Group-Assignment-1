package ics372.assignment1.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author conor murphy
 *
 */
public class CompanyIO {
	/**
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void importShipments(File file) throws Exception {
		Warehouse temp = CompanyIO.parseWarehouse(file);

		Company company = Company.getInstance();
		CompanyIO.log(String.format("importing shipments from %s", file.getName()));
		if (temp != null) { // if the .json was not empty
			for (Shipment s : temp.getWarehouse_contents()) {
				// Check shipment object for validity
				s.isValid(temp); // temp values
				// validate(s); should replace any unparsed fields with default values
				company.addIncomingShipment(s);
			}
		} else {
			CompanyIO.log("import empty");
		}
	}

	/**
	 * 
	 * @param warehouse_id
	 * @throws IOException
	 */
	public static void exportContentToJSON(String warehouse_id) throws IOException {
		Company company = Company.getInstance();
		Warehouse warehouse = company.getWarehouse(warehouse_id);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter(String.format("%s.json", warehouse_id));) {
			writer.write(gson.toJson(warehouse));
			log(String.format("warehouse%s: exported to %s.json", warehouse_id, warehouse_id));
		}
	}

	/**
	 * 
	 * @param entry
	 */
	public static void log(String entry) {
		File company_log = new File("company_log.txt");
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
	 * @param file
	 * @return
	 */
	private static String getFileExtension(File file) {
		// credit
		// https://www.technicalkeeda.com/java-tutorials/get-file-extension-using-java
		String extension = "";
		try {
			if (file != null && file.exists()) {
				String name = file.getName();
				extension = name.substring(name.lastIndexOf("."));
			}
		} catch (Exception e) {
			extension = "";
		}
		return extension;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static Warehouse parseWarehouse(File file) throws Exception {
		String file_type = CompanyIO.getFileExtension(file);
		Warehouse temp;
		switch (file_type) {
		case ".json":
			temp = CompanyIO.parseJSON(file);
			break;
		case ".xml":
			temp = CompanyIO.parseXML(file);
			break;
		default:
			throw new Exception();
		}
		return temp;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static Warehouse parseJSON(File file) throws Exception {
		Gson gson = new Gson();
		Warehouse temp = new Warehouse("tempuse");
		temp = gson.fromJson(new FileReader(file), Warehouse.class);
		return temp;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private static Warehouse parseXML(File file) {
		return null;
	}

	/**
	 * 
	 */
	protected static void saveState() {
		Company company = Company.getInstance();
		HashMap<Integer, Warehouse> warehouses = company.getWarehouses();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter writer = new FileWriter("company.json");) {
			writer.write(gson.toJson(warehouses));
			log(String.format("All content saved"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	protected static HashMap<Integer, Warehouse> loadState() {
		File file = new File("company.json");
		Gson gson = new Gson();
		Type genericType = new TypeToken<HashMap<Integer, Warehouse>>() {
		}.getType();
		HashMap<Integer, Warehouse> warehouses = new HashMap<Integer, Warehouse>();
		try {
			warehouses = gson.fromJson(new FileReader(file), genericType);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return warehouses;
	}
}
