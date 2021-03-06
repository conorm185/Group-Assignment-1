package edu.metrostate.ics372_assignment3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.CompanyIO;
import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;

/**
 * The Main View of the application, handles all fragments and dialogs.  Makes calls to the presenter in order to affect the model
 */
public class MainActivity extends AppCompatActivity implements MainActivityMVP.View, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final int CREATE_REQUEST_CODE = 40;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;

    private Button impButt, exportButt, addButt, addShipmentButton, toggleActiveInactiveButton, toggleRecieptButton, editWarehouseButton;
    private TextView warehouse_name, shipment_warehouse_id, shipment_shipment_id, shipment_method, shipment_weight, shipment_receipt, shipment_departure;

    private ArrayAdapter adapter;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ListView shipmentList;

    private WarehouseApplication application;
    private MainActivityPresenter presenter;
    private HashMap<String, Shipment> warehouse_contents;
    private List<String> warehouseIDs;

    private AlertDialog dialog;

    private String current_warehouse_id;
    private String current_shipment_id;

    private DialogInterface.OnClickListener addWarehouseHandler = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String id = ((TextView) MainActivity.this.dialog.findViewById(R.id.textViewWarehouseID)).getText().toString();
            String name = ((TextView) MainActivity.this.dialog.findViewById(R.id.editTextWarehouseName)).getText().toString();

            Warehouse warehouse = new Warehouse(id);
            warehouse.setWarehouse_name(name);

            presenter.addWarehouseCompleted(warehouse);
            dialog.dismiss();
        }

    };
    private DialogInterface.OnClickListener editWarehouseHandler = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String name = ((TextView) MainActivity.this.dialog.findViewById(R.id.editTextWarehouseName)).getText().toString();

            Warehouse warehouse = new Warehouse(current_warehouse_id);
            warehouse.setWarehouse_name(name);

            presenter.editWarehouseCompleted(warehouse);
            dialog.dismiss();
        }

    };
    private DialogInterface.OnClickListener addShipmentHandler = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String shipment_id = ((TextView) MainActivity.this.dialog.findViewById(R.id.editTextShipmentId)).getText().toString();
            String method_string = (String) ((Spinner) MainActivity.this.dialog.findViewById(R.id.spinnerShipmentMethod)).getSelectedItem();

            Shipment.ShippingMethod method = Shipment.ShippingMethod.valueOf(method_string);
            String weight_string = ((TextView) MainActivity.this.dialog.findViewById(R.id.editTextShipmentWeight)).getText().toString();
            double weight = Double.parseDouble(weight_string);

            Shipment shipment = new Shipment(current_warehouse_id, method, shipment_id, weight, System.currentTimeMillis());

            presenter.addShipmentCompleted(shipment);
            dialog.dismiss();
        }

    };
    private DialogInterface.OnClickListener moveShipmentHandler = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String target_warehouse_id = (String) ((Spinner) MainActivity.this.dialog.findViewById(R.id.spinnerWarehouseID)).getSelectedItem();

            if (target_warehouse_id == null || target_warehouse_id.equalsIgnoreCase(current_warehouse_id)) {
                Toast.makeText(MainActivity.this, "Shipment already at selected warehouse", Toast.LENGTH_SHORT).show();
            } else {
                presenter.moveShipmentCompleted(current_shipment_id, current_warehouse_id, target_warehouse_id);
            }
            dialog.dismiss();
        }

    };

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

        presenter = new MainActivityPresenter(application.getModel());
        presenter.setView(this);

        // buttons
        impButt = findViewById(R.id.importButton);
        exportButt = findViewById(R.id.exportContentButton);
        addButt = findViewById(R.id.addWarehouseButton);
        addShipmentButton = findViewById(R.id.addShipmentBtn);
        toggleActiveInactiveButton = findViewById(R.id.moveShipmentBtn);
        toggleRecieptButton = findViewById(R.id.receiptBtn);
        editWarehouseButton = findViewById(R.id.editWarehouseBtn);
        warehouse_name = findViewById(R.id.textViewWarehouseName);
        shipment_warehouse_id = findViewById(R.id.textViewWarehouseID);
        shipment_shipment_id = findViewById(R.id.textViewDepartureShipmentID);
        shipment_method = findViewById(R.id.textViewShipmentMethod);
        shipment_weight = findViewById(R.id.textViewShipmentWeight);
        shipment_receipt = findViewById(R.id.textViewReceipt);
        shipment_departure = findViewById(R.id.textViewDeparture);
        shipmentList = findViewById(R.id.shipment_list_view);

        spinner = findViewById(R.id.spinner);
        warehouseIDs = new ArrayList<>();
        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, warehouseIDs);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        updateSpinnerArray();
        if (!spinnerArrayAdapter.isEmpty())
            this.current_warehouse_id = spinnerArrayAdapter.getItem(0);

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }

        toggleActiveInactiveButton.setOnClickListener(this);
        toggleRecieptButton.setOnClickListener(this);
        editWarehouseButton.setOnClickListener(this);
        impButt.setOnClickListener(this);
        exportButt.setOnClickListener(this);
        addShipmentButton.setOnClickListener(this);
        addButt.setOnClickListener(this);

        refreshShipmentList();
    }

    /**
     * refreshes elements of the ui on Activity resume
     */
    @Override
    public void onResume() {
        super.onResume();
        updateSpinnerArray();
        refreshShipmentList();
        if (current_warehouse_id != null)
            refreshShipmentInfo((String) shipmentList.getSelectedItem());
    }

    /**
     * update the spinner array that stores warehouse IDs
     */
    public void updateSpinnerArray() {
        List<String> ids = presenter.getWarehouseIds();
        ids.forEach(i -> {
            if (!warehouseIDs.contains(i)) {
                warehouseIDs.add(i);
            }
        });
        spinnerArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Click listener that handles all of the on screen buttons.
     * @param v the button that was clicked
     */
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
            case R.id.addShipmentBtn:
                presenter.addShipmentClicked();
                break;
            case R.id.addWarehouseButton:
                presenter.addWarehouseClicked();
                break;
            case R.id.moveShipmentBtn:
                if (current_shipment_id == null) {
                    Toast.makeText(this, "No Shipment Selected", Toast.LENGTH_SHORT).show();
                } else if (current_shipment_id.contains("Inactive")) {
                    Toast.makeText(this, "Cannot move inactive shipment", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.moveShipmentClicked();
                }
                break;
            case R.id.receiptBtn:
                presenter.toggleFreightReciept(current_warehouse_id);
                //presenter.getFreightReceiptStatus();
                String status_string = "Warehouse " + current_warehouse_id + " receiving freight: "
                        + presenter.getFreightReceiptStatus(current_warehouse_id);
                Toast.makeText(this, status_string, Toast.LENGTH_SHORT).show();
                break;
            case R.id.editWarehouseBtn:
                presenter.editWarehouseClicked();
                break;
        }
    }


    /**
     * mothod to open a connection to a file for writing
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

    /**
     * method to begin a save operation to a file
     * @param view
     */
    public void exportContent(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //https://developer.android.com/training/sharing/send
        intent.setType("text/json");
        intent.putExtra(Intent.EXTRA_TITLE, current_warehouse_id + ".json");
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

    /**
     * method to import shipments from a file
     *
     *
     * @param requestCode
     * @param resultCode
     * @param resultData
     */
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
                    CompanyIO.saveContentToJSON(this, currentUri, current_warehouse_id);
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

    /**
     * method to read the string content of a file that has been opened
     *
     * @param uri
     * @return
     * @throws IOException
     */
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

    /**
     * On click listener that handles when a shipment id is clicked in the ListView
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String current_shipment_id = parent.getItemAtPosition(position).toString();
        refreshShipmentInfo(current_shipment_id);
    }

    /**
     * refresh the ListView with an updated list of shipments inside the given warehouse
     */
    public void refreshShipmentList() {
        if (current_warehouse_id != null) {
            warehouse_contents = presenter.readWarehouseContent(current_warehouse_id);
            String[] shipment_id_list = warehouse_contents.keySet().toArray(new String[0]);
            Arrays.sort(shipment_id_list);
            adapter = new ArrayAdapter<>(this, R.layout.shipment_list_view, shipment_id_list);
            shipmentList = findViewById(R.id.shipment_list_view);
            shipmentList.setAdapter(adapter);
            shipmentList.setOnItemClickListener(this);
            warehouse_name.setText(presenter.getWarehouseName(current_warehouse_id));
            if (!adapter.isEmpty())
                refreshShipmentInfo((String) adapter.getItem(0));
        }
    }

    /**
     * refresh the display shipment info whenever a new shipment is selected
     *
     * @param shipment_id the id of teh shipent being displayed
     */
    public void refreshShipmentInfo(String shipment_id) {
        if (shipment_id == null) {
            shipment_warehouse_id.setText(current_warehouse_id);
            shipment_shipment_id.setText("N/A");
            shipment_method.setText("N/A");
            shipment_weight.setText("N/A");
            shipment_receipt.setText("N/A");
            shipment_departure.setText("N/A");
        } else {
            current_shipment_id = shipment_id;
            Shipment current_shipment = warehouse_contents.get(shipment_id);
            Date receipt = new Date(current_shipment.getReceipt_date());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            shipment_warehouse_id.setText(current_shipment.getWarehouse_id());
            shipment_shipment_id.setText(current_shipment.getShipment_id());
            shipment_method.setText(current_shipment.getShipment_method().toString());
            shipment_weight.setText(String.format("%.2f lbs", current_shipment.getWeight()));
            shipment_receipt.setText(formatter.format(receipt));
            if (current_shipment.getDeparture_date() == null || current_shipment.getDeparture_date() == 0) {
                shipment_departure.setText("N/A");
            } else {
                shipment_departure.setText(formatter.format(current_shipment.getDeparture_date()));
            }
        }
    }

    /**
     * update the current warehouse id when one is selected from the warehouse selecting spinner
     * and fill the ListView with the shipments inside that warehouse
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();
        current_warehouse_id = selected;
        refreshShipmentList();
        if (!adapter.isEmpty()) {
            refreshShipmentInfo((String) shipmentList.getItemAtPosition(0));
        } else {
            refreshShipmentInfo(null);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * display a add shipment dialog when the presenter sends the command to
     */
    @Override
    public void showAddNewShipment() {
        View v = getLayoutInflater().inflate(R.layout.fragment_add_shipment, null);
        TextView warehouse_id = v.findViewById(R.id.showWareIdText);
        warehouse_id.setText(current_warehouse_id);

        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add New Shipment")
                .setView(v)
                .setPositiveButton("Add", addShipmentHandler).show();
    }

    /**
     * display a add warehouse dialog when the presenter sends the command to
     */
    @Override
    public void showAddNewWarehouse() {
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add New Warehouse")
                .setView(R.layout.fragment_add_warehouse)
                .setPositiveButton("Add", addWarehouseHandler).show();
    }

    /**
     * display a edit warehouse dialog when the presenter sends the command to
     */
    @Override
    public void showEditWarehouse() {
        View v = getLayoutInflater().inflate(R.layout.fragment_edit_warehouse, null);
        TextView warehouse_id = v.findViewById(R.id.textViewWarehouseID);
        warehouse_id.setText(current_warehouse_id);

        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Edit Warehouse")
                .setView(v)
                .setPositiveButton("Submit Edit", editWarehouseHandler).show();
    }

    /**
     * display a move shipment dialog when the presenter sends the command to
     */
    @Override
    public void showMoveShipment() {
        ArrayAdapter id_adapter = new ArrayAdapter<>(this, R.layout.shipment_list_view, warehouseIDs);
        View v = getLayoutInflater().inflate(R.layout.fragment_move_shipment, null);
        Spinner spin = v.findViewById(R.id.spinnerWarehouseID);
        spin.setAdapter(id_adapter);
        TextView departure_id = v.findViewById(R.id.textViewDepartureID);
        departure_id.setText(current_warehouse_id);
        TextView departure_shipment_id = v.findViewById(R.id.textViewDepartureShipmentID);
        departure_shipment_id.setText(current_shipment_id);
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Move Shipment")
                .setView(v)
                .setPositiveButton("Move Shipment", moveShipmentHandler).show();

    }

    /**
     * update the shipment list when the presenter says it is needed
     *
     * @param shipment_id_list
     */
    @Override
    public void showShipments(String[] shipment_id_list) {
        refreshShipmentList();
    }

    /**
     * update the warehouse id list when the presenter says it is needed
     *
     * @param warehouseIds
     */
    @Override
    public void showWarehouses(ArrayList<String> warehouseIds) {
        updateSpinnerArray();
        refreshShipmentList();
    }
}
