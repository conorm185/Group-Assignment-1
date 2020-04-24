package edu.metrostate.ics372_assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.Shipment;

public class ViewWarehouseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Company company;
    private Button addShipmentButton, toggleActiveInactiveButton, toggleRecieptButton, editWarehouseButton;
    private TextView warehouse_id, warehouse_name, shipment_warehouse_id, shipment_shipment_id, shipment_method, shipment_weight, shipment_receipt, shipment_departure;
    private ListView shipmentList;
    private HashMap<String, Shipment> warehouse_contents;
    private WarehouseApplication application;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_warehouse);

        application = (WarehouseApplication) getApplication();
        company = application.getCompany();

        addShipmentButton = findViewById(R.id.addShipmentButton);
        toggleActiveInactiveButton = findViewById(R.id.activeShipments);
        toggleRecieptButton = findViewById(R.id.receiptButton);
        editWarehouseButton = findViewById(R.id.editWarehouseButton);

        warehouse_id = findViewById(R.id.warehouseIDText);
        warehouse_name = findViewById(R.id.warehouseNameText);
        shipment_warehouse_id = findViewById(R.id.textViewWarehouseID);
        shipment_shipment_id = findViewById(R.id.textViewShipmentID);
        shipment_method = findViewById(R.id.textViewShipmentMethod);
        shipment_weight = findViewById(R.id.textViewShipmentWeight);
        shipment_receipt = findViewById(R.id.textViewReceipt);
        shipment_departure = findViewById(R.id.textViewDeparture);

        warehouse_id.setText(String.format("Warehouse ID: %s", application.getCurrentWarehouseID()));
        warehouse_name.setText(String.format("Name: %s", company.getWarehouseName(application.getCurrentWarehouseID())));

        addShipmentButton.setOnClickListener(this);
        toggleActiveInactiveButton.setOnClickListener(this);
        toggleRecieptButton.setOnClickListener(this);
        editWarehouseButton.setOnClickListener(this);

        refreshShipmentList();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshShipmentList();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addShipmentButton:
                Intent intent = new Intent(this, AddShipmentActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "Add Shipment Pressed", Toast.LENGTH_SHORT).show();
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
                Intent intentEditWarehouse = new Intent(this, EditWarehouseActivity.class);
                startActivity(intentEditWarehouse);
                break;
        }
    }

    public void refreshShipmentList() {
        warehouse_contents = company.readWarehouseContent(application.getCurrentWarehouseID());
        String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
        adapter = new ArrayAdapter<String>(this, R.layout.shipment_list_view, shipment_id_list);
        shipmentList = findViewById(R.id.shipment_list_view);
        shipmentList.setAdapter(adapter);
        shipmentList.setOnItemClickListener(this::onItemClick);
    }

    public void refreshShipmentInfo(String shipment_id) {
        Shipment current_shipment = warehouse_contents.get(shipment_id);
        Date receipt = new Date(current_shipment.getReceipt_date());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        shipment_warehouse_id.setText(current_shipment.getWarehouse_id());
        shipment_shipment_id.setText(current_shipment.getShipment_id());
        shipment_method.setText(current_shipment.getShipment_method().toString());
        shipment_weight.setText(String.format("%.2f lbs", current_shipment.getWeight()));
        shipment_receipt.setText(formatter.format(receipt));
        shipment_departure.setText("N/A");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String current_shipment_id = parent.getItemAtPosition(position).toString();
        refreshShipmentInfo(current_shipment_id);
    }
}
