package ics372.assignment1.io;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ics372.assignment1.model.Shipment;
import ics372.assignment1.model.Warehouse;

/**
 * 
 * @author Gyan
 *
 */

public class ImporterXML implements Importable {


	@Override
	public Warehouse parseWarehouse(File file) {
		Warehouse warehouse = null;
		try {
			//source
			//https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList warehouseList = doc.getElementsByTagName("Warehouse");
			Node warehouseNode = warehouseList.item(0);
			if (warehouseNode.getNodeType() == Node.ELEMENT_NODE) {
				warehouse = new Warehouse();
				Element warehouseElement = (Element) warehouseNode;

				warehouse.setWarehouse_id(warehouseElement.getAttribute("id"));
				warehouse.setWarehouse_name(warehouseElement.getAttribute("name"));

				NodeList shipmentList = warehouseElement.getElementsByTagName("Shipment");

				for (int temp2 = 0; temp2 < shipmentList.getLength(); temp2++) {
					Node shipmentNode = shipmentList.item(temp2);
					if (shipmentNode.getNodeType() == Node.ELEMENT_NODE) {
						Element shipmentElement = (Element) shipmentNode;
						Shipment shipment = new Shipment();	                    	
						String shippingmethod = shipmentElement.getAttribute("type");
						if(shippingmethod.equalsIgnoreCase("rail")) {
							shipment.setShipment_method(Shipment.ShippingMethod.rail);
						}
						else if(shippingmethod.equalsIgnoreCase("ship")) {
							shipment.setShipment_method(Shipment.ShippingMethod.ship);
						}
						else if(shippingmethod.equalsIgnoreCase("air")) {
							shipment.setShipment_method(Shipment.ShippingMethod.air);
						}
						else if(shippingmethod.equalsIgnoreCase("truck")) {
							shipment.setShipment_method(Shipment.ShippingMethod.truck);
						}
						System.out.println("Shipment ID is : " 
								+ shipmentElement.getAttribute("id"));
						shipment.setShipment_id(shipmentElement.getAttribute("id"));

						NodeList weightDetails = shipmentElement.getElementsByTagName("Weight");
						Node weightNode = weightDetails.item(0);
						if (weightNode.getNodeType() == Node.ELEMENT_NODE) {
							Element weightElement = (Element) weightNode;
							String unit = weightElement.getAttribute("unit");
							double weight = Double.parseDouble(weightElement.getTextContent());
							
							//checking if the weight is in the kg then change it to the pound
							if(unit.equalsIgnoreCase("kg")) {
								weight = weight * 2.2;
							}
							shipment.setWeight(weight);
						}

						// get receipt date if it exists
						
						NodeList receiptDateDetails = shipmentElement.getElementsByTagName("ReceiptDate");          
						if(receiptDateDetails.getLength() > 0) {
							Node receiptDateNode = receiptDateDetails.item(0);
							if (receiptDateNode.getNodeType() == Node.ELEMENT_NODE) {
								Element receiptElement = (Element) receiptDateNode;		
								shipment.setReceipt_date(Long.parseLong(receiptElement.getTextContent()));
							}
						}
						shipment.setWarehouse_id(warehouse.getWarehouse_id());
						warehouse.addShipment(shipment);
					}        	
				}

			}

			//System.out.println(warehouse.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return warehouse;
	}

}
