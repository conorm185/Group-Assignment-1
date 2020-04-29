package edu.metrostate.ics372_assignment3.model;

import java.util.ArrayList;

/**
 * Warehouse class that holds a collection of shipments
 *
 * @author nicole
 */
public class Warehouse {
    private ArrayList<Shipment> warehouse_contents;
    private ArrayList<Shipment> warehouse_contents_inactive;
    private String warehouse_name;
    private String warehouse_id;
    private boolean receiving_freight;

    /**
     * Warehouse constructor
     *
     * @param warehouse_id the unique warehouse_id used to identify this warehouse
     */
    public Warehouse(String warehouse_id) {
        warehouse_contents = new ArrayList<Shipment>();
        warehouse_contents_inactive = new ArrayList<Shipment>();
        this.warehouse_id = (warehouse_id == null) ? "" : warehouse_id;
        this.receiving_freight = true;
    }

    /**
     * Default constructor
     */
    public Warehouse() {
        this(null);
    }

    /**
     * get the contents of this warehouse in the form of an ArrayList
     *
     * @return the warehouse_contents
     */
    public ArrayList<Shipment> getWarehouse_contents() {
        return warehouse_contents;
    }

    /**
     * set the contents of this warehouse in the form of an ArrayList.
     *
     * @param warehouse_contents the warehouse_contents to set
     */
    public void setWarehouse_contents(ArrayList<Shipment> warehouse_contents) {
        this.warehouse_contents = warehouse_contents;
    }

    /**
     * get the inactive contents of this warehouse in the form of an ArrayList
     *
     * @return the warehouse_contents_inactive a list of shipments in the warehouse
     */
    public ArrayList<Shipment> getWarehouse_contents_inactive() {
        return warehouse_contents_inactive;
    }

    /**
     * set the inactive contents of this warehouse in the form of an ArrayList.
     *
     * @param warehouse_contents_inactive the inactive shipments in this warehouse
     */
    public void setWarehouse_contents_inactive(ArrayList<Shipment> warehouse_contents_inactive) {
        this.warehouse_contents_inactive = warehouse_contents_inactive;
    }

    /**
     * get the name of this warehouse
     *
     * @return the warehouse_name
     */
    public String getWarehouse_name() {
        return warehouse_name;
    }

    /**
     * set the name of this warehouse
     *
     * @param warehouse_name the warehouse_name to set
     */
    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    /**
     * get the warehouse_id of this warehouse
     *
     * @return the warehouse_id
     */
    public String getWarehouse_id() {
        return warehouse_id;
    }

    /**
     * set the warehouse_id of this warehouse
     *
     * @param warehouse_id the warehouse_id to set
     */
    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = (warehouse_id == null) ? "" : warehouse_id;
    }

    /**
     * receiving flag for a new shipment
     *
     * @return the receiving_freight
     */
    public boolean isReceiving_freight() {
        return receiving_freight;
    }

    /**
     * set the freight reciept status of this warehouse
     *
     * @param receiving_freight the receiving_freight to be set
     */
    public void setReceiving_freight(boolean receiving_freight) {
        this.receiving_freight = receiving_freight;
    }

    /**
     * method used to add a shipment to the warehouse_contents collection
     *
     * @param shipment
     * @return true if shipment was added to the warehouse_contents, and false if
     * the shipment was not added
     */
    public boolean addShipment(Shipment shipment) {
        if (receiving_freight == true && !(warehouse_contents.contains(shipment))) {
            warehouse_contents.add(shipment);
            return true;
        } else {
            return false;
        }
    }

    /**
     * move the shipment from active to inactive
     *
     * @param shipment_id the unique id of the shipment being removed
     * @return true if the shipment was removed from the warehouse_contents, and
     * false if the shipment was not removed
     */
    public boolean deportShipment(String shipment_id) {
        for (Shipment s : warehouse_contents) {
            if (s.getShipment_id().equalsIgnoreCase(shipment_id)) {
                s.setDeparture_date(System.currentTimeMillis());
                warehouse_contents_inactive.add(s);
                warehouse_contents.remove(s);
                return true;
            }
        }
        return false;
    }

    /**
     * find an active shipment inside this warehouse
     *
     * @param shipment_id the shipment id of the shipment being searched for
     * @return the shipment if found, otherwise null
     */
    public Shipment findActiveShipment(String shipment_id) {
        for (Shipment s : warehouse_contents) {
            if (s.getShipment_id().equalsIgnoreCase(shipment_id)) {
                return s;
            }
        }
        return null;
    }

    /**
     * find an inactive shipment inside this warehouse
     *
     * @param shipment_id the shipment id of the shipment being searched for
     * @return the shipment if found, otherwise null
     */
    public Shipment findInactiveShipment(String shipment_id) {
        for (Shipment s : warehouse_contents_inactive) {
            if (s.getShipment_id().equalsIgnoreCase(shipment_id)) {
                return s;
            }
        }
        return null;
    }

    /**
     * method that returns a String of the contents within a warehouse
     *
     * @return outputString a string representation of this Warehouse
     */
    @Override
    public String toString() {
        String outputString = String.format(
                "\n\"warehouse_id\":\"%s\",\n" + "\"receiving_freight\":\"%s\",\n" + "\"warehouse_contents\":[\n",
                warehouse_id, receiving_freight);
        for (Shipment s : warehouse_contents) {
            outputString += String.format("{%s},", s.toString());
        }
        outputString.substring(0, outputString.length() - 1);// remove last comma
        outputString += "]";
        return outputString;
    }

    /**
     * Override of equals method. Two warehouses are equal if they have the same ID
     * (ignoring case)
     *
     * @return true if the two warehousees have the same warehouse_id, otherwise
     * false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Warehouse)) {
            return false;
        }
        Warehouse warehouse = (Warehouse) obj;
        return this.warehouse_id.equalsIgnoreCase(warehouse.warehouse_id);
    }

    /**
     * Override the hashcode method. Hash of the warehouse_id
     *
     * @return a hash of this warehouses unique warehouse_id
     */
    @Override
    public int hashCode() {
        return warehouse_id.hashCode();
    }
}
