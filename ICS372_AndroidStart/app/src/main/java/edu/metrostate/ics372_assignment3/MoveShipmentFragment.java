package edu.metrostate.ics372_assignment3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoveShipmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoveShipmentFragment extends Fragment {

    private Shipment shipment;

    public static MoveShipmentFragment newInstance(Shipment shipment) {
        MoveShipmentFragment fragment = new MoveShipmentFragment();
        fragment.shipment = shipment;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_move_shipment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e("here", "here" + Shipment.ShippingMethod.values().toString());
        super.onActivityCreated(savedInstanceState);
    }

}