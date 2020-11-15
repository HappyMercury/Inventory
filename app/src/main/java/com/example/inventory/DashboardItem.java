package com.example.inventory;

public class DashboardItem {

    String mCategoryName;
    int mCategoryImageResource;
    int mCategoryCount;

    DashboardItem(String categoryName,int categoryImageResource,int categoryCount)
    {
        mCategoryName = categoryName;
        mCategoryImageResource = categoryImageResource;
        mCategoryCount = categoryCount;
    }

    public void setCategoryName(String categoryName)
    {
        mCategoryName = categoryName;
    }

    public void setCategoryImageResource(int categoryImageResource)
    {
        mCategoryImageResource = categoryImageResource;
    }

    public void setCategoryCount(int categoryCount)
    {
        mCategoryCount = categoryCount;
    }

    public String getCategoryName()
    {
        return mCategoryName;
    }

    public int getCategoryImageResource()
    {
        return mCategoryImageResource;
    }

    public int getCatgoeyCount()
    {
        return mCategoryCount;
    }

}
