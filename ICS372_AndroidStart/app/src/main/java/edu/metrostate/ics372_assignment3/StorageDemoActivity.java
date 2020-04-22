package edu.metrostate.ics372_assignment3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StorageDemoActivity extends AppCompatActivity {
    //https://medium.com/@sriramaripirala/android-10-open-failed-eacces-permission-denied-da8b630a89df
    //https://www.techotopia.com/index.php/An_Android_Storage_Access_Framework_Example
    private static EditText textView;

    private static final int CREATE_REQUEST_CODE = 40;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;

    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    private WarehouseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_demo);

        textView = (EditText) findViewById(R.id.editText);
        application = (WarehouseApplication) getApplication();
        //application.initialize(); content://com.android.providers.downloads.documents/document/24

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    //Enable the Admin person to create the Storage file location.
    public void newFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/json");
        intent.putExtra(Intent.EXTRA_TITLE, "company.json");

        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    public void saveFile(View view)
    {
        /*
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        startActivityForResult(intent, SAVE_REQUEST_CODE);

         */
        String json = textView.getText().toString();
        application.writeFileContent(json);
    }

    public void openFile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //https://developer.android.com/training/sharing/send
        intent.setType("text/json");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri currentUri = null;

        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_REQUEST_CODE) {
                if (resultData != null) {
                    // show the file location URI
                    String externalFileLocationURI = resultData.getData().toString();
                    application.writeLocationURI(externalFileLocationURI);
                    textView.setText(externalFileLocationURI);
                    //save the file location URI in our internal file storage
                    Toast.makeText(this, "The new file has been created", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == SAVE_REQUEST_CODE) {

                if (resultData != null) {
                    currentUri =
                            resultData.getData();
                    //writeFileContent(currentUri);
                    Toast.makeText(this, "The file has been saved at " + currentUri.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == OPEN_REQUEST_CODE) {

                if (resultData != null) {
                    currentUri = resultData.getData();

                    try {
                        String content =
                                readFileContent(currentUri);
                        textView.setText(content);
                        Toast.makeText(this, "The file has been opened", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // Handle error here
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

    /**
     * Indicates when the user has responded to a permission request
     * @param requestCode The request code
     * @param permissions The permissions requested
     * @param grantResults The result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        System.out.println("Request code is " + requestCode);
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("permission was granted, yay!");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    System.out.println("permission denied, boo!");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission required, closing application", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    public void goToMain(View view) {
        Intent intent = new Intent(StorageDemoActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
