package edu.metrostate.ics372_assignment3.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.MainActivityMVP;

/**
 * Company class is a Singleton implementation of a controller class used to
 * handle all handle all operation with the model objects of a shiping company.
 *
 * @author tom
 */
public class Company implements MainActivityMVP.Model {

    private static Company company_instance = null;
    private HashMap<Integer, Warehouse> warehouses;

    /**
     * Company constructor that loads the state of the warehouses within a company
     */
    private Company(Context context) {
        warehouses = CompanyIO.loadState(context);
        CompanyIO.setCompany(this);
    }

    /**
     * method that synchronizes an instance of a company
     *
     * @return a new instance of a company
     */
    public static Company getInstance(Context context) {
        if (company_instance == null) {
            synchronized (Company.class) {
                if (company_instance == null) {
                    company_instance = new Company(context);
                }
            }
        }
        return company_instance;
    }

    /**
     * method that loops through all the warehouses within a company and adds the
     * warehouse ids to an ArrayList then returns the ArrayList of warehouse ids
     * within that company
     *
     * @return ids ArrayList of warehouse ids
     */
    public ArrayList<String> getWarehouseIds() {
        ArrayList<String> ids = new ArrayList<String>();
        warehouses.forEach((hash, warehouse) -> {
            ids.add(warehouse.getWarehouse_id());
        });
        return ids;
    }

    /**
     * method that takes in a warehouse id and returns the warehouse to which the
     * specified id is mapped
     *
     * @param warehouse_id of a specific warehouse
     * @return the warehouse associated with given warehouse id
     */
    protected Warehouse getWarehouse(String warehouse_id) {
        return warehouses.get(warehouse_id.hashCode());
    }

    protected HashMap<Integer, Warehouse> getWarehouses() {
        return warehouses;
    }

    /**
     * Method to attempt to add a shipment to a corresponding warehouse, or create a
     * new warehouse if one does not currently exist. log the outcome.
     *
     * @param shipment that is to be added
     * @return
     */
    public boolean addIncomingShipment(Shipment shipment) {
        Warehouse warehouse = this.getWarehouse(shipment.getWarehouse_id());
        if (warehouse != null) { // if the warehouse exists
            if (warehouse.addShipment(shipment)) { // if the add was successful
                CompanyIO.addShipment(shipment);
                CompanyIO.log(String.format("Shipment: %s added to Warehouse: %s", shipment.getShipment_id(),
                        warehouse.getWarehouse_id()));
                return true;
            } else { // otherwise log the failure
                CompanyIO.log(String.format("Shipment: %s denied at Warehouse: %s ", shipment.getShipment_id(),
                        warehouse.getWarehouse_id()));
                return false;
            }
        } else { // if the warehouse doesn't exist, create one and then attempt add again.
            // (necessary for importing)
            addWarehouse(shipment.getWarehouse_id());
            return addIncomingShipment(shipment);
        }
    }

    /**
     * Method used to remove a shipment from a warehouse. Remove the shipment if the
     * warehouse is found and is not null. Or else log an error message.
     *
     * @param shipment_id  shipment to be deleted
     * @param warehouse_id warehouse the contains shipment to be deleted
     */
    public boolean removeShipment(String shipment_id, String warehouse_id) {
        Warehouse warehouse = this.getWarehouse(warehouse_id);
        if (warehouse != null) {
            CompanyIO.removeShipment(shipment_id, warehouse_id);
            return warehouse.removeShipment(shipment_id);
        } else {
            CompanyIO.log(String.format("Warehouse: %s not found", warehouse_id));
            return false;
        }
    }

    public boolean moveShipment(String shipment_id, String warehouse_id_from, String warehouse_id_to) {
        Warehouse warehouse_from = this.getWarehouse(warehouse_id_from);
        Warehouse warehouse_to = this.getWarehouse(warehouse_id_from);
        if (warehouse_from != null && warehouse_to != null && warehouse_from.findShipment(shipment_id) != null) {
            Shipment s = warehouse_from.findShipment(shipment_id);
            s.setDeparture_date(System.currentTimeMillis());
            CompanyIO.updateShipment(s);

            Shipment s2 = (Shipment) s.clone();
            s2.setWarehouse_id(warehouse_id_to);
            s2.setReceipt_date(System.currentTimeMillis());
            s2.setDeparture_date((long) 0);
            CompanyIO.addShipment(s2);

            return true;
        } else {
            CompanyIO.log(String.format("Warehouse: %s not found", warehouse_id_from));
            return false;
        }
    }

