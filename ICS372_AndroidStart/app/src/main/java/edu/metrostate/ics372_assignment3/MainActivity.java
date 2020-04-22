package edu.metrostate.ics372_assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.Shipment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button impButt, shipButt, receiptButt, viewButt, exportButt, addButt; // names are temporary but bad jokes last forever. buttons.

    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;

    private WarehouseApplication application;
    private Company company;

    /**
     * Creates the view for the application
     *
     * @param savedInstanceState saved state information for the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (WarehouseApplication) getApplication();
        application.setCompany();
        company = application.getCompany();
        this.fillSampleData();

        // buttons
        impButt = (Button) findViewById(R.id.importButton);
        shipButt = (Button) findViewById(R.id.addShipButton);
        receiptButt = (Button) findViewById(R.id.receiptButton);
        viewButt = (Button) findViewById(R.id.viewWarehouseButton);
        exportButt = (Button) findViewById(R.id.exportContentButton);
        addButt = (Button) findViewById(R.id.addWarehouseButton);

        impButt.setOnClickListener(this);
        shipButt.setOnClickListener(this);
        receiptButt.setOnClickListener(this);
        viewButt.setOnClickListener(this);
        exportButt.setOnClickListener(this);
        addButt.setOnClickListener(this);

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }


    }


    @Override
    public void onClick(View v) {
        //code for onclick listeners to move code out of onCreate. Toasts for debugging.
        switch (v.getId()) {
            case R.id.importButton:
                Toast.makeText(this, "importButton pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addShipButton:
                Toast.makeText(this, "addShip pressed", Toast.LENGTH_SHORT).show();
                openAddShipmentActivity();
                break;
            case R.id.receiptButton:
                Toast.makeText(this, "receiptButton pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.viewWarehouseButton:
                ((WarehouseApplication) this.getApplication()).setCurrentWarehouseID("4321");
                Intent intent = new Intent(this, ViewWarehouseActivity.class);
                startActivity(intent);

                //Toast.makeText(this, "view warehouse pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exportContentButton:
                Toast.makeText(this, "export pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.addWarehouseButton:
                Toast.makeText(this, "add warehouse pressed", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // opens Activity to add a shipment. Used by addShipButton.
    public void openAddShipmentActivity() {
        Intent intent = new Intent(this, AddShipment.class);
        startActivity(intent);
    }

    /**
     * Indicates when the user has responded to a permission request
     *
     * @param requestCode  The request code
     * @param permissions  The permissions requested
     * @param grantResults The result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission required, closing application", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    private void fillSampleData() {
        company.addWarehouse("4321");
        Shipment s1 = new Shipment("4321", Shipment.ShippingMethod.air,"444",5, (long) 1732279329);
        Shipment s2 = new Shipment("4321", Shipment.ShippingMethod.air,"443",5, (long) 1732279329);
        Shipment s3 = new Shipment("4321", Shipment.ShippingMethod.air,"442",5, (long) 1732279329);
        Shipment s4 = new Shipment("4321", Shipment.ShippingMethod.air,"441",5, (long) 1732279329);
        company.addIncomingShipment(s1);
        company.addIncomingShipment(s2);
        company.addIncomingShipment(s3);
        company.addIncomingShipment(s4);
    }
}
