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

    /**
     * construct this presenter and assign a model object to it
     *
     * @param model
     */
    public MainActivityPresenter(MainActivityMVP.Model model) {
        this.model = model;
    }

    /**
     * set the view that is associated with this presenter
     *
     * @param view
     */
    public void setView(MainActivityMVP.View view) {
        this.view = view;
    }

    /**
     * when the add warehouse command is clicked tell the view to display the appropriate dialog
     */
    @Override
    public void addWarehouseClicked() {
        view.showAddNewWarehouse();
    }

    /**
     * when the add warehouse command is clicked tell the view to display the appropriate dialog
     */
    @Override
    public void addShipmentClicked() {
        view.showAddNewShipment();
    }

    /**
     * when the add warehouse command is clicked tell the view to display the appropriate dialog
     */
    @Override
    public void editWarehouseClicked() {
        view.showEditWarehouse();
    }

    /**
     * when the add warehouse command is clicked tell the view to display the appropriate dialog
     */
    @Override
    public void moveShipmentClicked() {
        view.showMoveShipment();
    }

    /**
     * when a add shipment dialog has been completed submit it to the model and send a refresh command to the view
     *
     * @param shipment
     */
    @Override
    public void addShipmentCompleted(Shipment shipment) {
        model.addIncomingShipment(shipment);
        HashMap<String, Shipment> warehouse_contents = model.readWarehouseContent(shipment.getWarehouse_id());
        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
        view.showShipments(shipment_id_list);
    }

    /**
     * when a add warehouse dialog is completed, submit it to the model and send a refresh command to the view
     *
     * @param warehouse
     */
    @Override
    public void addWarehouseCompleted(Warehouse warehouse) {
        model.addWarehouse(warehouse.getWarehouse_id());
        model.setWarehouseName(warehouse.getWarehouse_id(), warehouse.getWarehouse_name());
        view.showWarehouses(model.getWarehouseIds());
    }

    /**
     * when a edit warehouse dialog is completed, submit it to the model and send a refresh command to the view4
     *
     * @param warehouse
     */
    @Override
    public void editWarehouseCompleted(Warehouse warehouse) {
        model.setWarehouseName(warehouse.getWarehouse_id(), warehouse.getWarehouse_name());
        view.showWarehouses(model.getWarehouseIds());
    }

    /**
     * when a move shipment command is completed, submit it to the model and send a refresh command to the view
     *
     * @param current_shipment_id
     * @param current_warehouse_id
     * @param target_warehouse_id
     */
    @Override
    public void moveShipmentCompleted(String current_shipment_id, String current_warehouse_id, String target_warehouse_id) {
        model.moveShipment(current_shipment_id, current_warehouse_id, target_warehouse_id);
        HashMap<String, Shipment> warehouse_contents = model.readWarehouseContent(current_warehouse_id);
        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
        view.showShipments(shipment_id_list);
    }

    /**
     * get all warehouse id's from the model
     *
     * @return
     */
    @Override
    public List<String> getWarehouseIds() {
        ArrayList<String> ids = model.getWarehouseIds();
        Collections.sort(ids);
        return ids;
    }

    /**
     * read the content of a single warehouse inside the model
     *
     * @param current_warehouse_id
     * @return
     */
    @Override
    public HashMap<String, Shipment> readWarehouseContent(String current_warehouse_id) {
        return model.readWarehouseContent(current_warehouse_id);
    }

    /**
     * get the name of a single warehouse from the model
     *
     * @param current_warehouse_id
     * @return
     */
    @Override
    public String getWarehouseName(String current_warehouse_id) {
        return model.getWarehouseName(current_warehouse_id);
    }

    /**
     * toggle the freight reciept status of a warehouse
     *
     * @param current_warehouse_id
     */
    @Override
    public void toggleFreightReciept(String current_warehouse_id) {
        model.toggleFreightReciept(current_warehouse_id);
    }

    /**
     * get the freight reciept status of a warehouse
     *
     * @param current_warehouse_id
     * @return
     */
    @Override
    public boolean getFreightReceiptStatus(String current_warehouse_id) {
        return model.getFreightReceiptStatus(current_warehouse_id);
    }
}
