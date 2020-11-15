package com.example.inventory;

public class DashboardItem {

    String mCategoryName;
    int mCategoryCount;

    DashboardItem(String categoryName,int categoryCount)
    {
        mCategoryName = categoryName;

        mCategoryCount = categoryCount;
    }

    public void setCategoryName(String categoryName)
    {
        mCategoryName = categoryName;
    }


    public void setCategoryCount(int categoryCount)
    {
        mCategoryCount = categoryCount;
    }

    public String getCategoryName()
    {
        return mCategoryName;
    }


    public int getCategoryCount()
    {
        return mCategoryCount;
    }

}
