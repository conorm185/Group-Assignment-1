package ics372.assignment1.io;

import java.io.File;

import ics372.assignment1.model.Warehouse;

public interface Importable {
	public Warehouse parseWarehouse(File file) throws Exception;
}
