package edu.metrostate.ics372_assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.Shipment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button impButt, shipButt, receiptButt, viewButt, exportButt, addButt; // names are temporary but bad jokes last forever. buttons.

    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;

    private WarehouseApplication application;
    private Company company;
    private static TextView textView;
    private static Spinner spinner;
    List<String> warehouseIDs;
    ArrayAdapter<String> spinnerArrayAdapter;

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

        textView = findViewById(R.id.TextView);
        //https://mkyong.com/android/android-spinner-drop-down-list-example/
        //https://android--code.blogspot.com/2015/08/android-spinner-add-item-dynamically.html
        spinner = findViewById(R.id.spinner);
        warehouseIDs = new ArrayList<>();
        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,warehouseIDs);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        impButt.setOnClickListener(this);
        shipButt.setOnClickListener(this);
        receiptButt.setOnClickListener(this);
        viewButt.setOnClickListener(this);
        exportButt.setOnClickListener(this);
        addButt.setOnClickListener(this);

        updateSpinnerArray();

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }


    }

    // an action listene that is called when a warehouse id is selected
    class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            // get the item selected
            String selected =  parent.getItemAtPosition(pos).toString();
            application.setCurrentWarehouseID(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }


    // a method that is called to loadnew entries to the spinner list if they dont exist
    public void updateSpinnerArray(){
        //query Company for its list of IDs
        List<String> ids = application.getCompany().getWarehouseIds();
        ids.forEach(i -> System.out.println(i));

        //loop through the list and check it is already in the warehouselist here
        //if its not we add it
        ids.forEach(i -> {
            if(!warehouseIDs.contains(i)){
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


    public void openFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //https://developer.android.com/training/sharing/send
        intent.setType("text/json");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public void exportContent(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //https://developer.android.com/training/sharing/send
        intent.setType("text/json");
        intent.putExtra(Intent.EXTRA_TITLE, application.getCurrentWarehouseID() + ".json");
        startActivityForResult(intent, SAVE_REQUEST_CODE);
    }

    // opens Activity to add a shipment. Used by addShipButton.
    public void openAddShipmentActivity() {
        Intent intent = new Intent(this, AddShipmentActivity.class);
        startActivity(intent);
    }

    // opens Activity to add a shipment. Used by addShipButton.
    public void openViewWarehouseActivity() {
        Intent intent = new Intent(this, ViewWarehouseActivity.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri currentUri = null;
        Uri uri = new Uri.Builder().build();

        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SAVE_REQUEST_CODE) {

                if (resultData != null) {

                    currentUri =
                            resultData.getData();
                    try {
                        application.writeWarehouseDataToFile(currentUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(this, "The file has been saved at " + currentUri.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == OPEN_REQUEST_CODE) {

                if (resultData != null) {
                    currentUri = resultData.getData();

                    try {
                        // get file extension
                        // https://stackoverflow.com/questions/53869269/get-file-name-and-extension-of-any-file-in-android
                        String path = new File(resultData.getData().getPath()).getAbsolutePath();

                        if(path != null) {
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
                            System.out.println("The file name " + name + " extension is " + extension + " name is " + name);
                            Toast.makeText(this, "The file name " + name + " extension is " + extension, Toast.LENGTH_SHORT).show();

                            String content =
                                    readFileContent(currentUri);
                            application.importShipment(content, extension);
                            updateSpinnerArray();
                            //textView.append("The file has been imported into Company \n");
                            //Toast.makeText(this, "The file has been opened", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        // Handle error here
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        //content://com.android.providers.downloads.documents/document/29
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
}
