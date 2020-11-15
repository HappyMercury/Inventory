package com.example.inventory;

public class ToDoListElement {

    String mToDoListTextView;
    int mToDoListImageView;
    boolean mReminderImageView;

    ToDoListElement(String toDoListTextView,int toDoListImageView,boolean reminderImageView)
    {
        mToDoListImageView = toDoListImageView;
        mToDoListTextView = toDoListTextView;
        mReminderImageView = reminderImageView;
    }

    public void setToDoListTextView(String toDoListTextView)
    {
        mToDoListTextView = toDoListTextView;
    }

    public void setToDoListImageView(int toDoListImageView)
    {
        mToDoListImageView = toDoListImageView;
    }

    public void setReminderImageView(boolean reminderImageView)
    {
        mReminderImageView = reminderImageView;
    }

    public String getToDoListTextView()
    {
        return mToDoListTextView;
    }

    public int getToDoListImageView()
    {
        return mToDoListImageView;
    }

    public boolean getReminderImageView()
    {
        return mReminderImageView;
    }
}
