package edu.metrostate.ics372_assignment3;

import android.app.Application;

import edu.metrostate.ics372_assignment3.model.Company;

public class WarehouseApplication extends Application {
    private Company company;
    private String currentWarehouseID;

    public Company getCompany(){
        return company;
    }

    public void setCompany(){
        company = Company.getInstance();
    }

    public void setCurrentWarehouseID(String currentWarehouseID){
        this.currentWarehouseID = currentWarehouseID;
    }

    public String getCurrentWarehouseID(){
        return currentWarehouseID;
    }
}
