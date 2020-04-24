package edu.metrostate.ics372_assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.metrostate.ics372_assignment3.model.Company;

public class AddWarehouseActivity extends AppCompatActivity implements View.OnClickListener{

    Company company;
    WarehouseApplication application;
    Button submitButton,cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_warehouse);

        application = (WarehouseApplication) getApplication();
        company = application.getCompany();

        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitButton:
                addWarehouse();
                this.finish();
                break;
            case R.id.cancelButton:
                this.finish();
                break;
        }
    }

    private void addWarehouse(){
        EditText warehouse_id_text = findViewById(R.id.editTextWarehouseID);
        String warehouse_id = warehouse_id_text.getText().toString();

        EditText warehouse_name_text = findViewById(R.id.editTextWarehouseName);
        String warehouse_name = warehouse_name_text.getText().toString();

        company.addWarehouse(warehouse_id);
        company.setWarehouseName(warehouse_id,warehouse_name);
    }
}
