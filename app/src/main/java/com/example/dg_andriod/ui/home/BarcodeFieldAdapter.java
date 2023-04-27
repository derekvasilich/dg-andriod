package com.example.dg_andriod.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dg_andriod.R;

import java.util.ArrayList;

public class BarcodeFieldAdapter extends RecyclerView.Adapter {

    private ArrayList<BarcodeField> barcodeFieldList;

    public BarcodeFieldAdapter(ArrayList<BarcodeField> fieldList) {
        barcodeFieldList = fieldList;
    }

    public static class BarcodeFieldViewHolder extends RecyclerView.ViewHolder {

        private static TextView labelView;
        private static TextView valueView;

        public BarcodeFieldViewHolder(@NonNull View itemView) {
            super(itemView);
            labelView = itemView.findViewById(R.id.barcode_field_label);
            valueView = itemView.findViewById(R.id.barcode_field_value);
        }

        public void bindBarcodeField(BarcodeField barcodeField) {
            labelView.setText(barcodeField.label);
            valueView.setText(barcodeField.value);
        }

        public static BarcodeFieldViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_field, parent, false);
            return new BarcodeFieldViewHolder(view);
        }
    }

    @NonNull
    @Override
    public BarcodeFieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BarcodeFieldViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BarcodeFieldViewHolder)holder).bindBarcodeField(barcodeFieldList.get(position));
    }


    @Override
    public int getItemCount() {
        return barcodeFieldList.size();
    }
}
