package ics372.assignment1.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Info {

	List<Warehouse> warehouse_contents;

	public List<Warehouse> getWarehouse_contents() {
		return warehouse_contents;
	}

	public void setWarehouse_contents(List<Warehouse> warehouse_contents) {
		this.warehouse_contents = warehouse_contents;
	}

	public static void main(String[] args) {
		File file = new File("example.json");
		//Company tester = new Company();
		//tester.importShipments(file);

		Gson gson = new Gson();
		Object object = null;
		try {
			object = gson.fromJson(new FileReader(file), Object.class);
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(object);
		
		String json = gson.toJson(object);
		
		Info test = gson.fromJson(json, Info.class);
		
		System.out.println(test);
	}
}
