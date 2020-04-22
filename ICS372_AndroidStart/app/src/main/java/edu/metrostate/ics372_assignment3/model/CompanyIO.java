package edu.metrostate.ics372_assignment3.model;

import android.util.Log;

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

import edu.metrostate.ics372_assignment3.io.Importable;
import edu.metrostate.ics372_assignment3.io.ImporterJSON;
import edu.metrostate.ics372_assignment3.io.ImporterXML;

/**
 * Class filled with static methods to handle all file input/output operations
 * for the Company singleton. Includes importing, exporting, logging, saving,
 * and loading.
 * 
 * @author conor murphy
 *
 */
public class CompanyIO {
	/**
	 * Method that attempts to import a JSON file by parsing the file into a
	 * temporary file/warehouse if the file/warehouse is not empty it loops through
	 * all the shipments in the file and checks there validity then adds the
	 * shipment to the company. Outcome is logged.
	 * 
	 * @param file to be imported
	 * @throws Exception
	 */
	public static void importShipments(File file) throws Exception {
		Warehouse temp = CompanyIO.parseWarehouse(file);

		Company company = Company.getInstance();
		CompanyIO.log(String.format("importing shipments from %s", file.getName()));
		if (temp != null) { // if the .json was not empty
			for (Shipment s : temp.getWarehouse_contents()) {
				// Check shipment object for validity
				s.validate(temp); // temp values
				// validate(s); should replace any unparsed fields with default values
				company.addIncomingShipment(s);
			}
		} else {
			CompanyIO.log("import empty");
		}
	}

	public static void importShipments(String fileContent, String fileExtension) throws Exception {
		Warehouse temp = CompanyIO.parseWarehouse(fileContent, fileExtension);

		Company company = Company.getInstance();
		CompanyIO.log(String.format("importing shipments from %s", fileExtension));
		if (temp != null) { // if the .json was not empty
			for (Shipment s : temp.getWarehouse_contents()) {
				// Check shipment object for validity
				s.validate(temp); // temp values
				// validate(s); should replace any unparsed fields with default values
				company.addIncomingShipment(s);
			}
		} else {
			CompanyIO.log("import empty");
		}
	}

	/**
	 * Method that takes a warehouse id and gets an instance of a warehouse with
	 * that id
	 * 
	 * @param warehouseId id of warehouse being accessed
	 * @return warehouse that was accessed with specific id
	 */
	public static Warehouse getWarehouse(String warehouseId) {
		Company company = Company.getInstance();
		Warehouse warehouse = company.getWarehouse(warehouseId);

		return warehouse;
	}

	/**
	 * Method that exports the contents of a specific warehouse to a JSON file and
	 * logs the outocome
	 * 
	 * @param warehouse_id id of warehouse whose contents need to be exported
	 * @throws IOException
	 * @return
	 */
	public static String exportContentToJSON(String warehouse_id) throws IOException {
		Company company = Company.getInstance();
		Warehouse warehouse = company.getWarehouse(warehouse_id);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(warehouse);
		/*try (FileWriter writer = new FileWriter(String.format("%s.json", warehouse_id));) {
			writer.write(gson.toJson(warehouse));
			log(String.format("warehouse%s: exported to %s.json", warehouse_id, warehouse_id));
		}*/
	}

	/**
	 * Method used to log actions throughout the software
	 * 
	 * @param entry the string/message to be logged/displayed
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
	 * Private method that gets the file extention/type of a file
	 * 
	 * @param file whos extention/type needs to be found
	 * @return a string of the extention type
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
	 * Method that takes in a file, finds its extention, creates a new importer
	 * object based on the file type, then parses that file into a temporary
	 * warehouse
	 * 
	 * @param file that needs to be parsed
	 * @return file/temporary parsed warehouse
	 * @throws Exception
	 */
	private static Warehouse parseWarehouse(File file) throws Exception {
		String file_type = CompanyIO.getFileExtension(file);
		Warehouse temp;
		Importable importer;
		switch (file_type) {
		case ".json":
			importer = new ImporterJSON();
			temp = importer.parseWarehouse(file);
			break;
		case ".xml":
			importer = new ImporterXML();
			temp = importer.parseWarehouse(file);
			break;
		default:
			throw new Exception();
		}
		System.out.println("Warehouse ID " + temp.getWarehouse_id());
		return temp;
	}

	private static Warehouse parseWarehouse(String fileContent, String fileExtension) throws Exception {
		//String file_type = CompanyIO.getFileExtension(file);
		String file_type = fileExtension;
		System.out.println("File type is " + file_type);
		Warehouse temp;
		Importable importer;
		switch (file_type) {
			case "json":
				System.out.println("its json");
				importer = new ImporterJSON();
				temp = importer.parseWarehouse(fileContent);
				break;
			case "xml":
				System.out.println("its xml");
				importer = new ImporterXML();
				temp = importer.parseWarehouse(fileContent);
				break;
			default:
				throw new Exception();
		}
		System.out.println("Warehouse ID " + temp.getWarehouse_id());
		return temp;
	}

	/**
	 * Method that saves the state of the software. It gets the HashMap of
	 * warehouses within a company and writes them to a JSON and logs the outcome
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
	 * Method that loads the contents on the saved JSON file
	 * 
	 * @return HashMap of warehouses within the JSON
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
