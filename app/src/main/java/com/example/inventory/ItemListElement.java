package com.example.inventory;

public class ItemListElement {

    String mItemName;
    boolean mError;

    ItemListElement(String itemName,boolean error)
    {
        mItemName = itemName;
        mError = error;
    }

    public void setItemName(String itemName){
        mItemName = itemName;
    }

    public void setError(boolean error)
    {
        mError = error;
    }

    public String getItemName()
    {
        return mItemName;
    }

    public boolean getError()
    {
        return mError;
    }

}
