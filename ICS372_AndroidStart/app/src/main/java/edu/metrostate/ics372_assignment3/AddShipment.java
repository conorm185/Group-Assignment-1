package edu.metrostate.ics372_assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Shipment.ShippingMethod;

public class AddShipment extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button submitButton, cancelButton;
    private Company company;
    WarehouseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipment);
        application = (WarehouseApplication) getApplication();
        company = application.getCompany();

        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.shipmentMethodSpinner);
        spinner.setAdapter(new ArrayAdapter<Shipment.ShippingMethod>(this, android.R.layout.simple_spinner_item, Shipment.ShippingMethod.values()));
        // Specify the layout to use when the list of choices appears
        // Apply the adapter to the spinner

        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.submitButton:
                addShipment(application.getCurrentWarehouseID());
                this.finish();
                //Toast.makeText(this, "submitButton pressed", Toast.LENGTH_SHORT).show();
                //submit()
                //addShipment(warehouse_id); // need a way to get warehouse id
                break;
            case R.id.cancelButton:
                this.finish();
                break;
        }
    }


    private void addShipment(String warehouse_id) {
        try {

            // get weight from EditText holding weight. may throw problems if nothing entered.
            EditText weightEdit = findViewById(R.id.weightEdit);
            String weightString = weightEdit.getText().toString();
            double weight = Double.parseDouble(weightString);

            // get selection from spinner
            Spinner mySpinner = (Spinner) findViewById(R.id.shipmentMethodSpinner);
            ShippingMethod method = (ShippingMethod) mySpinner.getSelectedItem();

            // get selection from EditText holding the shipment Id
            EditText shipIdEdit = findViewById(R.id.shipmentIdEdit);
            String shipment_id = shipIdEdit.getText().toString();

            Shipment this_shipment = new Shipment(warehouse_id, method, shipment_id, weight,
                    System.currentTimeMillis());

            if (company.addIncomingShipment(this_shipment)) {
                //log(String.format("shipment: %s added to warehouse: %s", shipment_id, warehouse_id));
            } else {
                //log(String.format("shipment: %s denied reciept at warehouse: %s", shipment_id, warehouse_id));
            }
        } catch (NumberFormatException e1) {
            //log(String.format("shipment denied invalid input."));
        } finally {
            //main_frame_add.dispose();
        }
    }

    // may be used a lot between activities.
    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // spinner interface.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "YOUR SELECTION IS : " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }
    // spinner interface.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
