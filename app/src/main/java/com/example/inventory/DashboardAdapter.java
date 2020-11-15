package com.example.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DashboardAdapter extends ArrayAdapter<DashboardItem> {


    public DashboardAdapter(@NonNull Context context, int resource, ArrayList<DashboardItem> list) {
        super(context, resource,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View mCategoryItem = convertView;
        DashboardItem currentItem = getItem(position);
        if(mCategoryItem==null)
        {
            mCategoryItem = LayoutInflater.from(getContext()).inflate(R.layout.dashboard_item,parent,false);
        }

        ImageView dashboardItemImageView = mCategoryItem.findViewById(R.id.dashboardItemImageView);
        dashboardItemImageView.setImageResource(currentItem.getCategoryImageResource());

        TextView dashboardItemTextView = mCategoryItem.findViewById(R.id.dashboardItemTextView);
        dashboardItemTextView.setText(currentItem.getCategoryName());

        TextView categoryCountTextView = mCategoryItem.findViewById(R.id.itemCount);
        categoryCountTextView.setText("Item Count: "+(String.valueOf(currentItem.getCatgoeyCount())));

        return mCategoryItem;

    }
}
