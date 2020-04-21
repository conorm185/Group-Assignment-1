package edu.metrostate.ics372_assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.Shipment;

public class ViewWarehouseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Company company;
    private Button addShipmentButton, toggleActiveInactiveButton, toggleRecieptButton, editWarehouseButton;
    private ListView shipmentList;
    private HashMap<String, Shipment> warehouse_contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_warehouse);
        company = ((WarehouseApplication) this.getApplication()).getCompany();


        addShipmentButton = (Button) findViewById(R.id.submitButton);
        toggleActiveInactiveButton = (Button) findViewById(R.id.submitButton);
        toggleRecieptButton = (Button) findViewById(R.id.submitButton);
        editWarehouseButton = (Button) findViewById(R.id.submitButton);

        addShipmentButton.setOnClickListener(this);
        toggleActiveInactiveButton.setOnClickListener(this);
        toggleRecieptButton.setOnClickListener(this);
        editWarehouseButton.setOnClickListener(this);

        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.shipment_list_view, shipment_id_list);
        shipmentList = (ListView) findViewById(R.id.shipment_list_view);
        shipmentList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addShipmentButton:
                Toast.makeText(this, "Add Shipment Pressed", Toast.LENGTH_SHORT).show();
                //submit()
                //addShipment(warehouse_id); // need a way to get warehouse id
                break;
            case R.id.activeShipments:
                Toast.makeText(this, "Shipment List Toggled", Toast.LENGTH_SHORT).show();
                //openMainActivity(); // goes back to home screen. This may be used a lot between activities.
                break;
            case R.id.receiptButton:
                Toast.makeText(this, "Reciept Status Toggled", Toast.LENGTH_SHORT).show();
                //openMainActivity(); // goes back to home screen. This may be used a lot between activities.
                break;
            case R.id.editWarehouseButton:
                Toast.makeText(this, "Edit Warehouse Pressed", Toast.LENGTH_SHORT).show();
                //openMainActivity(); // goes back to home screen. This may be used a lot between activities.
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
