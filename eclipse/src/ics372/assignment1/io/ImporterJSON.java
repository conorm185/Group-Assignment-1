package ics372.assignment1.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import ics372.assignment1.model.Warehouse;

public class ImporterJSON implements Importable {

	@Override
	public Warehouse parseWarehouse(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();
		Warehouse temp = new Warehouse("tempuse");
		temp = gson.fromJson(new FileReader(file), Warehouse.class);
		return temp;
	}

}
