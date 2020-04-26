package edu.metrostate.ics372_assignment3;

import java.util.ArrayList;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;

public interface MainActivityMVP {

    /**
     * A view component in MVP contains a visual part of the application.
     * <p>
     * It contains only the UI and it does not contain any logic or knowledge of the data displayed.
     * In typical implementations the view components in MVP exports an interface that is used by
     * the Presenter. The presenter uses these interface methods to manipulate the view. Example
     * method names would be: showProgressBar, updateData
     */
    interface View {
        void showAddNewShipment();
        void showAddNewWarehouse();
        void showEditWarehouse();

        void showShipments(String[] shipment_id_list);

        void showWarehouses(ArrayList<String> warehouseIds);
    }

    /**
     * The presenter triggers the business logic and tells the view when to update. It therefore
     * interacts with the model and fetches and transforms data from the model to update the view.
     * The presenter should not have, if possible, a dependency to the Android SDK
     */
    interface Presenter {
        void setView(View view);

        void addShipmentCompleted(Shipment shipment);

        void addWarehouseCompleted(Warehouse warehouse);

        void editWarehouseCompleted(Warehouse warehouse);

        void addWarehouseClicked();

        void addShipmentClicked();

        void editWarehouseClicked();
    }

    /**
     * Contains a data provider and the code to fetch and update the data.
     * This part of MVP updates the database or communicate with a webserve
     */
    interface Model {
        ArrayList<String> getWarehouseIds();

        boolean addIncomingShipment(Shipment shipment);

        boolean removeShipment(String shipment_id, String warehouse_id);

        boolean moveShipment(String shipment_id, String warehouse_id_from, String warehouse_id_to);

        boolean addWarehouse(String warehouse_id);

        boolean removeWarehouse(String warehouse_id);

        void toggleFreightReciept(String warehouse_id);

        boolean getFreightReceiptStatus(String warehouse_id);

        String getWarehouseName(String warehouse_id);

        void setWarehouseName(String warehouse_id, String warehouse_name);

        HashMap<String, Shipment> readWarehouseContent(String warehouse_id);
    }

}
