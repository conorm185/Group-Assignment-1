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
        // read the external URI location from the internal file
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

    // write file location URI to internal file storage
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

    // Read file location URI from internal file storage
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

    public void writeFileContent(String textContent) {
        System.out.println("In write file content");
        System.out.println("Text content " + textContent);
        System.out.println("URI " + externalFileStorageURI.toString());

        try {
            ParcelFileDescriptor pfd =
                    getContentResolver().openFileDescriptor(externalFileStorageURI, "rwt");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(
                            pfd.getFileDescriptor());

            fileOutputStream.write(textContent.getBytes());

            fileOutputStream.close();
            pfd.close();
            Toast.makeText(this, "The storage file has been updated. ", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeWarehouseDataToFile(Uri fileUri) throws IOException {
        /*//String textContent = readFromExternalFileStorageURI();
        System.out.println("URI " + fileUri.toString());
        String warehouseJsonText = CompanyIO.exportContentToJSON(getCurrentWarehouseID());
        //String saveFileName = String.format("%s.json", warehouse_id);
        try{
            ParcelFileDescriptor pfd =
                    getContentResolver().
                            openFileDescriptor(fileUri, "rwt");

            FileOutputStream fileOutputStream =
                    new FileOutputStream(
                            pfd.getFileDescriptor());

            fileOutputStream.write(warehouseJsonText.getBytes());

            fileOutputStream.close();
            pfd.close();
            Toast.makeText(this, "The company data has been exported to external file. ", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*public void log(String text){
        MainActivity.log(text);
    }*/

    public void importShipment(String content, String fileExtension) throws Exception {
        /*CompanyIO.importShipments(content, fileExtension);*/
    }

    /*
        public void exportContentToJSON(String warehouse_id) throws Exception {
            String companyJsonText = CompanyIO.exportContentToJSON(warehouse_id);
            String saveFileName = String.format("%s.json", warehouse_id);

        }
    */
    private String readFromExternalFileStorageURI() throws IOException {
        String storageContent = readFileContent(externalFileStorageURI);
        System.out.println("Storage length is " + storageContent.length());
        return storageContent;
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

    public String loadState() throws IOException {
        return readFromExternalFileStorageURI();
    }
}
