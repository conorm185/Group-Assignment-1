package edu.metrostate.ics372_assignment3.io;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.metrostate.ics372_assignment3.model.Warehouse;

/**
 * Class that parses a JSON file into a warehouse filled with shipments to be
 * imported.
 *
 * @author conor murphy
 */
public class ImporterJSON implements Importable {

    /**
     * method to parse a file into a usable warehouse object
     *
     * @param file the file object to be parsed
     * @return temp, a warehouse object used as a container for a list of shipments
     * to be imported into the Company object.
     */
    @Override
    public Warehouse parseWarehouse(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Gson gson = new Gson();
        Warehouse temp = new Warehouse("tempuse");
        temp = gson.fromJson(new FileReader(file), Warehouse.class);
        return temp;
    }

    /**
     * Method to parse a files content(as a string) into a useable warehouse
     *
     * @param fileContent a string of the entire file
     * @return temp, a warehouse filled with every imported shipment
     * @throws JsonSyntaxException
     * @throws JsonIOException
     */
    @Override
    public Warehouse parseWarehouse(String fileContent) throws JsonSyntaxException, JsonIOException {
        Gson gson = new Gson();
        Warehouse temp = new Warehouse("tempuse");
        temp = gson.fromJson(fileContent, Warehouse.class);
        return temp;
    }
}
