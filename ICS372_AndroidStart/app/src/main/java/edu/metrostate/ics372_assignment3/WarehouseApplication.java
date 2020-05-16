package edu.metrostate.ics372_assignment3;

import android.app.Application;

import edu.metrostate.ics372_assignment3.model.Company;

public class WarehouseApplication extends Application {
    MainActivityMVP.Model model = null;

    public void setModel(){
        model = Company.getInstance(this);
    }

    public MainActivityMVP.Model getModel(){
        return model;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        model = Company.getInstance(this);
    }
}
