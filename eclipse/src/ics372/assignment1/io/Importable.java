package ics372.assignment1.io;

import java.io.File;

import ics372.assignment1.model.Warehouse;

/**
 * Interface for classes that are capable of parsing a file of a specific format
 * into a warehouse object
 * 
 * @author conor
 *
 */
public interface Importable {
	public Warehouse parseWarehouse(File file) throws Exception;
}
