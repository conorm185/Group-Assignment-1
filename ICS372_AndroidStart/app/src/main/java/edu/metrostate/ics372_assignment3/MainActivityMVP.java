package edu.metrostate.ics372_assignment3;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.CompanyIO;
import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;

public interface MainActivityMVP {

    /**
     * A view component in MVP contains a visual part of the application.
     *
     * It contains only the UI and it does not contain any logic or knowledge of the data displayed.
     * In typical implementations the view components in MVP exports an interface that is used by
     * the Presenter. The presenter uses these interface methods to manipulate the view. Example
     * method names would be: showProgressBar, updateData
     */
    interface View {
        /*void showBooks(List<Book> books);
        void showAddNewBook();*/
    }

    /**
     * The presenter triggers the business logic and tells the view when to update. It therefore
     * interacts with the model and fetches and transforms data from the model to update the view.
     * The presenter should not have, if possible, a dependency to the Android SDK
     */
    interface Presenter {
        void setView(View view);


        /*void addBookClicked();
        void addBookCompleted(String author, String title);*/
    }

    /**
     * Contains a data provider and the code to fetch and update the data.
     * This part of MVP updates the database or communicate with a webserve
     */
    interface Model {
        public ArrayList<String> getWarehouseIds();

        // these methods are internal
        //protected Warehouse getWarehouse(String warehouse_id);
        //protected HashMap<Integer, Warehouse> getWarehouses();
        //private Company(Context context);

        public boolean addIncomingShipment(Shipment shipment);

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
