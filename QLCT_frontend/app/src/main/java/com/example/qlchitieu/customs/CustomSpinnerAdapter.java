package com.example.qlchitieu.customs;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlchitieu.R;
import com.example.qlchitieu.models.LoaiPhatSinh;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<LoaiPhatSinh> {

    private Context context;
    private ArrayList<LoaiPhatSinh> data;
    public Resources res;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, ArrayList<LoaiPhatSinh> objects) {
        super(context, R.layout.spinner_row, objects);

        this.context = context;
        data = objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.spinner_row, parent, false);

        TextView tvCategory = (TextView) row.findViewById(R.id.tvCategory);

        tvCategory.setText(data.get(position).getLoai().toString());

        ImageView imCategory = (ImageView) row.findViewById(R.id.im_spinner);

        imCategory.setImageResource(data.get(position).getIcon());
        return row;
    }
}