package edu.metrostate.ics372_assignment3.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;

/**
 * Class that parses an XML file into a warehouse filled with shipments to be
 * imported.
 *
 * @author Gyan
 */

public class ImporterXML implements Importable {

    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * method to parse a file into a usable warehouse object
     *
     * @param file the file object to be parsed
     * @return warehouse a warehouse object used as a container for a list of
     * shipments to be imported into the Company object.
     */
    @Override
    public Warehouse parseWarehouse(File file) {
        Warehouse warehouse = null;
        try {
            // source
            // https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm

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
                        if (shippingmethod.equalsIgnoreCase("rail")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.rail);
                        } else if (shippingmethod.equalsIgnoreCase("ship")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.ship);
                        } else if (shippingmethod.equalsIgnoreCase("air")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.air);
                        } else if (shippingmethod.equalsIgnoreCase("truck")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.truck);
                        }
                        System.out.println("Shipment ID is : " + shipmentElement.getAttribute("id"));
                        shipment.setShipment_id(shipmentElement.getAttribute("id"));

                        NodeList weightDetails = shipmentElement.getElementsByTagName("Weight");
                        Node weightNode = weightDetails.item(0);
                        if (weightNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element weightElement = (Element) weightNode;
                            String unit = weightElement.getAttribute("unit");
                            double weight = Double.parseDouble(weightElement.getTextContent());

                            // checking if the weight is in the kg then change it to the pound
                            if (unit.equalsIgnoreCase("kg")) {
                                weight = weight * 2.2;
                            }
                            shipment.setWeight(weight);
                        }

                        // get receipt date if it exists

                        NodeList receiptDateDetails = shipmentElement.getElementsByTagName("ReceiptDate");
                        if (receiptDateDetails.getLength() > 0) {
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

            // System.out.println(warehouse.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return warehouse;
    }

    /**
     * Mothod to parse an XML string into a useable warehouse object
     * @param fileContent a string in XML format
     * @return  warehouse, a warehouse filled with every shipment in the XML file
     * @throws Exception
     */
    @Override
    public Warehouse parseWarehouse(String fileContent) throws Exception {
        Warehouse warehouse = null;
        try {
            //DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            //DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = convertStringToXMLDocument(fileContent);
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
                        if (shippingmethod.equalsIgnoreCase("rail")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.rail);
                        } else if (shippingmethod.equalsIgnoreCase("ship")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.ship);
                        } else if (shippingmethod.equalsIgnoreCase("air")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.air);
                        } else if (shippingmethod.equalsIgnoreCase("truck")) {
                            shipment.setShipment_method(Shipment.ShippingMethod.truck);
                        }
                        System.out.println("Shipment ID is : " + shipmentElement.getAttribute("id"));
                        shipment.setShipment_id(shipmentElement.getAttribute("id"));

                        NodeList weightDetails = shipmentElement.getElementsByTagName("Weight");
                        Node weightNode = weightDetails.item(0);
                        if (weightNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element weightElement = (Element) weightNode;
                            String unit = weightElement.getAttribute("unit");
                            double weight = Double.parseDouble(weightElement.getTextContent());

                            // checking if the weight is in the kg then change it to the pound
                            if (unit.equalsIgnoreCase("kg")) {
                                weight = weight * 2.2;
                            }
                            shipment.setWeight(weight);
                        }

                        // get receipt date if it exists

                        NodeList receiptDateDetails = shipmentElement.getElementsByTagName("ReceiptDate");
                        if (receiptDateDetails.getLength() > 0) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return warehouse;
    }

}
