package ics372.assignment1.controller;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
	public static void main(String[] args) throws Exception {

		// parsing file "JSONExample.json"
		Object obj = new JSONParser().parse(new FileReader("example.json"));
		// typecasting obj to JSONObject
		JSONObject jo = (JSONObject) obj;

		ShippingUI ui = new ShippingUI();

	}
}
