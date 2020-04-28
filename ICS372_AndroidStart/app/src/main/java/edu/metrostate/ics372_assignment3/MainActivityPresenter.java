package edu.metrostate.ics372_assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;

/**
 * The presenter triggers the business logic and tells the view when to update. It therefore
 * interacts with the model and fetches and transforms data from the model to update the view.
 * The presenter should not have, if possible, a dependency to the Android SDK.
 */
public class MainActivityPresenter implements MainActivityMVP.Presenter {
    private MainActivityMVP.View view;
    private MainActivityMVP.Model model; //this is the company

    public MainActivityPresenter(MainActivityMVP.Model model) {
        this.model = model;
    }

    public void setView(MainActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void addShipmentCompleted(Shipment shipment) {
        model.addIncomingShipment(shipment);
        HashMap<String, Shipment> warehouse_contents = model.readWarehouseContent(shipment.getWarehouse_id());
        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
        view.showShipments(shipment_id_list);
    }

    @Override
    public void addWarehouseCompleted(Warehouse warehouse) {
        model.addWarehouse(warehouse.getWarehouse_id());
        model.setWarehouseName(warehouse.getWarehouse_id(), warehouse.getWarehouse_name());
        view.showWarehouses(model.getWarehouseIds());
    }

    @Override
    public void editWarehouseCompleted(Warehouse warehouse) {
        model.setWarehouseName(warehouse.getWarehouse_id(), warehouse.getWarehouse_name());
        view.showWarehouses(model.getWarehouseIds());
    }

    @Override
    public void addWarehouseClicked() {
        view.showAddNewWarehouse();
    }

    @Override
    public void addShipmentClicked() {
        view.showAddNewShipment();
    }

    @Override
    public void editWarehouseClicked() {
        view.showEditWarehouse();
    }

    @Override
    public void moveShipmentClicked() {
        view.showMoveShipment();
    }

    @Override
    public void moveShipmentCompleted(String current_shipment_id, String current_warehouse_id, String target_warehouse_id) {
        model.moveShipment(current_shipment_id, current_warehouse_id, target_warehouse_id);
        HashMap<String, Shipment> warehouse_contents = model.readWarehouseContent(current_warehouse_id);
        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
        view.showShipments(shipment_id_list);
    }

    @Override
    public List<String> getWarehouseIds() {
        ArrayList<String> ids = model.getWarehouseIds();
        Collections.sort(ids);
        return ids;
    }

    @Override
    public HashMap<String, Shipment> readWarehouseContent(String current_warehouse_id) {
        return model.readWarehouseContent(current_warehouse_id);
    }

    @Override
    public String getWarehouseName(String current_warehouse_id) {
        return model.getWarehouseName(current_warehouse_id);
    }

    @Override
    public void toggleFreightReciept(String current_warehouse_id) {
        model.toggleFreightReciept(current_warehouse_id);
    }

    @Override
    public boolean getFreightReceiptStatus(String current_warehouse_id) {
        return model.getFreightReceiptStatus(current_warehouse_id);
    }
}
