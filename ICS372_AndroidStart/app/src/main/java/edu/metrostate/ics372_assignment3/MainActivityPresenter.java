package edu.metrostate.ics372_assignment3;

import java.util.HashMap;

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
    public void addShipmentCompleted(Shipment shipment){
        model.addIncomingShipment(shipment);
        HashMap<String, Shipment> warehouse_contents = model.readWarehouseContent(shipment.getWarehouse_id());
        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
        view.showShipments(shipment_id_list);
    }

    @Override
    public void addWarehouseCompleted(Warehouse warehouse){
        model.addWarehouse(warehouse.getWarehouse_id());
        model.setWarehouseName(warehouse.getWarehouse_id(),warehouse.getWarehouse_name());
        view.showWarehouses(model.getWarehouseIds());
    }

    @Override
    public void editWarehouseCompleted(Warehouse warehouse){
        model.setWarehouseName(warehouse.getWarehouse_id(),warehouse.getWarehouse_name());
        view.showWarehouses(model.getWarehouseIds());
    }
}
