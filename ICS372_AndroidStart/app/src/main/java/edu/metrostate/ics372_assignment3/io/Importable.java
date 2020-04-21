package edu.metrostate.ics372_assignment3.io;

import java.io.File;

import edu.metrostate.ics372_assignment3.model.Warehouse;

/**
 * Interface for classes that are capable of parsing a file of a specific format
 * into a warehouse object
 * 
 * @author conor
 *
 */
public interface Importable {
	public Warehouse parseWarehouse(File file) throws Exception;
	public Warehouse parseWarehouse(String fileContent) throws Exception;
}
