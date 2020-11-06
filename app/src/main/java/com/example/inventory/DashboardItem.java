package com.example.inventory;

public class DashboardItem {

    String mCategoryName;
    int mCategoryImageResource;

    DashboardItem(String categoryName,int categoryImageResource)
    {
        mCategoryName = categoryName;
        mCategoryImageResource = categoryImageResource;
    }

    public void setCategoryName(String categoryName)
    {
        mCategoryName = categoryName;
    }

    public void setCategoryImageResource(int categoryImageResource)
    {
        mCategoryImageResource = categoryImageResource;
    }

    public String getCategoryName()
    {
        return mCategoryName;
    }

    public int getCategoryImageResource()
    {
        return mCategoryImageResource;
    }

}
