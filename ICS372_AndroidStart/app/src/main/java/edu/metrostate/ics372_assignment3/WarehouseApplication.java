package edu.metrostate.ics372_assignment3;

import android.app.Application;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.CompanyIO;

public class WarehouseApplication extends Application {
    static final int READ_BLOCK_SIZE = 100;
    public static WarehouseApplication warehouseApplicationSingleton;
    private Company company = null;
    private String currentWarehouseID = null;
    private Uri externalFileStorageURI;
    private String internalFileStorageURI;
    private CompanyIO companyIO;

    public WarehouseApplication() {

    }

    public Company getCompany() {
        return company;
    }

    public void setCompany() {
        company = Company.getInstance(this);
    }

    public String getCurrentWarehouseID() {
        return currentWarehouseID;
    }

    public void setCurrentWarehouseID(String currentWarehouseID) {
        this.currentWarehouseID = currentWarehouseID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInternalFileStorageURI();
        setExternalFileStorageURI();
        warehouseApplicationSingleton = this;
        companyIO = new CompanyIO();
        company = Company.getInstance(this);

    }

    public String getInternalFileStorageURI() {
        return internalFileStorageURI;
    }

    private void setExternalFileStorageURI() {
        String uriString = readLocationURI();
        externalFileStorageURI = Uri.parse(uriString);
    }

    public Uri getExternalFileStorageURI() {
        return externalFileStorageURI;
    }

    private void setInternalFileStorageURI() {
        // check if filestorage.txt exists in internal storage
        File file = new File("filestorageuri.txt");
        if (file.exists()) {
            // set it
            System.out.println("file exists");
            internalFileStorageURI = "filestorageuri.txt";
        } else {
            // if it does not, set it and create it
            System.out.println("file does not exists");
            internalFileStorageURI = "filestorageuri.txt";
            writeLocationURI("");
        }
    }

    public void writeLocationURI(String fileStorageURI) {
        // add-write text into file
        try {
            FileOutputStream fileout = openFileOutput(getInternalFileStorageURI(), MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(fileStorageURI);
            outputWriter.close();
            System.out.println("filestorageuri.txt saved successfully!");
            setExternalFileStorageURI();
            System.out.println("External URI is " + externalFileStorageURI.toString());
            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readLocationURI() {
        //reading text from file
        String s = "";
        try {
            FileInputStream fileIn = openFileInput(getInternalFileStorageURI());
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
