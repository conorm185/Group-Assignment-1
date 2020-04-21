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
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button impButt, shipButt, receiptButt, viewButt, exportButt, addButt; // names are temporary but bad jokes last forever. buttons.

    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;


    private WarehouseApplication application;
    private static TextView textView;

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

        // buttons
        impButt = (Button) findViewById(R.id.importButton);
        shipButt = (Button) findViewById(R.id.addShipButton);
        receiptButt = (Button) findViewById(R.id.receiptButton);
        viewButt = (Button) findViewById(R.id.viewWarehouseButton);
        exportButt = (Button) findViewById(R.id.exportContentButton);
        addButt = (Button) findViewById(R.id.addWarehouseButton);
        textView = findViewById(R.id.TextView);

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
                openFile(v);
                break;
            case R.id.addShipButton:
                Toast.makeText(this, "addShip pressed", Toast.LENGTH_SHORT).show();
                openAddShipmentActivity();
                break;
            case R.id.receiptButton:
                Toast.makeText(this, "receiptButton pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.viewWarehouseButton:
                Toast.makeText(this, "view warehouse pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exportContentButton:
                Toast.makeText(this, "export pressed", Toast.LENGTH_SHORT).show();
                exportContent(v);
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
        intent.putExtra(Intent.EXTRA_TITLE, "55.json");
        startActivityForResult(intent, SAVE_REQUEST_CODE);
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
                        application.writeCompanyDataToFile(currentUri);
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


    public static void log(String content){
        textView.append(content + "\n");
    }

}
