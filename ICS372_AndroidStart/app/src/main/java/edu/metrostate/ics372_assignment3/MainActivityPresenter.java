package edu.metrostate.ics372_assignment3;

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
}
