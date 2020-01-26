package ics372.assignment1.controller;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class Main {
	public static void main(String[] args) throws Exception {

		// parsing file "JSONExample.json"
		Object obj = new JSONParser().parse(new FileReader("example.json"));
		// typecasting obj to JSONObject
		JSONObject jo = (JSONObject) obj;

		// ShippingUI ui = new ShippingUI();
		////////////////////////
		Gson gson = new Gson();
		Object object = gson.fromJson(new FileReader("example.json"), Object.class);
		System.out.println(object.toString());
//		String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";
//
//		GsonBuilder builder = new GsonBuilder();
//		builder.setPrettyPrinting();
//
//		Gson gson2 = builder.create();
//		//Student student = gson2.fromJson(jsonString, Student.class);
//		System.out.println(student);
//
//		jsonString = gson.toJson(student);
//		System.out.println(jsonString);

	}
}
