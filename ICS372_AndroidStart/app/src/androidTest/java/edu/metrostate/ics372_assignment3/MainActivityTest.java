package edu.metrostate.ics372_assignment3;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import edu.metrostate.ics372_assignment3.model.Company;
import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;

import static org.junit.Assert.assertTrue;

public class MainActivityTest {

    private MainActivityPresenter presenter;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        presenter = new MainActivityPresenter(Company.getInstance(appContext));
    }

    @Test
    public void showAddNewShipment() {
        // Count number of shipments in warehouse 12531
        Map<String, Shipment> sh = presenter.readWarehouseContent("12531");
        int old_count = sh.size();
        System.out.println(old_count);
        // Add a Shipment to warehouse 12531
        String shipment_id = "23e456j";
        Shipment.ShippingMethod method = Shipment.ShippingMethod.ship;
        double weight = 12.5;
        Shipment shipment = new Shipment("12531", method, shipment_id, weight, System.currentTimeMillis());

        presenter.addShipmentCompleted(shipment);
        // Count the new number of Shipments in that warehouse
        Map<String, Shipment> sh2 = presenter.readWarehouseContent("12531");
        int new_count = sh2.size();
        System.out.println(new_count);
        assertTrue(new_count > old_count);
    }

    @Test
    public void showAddNewWarehouse() {
        // Count number of warehouse
        int old_count = presenter.getWarehouseIds().size();
        // Add new warehouse 44444
        Warehouse wh = new Warehouse("22567");
        wh.setWarehouse_name("Shipping Team");
        presenter.addWarehouseCompleted(wh);
        // Count the new number of warehouses
        int new_count = presenter.getWarehouseIds().size();
        assertTrue(new_count > old_count);
    }

    @Test
    public void toggleWarehouse() {
        boolean old_freight_status = presenter.getFreightReceiptStatus("12531");
        presenter.toggleFreightReciept("12531");
        boolean new_freight_status = presenter.getFreightReceiptStatus("12531");
        assertTrue(old_freight_status != new_freight_status);
    }
}