    /**
     * Method used to add a new warehouse. log the outcome.
     *
     * @param warehouse_id of new warehouse to be added
     */
    public synchronized boolean addWarehouse(String warehouse_id) {
        Warehouse warehouse = new Warehouse(warehouse_id);

        if (warehouses.putIfAbsent(warehouse.hashCode(), warehouse) == null) { // if the previous value was null
            CompanyIO.addWarehouse(warehouse_id);
            CompanyIO.log(String.format("Warehouse: %s added to warehouse list", warehouse_id));
            return true;
        } else {
            CompanyIO.log(String.format("Warehouse: %s already on list", warehouse_id));
            return false;
        }
    }

    /**
     * Method that takes a warehouse id and removes that warehouse. log the outcome.
     *
     * @param warehouse_id of warehouse to remove
     */
    public boolean removeWarehouse(String warehouse_id) {
        if (warehouses.remove(warehouse_id.hashCode()) == null) {
            CompanyIO.removeWarehouse(warehouse_id);
            CompanyIO.log(String.format("Warehouse: %s removed from warehouse list", warehouse_id));
            return true;
        } else {
            CompanyIO.log(String.format("Warehouse: %s not on list", warehouse_id));
            return false;
        }
    }

    /**
     * Method that changes the reciveing status of a freight receipt
     * (on-to-off/off-to-on). log the outcome.
     *
     * @param warehouse_id used to find the receipt of a specific warehouse
     */
    public void toggleFreightReciept(String warehouse_id) {
        Warehouse warehouse = this.getWarehouse(warehouse_id);
        if (warehouse != null) {
            if (warehouse.isReceiving_freight()) {
                warehouse.setReceiving_freight(false);
                CompanyIO.updateWarehouse(warehouse_id, warehouse.getWarehouse_name(), false);
            } else {
                warehouse.setReceiving_freight(true);
                CompanyIO.updateWarehouse(warehouse_id, warehouse.getWarehouse_name(), true);
            }

            CompanyIO.log(String.format("Warehouse: %s freight status set to %b", warehouse_id,
                    warehouse.isReceiving_freight()));
        } else {
            CompanyIO.log(String.format("Warehouse: %s not found", warehouse_id));
        }
    }

    /**
     * Method the returns the status of a freight receipt (enabled/disabled). Return
     * the freight receipt status if the warehouse was found and is not null. Or
     * else log an error message.
     *
     * @param warehouse_id used to find the receipt of a specific warehouse
     * @return status of the freight receipt
     */
    public boolean getFreightReceiptStatus(String warehouse_id) {
        Warehouse warehouse = this.getWarehouse(warehouse_id);
        if (warehouse != null) {
            return warehouse.isReceiving_freight();
        } else {
            CompanyIO.log(String.format("Warehouse: %s not found, unable to toggle freight status", warehouse_id));
            return false;
        }
    }

    /**
     * public method to retrieve the name of a given warehouse. Return the warehouse
     * name if found and not null. Or else log and error message
     *
     * @param warehouse_id the id of the warehouse being searched for
     * @return the name of the warehouse as a string
     */
    public String getWarehouseName(String warehouse_id) {
        Warehouse warehouse = this.getWarehouse(warehouse_id);
        if (warehouse != null) {
            return warehouse.getWarehouse_name();
        } else {
            CompanyIO.log(String.format("Warehouse: %s not found, unable to retrieve name", warehouse_id));
            return "unnamed warehouse";
        }
    }

    /**
     * public method to change the name of a given warehouse. log the outcome
     *
     * @param warehouse_id   the id of the warehouse being renamed
     * @param warehouse_name the new name of the warehouse
     */
    public void setWarehouseName(String warehouse_id, String warehouse_name) {
        Warehouse warehouse = this.getWarehouse(warehouse_id);
        if (warehouse != null) {
            warehouse.setWarehouse_name(warehouse_name);
            CompanyIO.updateWarehouse(warehouse_id, warehouse_name, warehouse.isReceiving_freight());
            CompanyIO.log(String.format("Warehouse: %s name changed to %s", warehouse_id, warehouse_name));
        } else {
            CompanyIO.log(String.format("Warehouse: %s not found, unable to change name", warehouse_id));
        }
    }

    /**
     * Returns a HashMap of a warehouses contents.  Intended to be view only (cloned to protect the underlying model)
     *
     * @param warehouse_id the id of the warehouse
     * @return String of warehouse contents
     */
    public HashMap<String, Shipment> readWarehouseContent(String warehouse_id) {
        Warehouse warehouse = this.getWarehouse(warehouse_id);
        HashMap<String, Shipment> shipments_in_warehouse = new HashMap<String, Shipment>();
        if (warehouse != null) {
            for (Shipment shipment : warehouse.getWarehouse_contents()) {
                shipments_in_warehouse.put(shipment.getShipment_id(), (Shipment) shipment.clone());
            }
        }
        return shipments_in_warehouse;
    }
}
