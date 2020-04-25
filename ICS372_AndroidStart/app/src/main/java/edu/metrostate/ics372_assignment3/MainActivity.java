package edu.metrostate.ics372_assignment3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.CompanyIO;
import edu.metrostate.ics372_assignment3.model.Shipment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {

    private static final int CREATE_REQUEST_CODE = 40;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    private  Spinner spinner;
    private Button impButt, exportButt, addButt; // names are temporary but bad jokes last forever. buttons.
    ArrayAdapter<String> spinnerArrayAdapter;

    private WarehouseApplication application;
    private Company company;
    private List<String> warehouseIDs;


    private Button addShipmentButton, toggleActiveInactiveButton, toggleRecieptButton, editWarehouseButton;
    private TextView shipment_warehouse_id, shipment_shipment_id, shipment_method, shipment_weight, shipment_receipt, shipment_departure;
    private ListView shipmentList;
    private HashMap<String, Shipment> warehouse_contents;
    private ArrayAdapter adapter;


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
        //this.fillSampleData();

        // buttons
        impButt = findViewById(R.id.importButton);
        exportButt = findViewById(R.id.exportContentButton);
        addButt = findViewById(R.id.addWarehouseButton);
        addShipmentButton = findViewById(R.id.addShipmentButton);
        toggleActiveInactiveButton = findViewById(R.id.activeShipments);
        toggleRecieptButton = findViewById(R.id.receiptButton);
        editWarehouseButton = findViewById(R.id.editWarehouseButton);
        shipment_warehouse_id = findViewById(R.id.textViewWarehouseID);
        shipment_shipment_id = findViewById(R.id.textViewShipmentID);
        shipment_method = findViewById(R.id.textViewShipmentMethod);
        shipment_weight = findViewById(R.id.textViewShipmentWeight);
        shipment_receipt = findViewById(R.id.textViewReceipt);
        shipment_departure = findViewById(R.id.textViewDeparture);


        spinner = findViewById(R.id.spinner);
        warehouseIDs = new ArrayList<>();
        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, warehouseIDs);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        updateSpinnerArray();
        application.setCurrentWarehouseID(spinnerArrayAdapter.getItem(0));

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }

        addShipmentButton.setOnClickListener(this);
        toggleActiveInactiveButton.setOnClickListener(this);
        toggleRecieptButton.setOnClickListener(this);
        editWarehouseButton.setOnClickListener(this);
        impButt.setOnClickListener(this);
        exportButt.setOnClickListener(this);
        addButt.setOnClickListener(this);

        refreshShipmentList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSpinnerArray();
        refreshShipmentList();
    }

    public void updateSpinnerArray() {
        List<String> ids = company.getWarehouseIds();
        ids.forEach(i -> {
            if (!warehouseIDs.contains(i)) {
                warehouseIDs.add(i);
            }
        });
        spinnerArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        //code for onclick listeners to move code out of onCreate. Toasts for debugging.
        switch (v.getId()) {
            case R.id.importButton:
                Toast.makeText(this, "importButton pressed", Toast.LENGTH_SHORT).show();
                openFile(v);
                break;
            case R.id.exportContentButton:
                Toast.makeText(this, "export pressed", Toast.LENGTH_SHORT).show();
                exportContent(v);
                break;
            case R.id.addWarehouseButton:
                Intent intentAddWarehouse = new Intent(this, AddWarehouseActivity.class);
                startActivity(intentAddWarehouse);
                break;
            case R.id.addShipmentButton:
                Intent intent = new Intent(this, AddShipmentActivity.class);
                startActivity(intent);
                break;
            case R.id.activeShipments:
                Toast.makeText(this, "Shipment List Toggled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.receiptButton:
                Toast.makeText(this, "Reciept Status Toggled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editWarehouseButton:
                Intent intentEditWarehouse = new Intent(this, EditWarehouseActivity.class);
                startActivity(intentEditWarehouse);
                break;
        }
    }

    /**
     * Create an intent and set the file type parameters of
     *
     * @param view
     */
    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //https://developer.android.com/training/sharing/send
        //https://stackoverflow.com/questions/50386916/select-specific-file-types-using-action-get-content-and-settype-or-intent-extra
        String[] supportedMimeTypes = {"text/json", "text/xml"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(supportedMimeTypes.length == 1 ? supportedMimeTypes[0] : "*/*");
            if (supportedMimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
            }
        } else {
            String mimeTypes = "";
            for (String mimeType : supportedMimeTypes) {
                mimeTypes += mimeType + "|";
            }
            intent.setType(mimeTypes.substring(0, mimeTypes.length() - 1));
        }
        //intent.setType("text/json", "text/xml");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public void exportContent(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //https://developer.android.com/training/sharing/send
        intent.setType("text/json");
        intent.putExtra(Intent.EXTRA_TITLE, application.getCurrentWarehouseID() + ".json");
        startActivityForResult(intent, SAVE_REQUEST_CODE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri currentUri = null;
        Uri uri = new Uri.Builder().build();

        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SAVE_REQUEST_CODE) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    CompanyIO.saveContentToJSON(this, currentUri, application.getCurrentWarehouseID());
                }
            } else if (requestCode == OPEN_REQUEST_CODE) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    try {
                        String path = new File(resultData.getData().getPath()).getAbsolutePath();

                        if (path != null) {
                            System.out.println("Path is not null");
                            uri = resultData.getData();

                            String filename;
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                            if (cursor == null) filename = uri.getPath();
                            else {
                                cursor.moveToFirst();
                                int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                                filename = cursor.getString(idx);
                                cursor.close();
                            }
                            String name = filename.substring(filename.lastIndexOf("."));
                            String extension = filename.substring(filename.lastIndexOf(".") + 1);
                            String content = readFileContent(currentUri);
                            CompanyIO.importShipments(content, extension);
                            updateSpinnerArray();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private String readFileContent(Uri uri) throws IOException {
        InputStream inputStream =
                getContentResolver().openInputStream(uri);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inputStream.close();
        return stringBuilder.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String current_shipment_id = parent.getItemAtPosition(position).toString();
        refreshShipmentInfo(current_shipment_id);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        application.setCurrentWarehouseID(selected);
        refreshShipmentList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
