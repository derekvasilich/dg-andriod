package com.example.dg_andriod.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dg_andriod.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BarcodeResultFragment extends BottomSheetDialogFragment {

    private static final String TAG = "BarcodeResultFragment";
    private static final String ARG_BARCODE_FIELD_LIST = "arg_barcode_field_list";
    public static DialogInterface.OnDismissListener onDismissListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_license_bottom_sheet, container);

        ArrayList<BarcodeField> barcodeFieldList = getArguments().getParcelableArrayList(ARG_BARCODE_FIELD_LIST);

        RecyclerView recyclerView = root.findViewById(R.id.barcode_field_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new BarcodeFieldAdapter(barcodeFieldList));

        Button saveButton = root.findViewById(R.id.barcode_button_save);
        saveButton.setOnClickListener(view -> onSaveBarcode(root));

        return root;
    }

    public static void show(@NonNull FragmentManager manager, ArrayList<BarcodeField> barcodeFieldArrayList, @NonNull DialogInterface.OnDismissListener onDismissListener) {
        BarcodeResultFragment barcodeResultFragment = new BarcodeResultFragment();
        barcodeResultFragment.onDismissListener = onDismissListener;

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_BARCODE_FIELD_LIST, barcodeFieldArrayList);
        barcodeResultFragment.setArguments(bundle);

        barcodeResultFragment.show(manager, TAG);
    }

    public void onSaveBarcode(View view) {
        Bundle bundle = getArguments();
        ArrayList<BarcodeField> barcodeFieldArrayList = bundle.getParcelableArrayList(ARG_BARCODE_FIELD_LIST);

        // save the barcode the to database
        Toast.makeText(view.getContext(),"Saving Barcode list of length: "+barcodeFieldArrayList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        onDismissListener.onDismiss(dialogInterface);
    }

    @Override
    public void dismiss() {
        // TODO - dismiss the fragments
        super.dismiss();
    }
}
