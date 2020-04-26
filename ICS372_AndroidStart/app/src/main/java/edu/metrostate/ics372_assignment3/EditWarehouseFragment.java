package edu.metrostate.ics372_assignment3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.metrostate.ics372_assignment3.model.Warehouse;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditWarehouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditWarehouseFragment extends Fragment {

    private Warehouse warehouse;

    public static EditWarehouseFragment newInstance(Warehouse warehouse) {
        EditWarehouseFragment fragment = new EditWarehouseFragment();
        fragment.warehouse = warehouse;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_warehouse, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((TextView)getActivity().findViewById(R.id.textViewWarehouseID)).setText(warehouse.getWarehouse_id());
        ((TextView)getActivity().findViewById(R.id.editTextWarehouseName)).setText(warehouse.getWarehouse_name());
    }

}
