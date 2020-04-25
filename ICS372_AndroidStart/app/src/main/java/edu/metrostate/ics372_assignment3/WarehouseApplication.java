package edu.metrostate.ics372_assignment3;

import android.app.Application;
import edu.metrostate.ics372_assignment3.model.Company;

public class WarehouseApplication extends Application {
    private Company company = null;
    private String currentWarehouseID = null;

    public Company getCompany() {
        return company;
    }

    public void setCompany() {
        company = Company.getInstance(this);
    }

    public String getCurrentWarehouseID() {
        return currentWarehouseID;
    }

    public void setCurrentWarehouseID(String currentWarehouseID) {
        this.currentWarehouseID = currentWarehouseID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        company = Company.getInstance(this);
    }
}
