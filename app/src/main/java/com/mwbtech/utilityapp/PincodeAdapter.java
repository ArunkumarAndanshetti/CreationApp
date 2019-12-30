package com.mwbtech.utilityapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PincodeAdapter extends BaseAdapter implements Filterable {

    Activity activity;
    List<Office> pincodeArrayList;
    List<Office> pincodeFilter;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    Context context;
    public PincodeAdapter(Activity activity, List<Office> pincodeArrayList) {

        this.activity =activity;
        this.pincodeArrayList = pincodeArrayList;
        this.pincodeFilter = pincodeArrayList;
    }

    @Override
    public int getCount() {
        try {
            int size = pincodeFilter.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }

        //return pincodeFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return pincodeFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pincode_adapter_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvId = (TextView)convertView.findViewById(R.id.pinId);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.pinName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvId.setText(pincodeFilter.get(position).getPincode());
        viewHolder.tvName.setText(pincodeFilter.get(position).getName());
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pincodeFilter = pincodeArrayList;
                } else {
                    ArrayList<Office> filteredList = new ArrayList<>();
                    for (Office row : pincodeArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || String.valueOf(row.getPincode()).contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    pincodeFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pincodeFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pincodeFilter = (ArrayList<Office>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder {

        public TextView tvName,tvId;

    }

    private void setRowColor(View view, int var) {
        if (var % 2 == 0 ) {
            view.setBackgroundResource(R.color.button1);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }
}
