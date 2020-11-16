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

public class ItemListElementAdapter extends ArrayAdapter<ItemListElement> {
    public ItemListElementAdapter(@NonNull Context context, int resource,ArrayList<ItemListElement> list) {
        super(context, resource,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View mItemListElement = convertView;
        ItemListElement currentItem = getItem(position);
        if(mItemListElement==null)
        {
            mItemListElement = LayoutInflater.from(getContext()).inflate(R.layout.item_list_element,parent,false);
        }

        TextView itemListElementTextView = mItemListElement.findViewById(R.id.itemListElementTextView);
        itemListElementTextView.setText(currentItem.getItemName());

        ImageView itemListElementErrorImageView = mItemListElement.findViewById(R.id.itemListElementErrorImageView);
        itemListElementErrorImageView.setVisibility((currentItem.getError()?View.VISIBLE:View.GONE));

        return mItemListElement;
    }
}
