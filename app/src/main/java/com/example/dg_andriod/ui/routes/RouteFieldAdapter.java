package com.example.dg_andriod.ui.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dg_andriod.R;
import com.example.dg_andriod.data.model.Route;

import java.util.List;

public class RouteFieldAdapter extends ArrayAdapter<Route> {

    private Context context;
    private List<Route> routeFieldList;

    public RouteFieldAdapter(Context context, List<Route> fieldList) {
        super(context, -1, fieldList);
        this.context = context;
        this.routeFieldList = fieldList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.route_row, parent, false);

        TextView dateView = rowView.findViewById(R.id.route_date);
        TextView companyView = rowView.findViewById(R.id.route_company);
        TextView addressView = rowView.findViewById(R.id.route_address);

        Route route = routeFieldList.get(position);

        dateView.setText(route.date.toString());
        companyView.setText(route.company != null ? route.company.companyName : "");
        addressView.setText(route.startAddress.toString());

        return rowView;
    }
}
