package edu.metrostate.ics372_assignment3.model;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.DB.DBHelper;
import edu.metrostate.ics372_assignment3.io.Importable;
import edu.metrostate.ics372_assignment3.io.ImporterJSON;
import edu.metrostate.ics372_assignment3.io.ImporterXML;

/**
 * Class filled with static methods to handle all file input/output operations
 * for the Company singleton. Includes importing, exporting, logging, saving,
 * and loading.
 *
 * @author conor murphy
 */
public class CompanyIO {
    private static DBHelper database;
    private static Company company;


    public static void importShipments(Uri uri) throws Exception {
        File file = new File(uri.getPath());
        CompanyIO.importShipments(file);
    }

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

	//remove
	public static void importShipments(String fileContent, String fileExtension) throws Exception {
		Warehouse temp = CompanyIO.parseWarehouse(fileContent, fileExtension);

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
	public static String exportContentToJSON(String warehouse_id)  {
		Warehouse warehouse = company.getWarehouse(warehouse_id);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(warehouse);
	}

	public static void saveContentToJSON(Context context, Uri fileUri, String warehouse_id) {
        String warehouseJsonText = CompanyIO.exportContentToJSON(warehouse_id);
        try{
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(fileUri, "rwt");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(warehouseJsonText.getBytes());
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to log actions throughout the software
     *
     * @param entry the string/message to be logged/displayed
     */
    public static void log(String entry) {
        Log.e("tag", entry);
        /*File company_log = new File("company_log.txt");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(company_log, true))) {
            String log_entry = String.format("[%s]\t[%s]\n", formatter.format(now), entry);
            writer.write(log_entry);
            System.out.print(log_entry);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Private method that gets the file extention/type of a file
     *
     * @param file whos extention/type needs to be found
     * @return a string of the extention type
     */
    private static String getFileExtension(File file) {
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
        return temp;
    }

    private static Warehouse parseWarehouse(String fileContent, String fileExtension) throws Exception {
        String file_type = fileExtension;
        Warehouse temp;
        Importable importer;
        switch (file_type) {
            case "json":
                importer = new ImporterJSON();
                temp = importer.parseWarehouse(fileContent);
                break;
            case "xml":
                importer = new ImporterXML();
                temp = importer.parseWarehouse(fileContent);
                break;
            default:
                throw new Exception();
        }
        return temp;
    }

    public static void setCompany(Company company){
        CompanyIO.company = company;
    }

    /**
     * Method that loads the contents on the saved JSON file
     *
     * @param context
     * @return HashMap of warehouses within the JSON
     */
    protected static HashMap<Integer, Warehouse> loadState(Context context) {
        database = new DBHelper(context);
        return database.getAllWarehouses();
    }

    public static void addShipment(Shipment shipment) {
        database.insertShipment(shipment.getShipment_id(), shipment.getWarehouse_id()
                , shipment.getShipment_method().toString(), shipment.getWeight()
                , shipment.getReceipt_date(), 0);
    }

    public static void removeShipment(String shipment_id, String warehouse_id) {
        database.deleteShipment(shipment_id, warehouse_id);
    }

    public static void addWarehouse(String warehouse_id) {
        database.insertWarehouse(warehouse_id);
    }

    public static void removeWarehouse(String warehouse_id) {
        database.deleteWarehouse(warehouse_id);
    }

    public static void updateWarehouse(String warehouse_id, String warehouse_name, boolean b) {
        database.updateWarehouse(warehouse_id, warehouse_name, b);
    }

    public static void updateShipment(Shipment shipment) {
        database.updateShipment(shipment.getShipment_id(), shipment.getWarehouse_id()
                , shipment.getShipment_method().toString(), shipment.getWeight()
                , shipment.getReceipt_date(), shipment.getDeparture_date());
    }
}
