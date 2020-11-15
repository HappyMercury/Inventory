package com.example.inventory;

public class ToDoListElement {

    String mToDoListTextView;
    boolean mReminderImageView;

    ToDoListElement(String toDoListTextView,boolean reminderImageView)
    {
        mToDoListTextView = toDoListTextView;
        mReminderImageView = reminderImageView;
    }

    public void setToDoListTextView(String toDoListTextView)
    {
        mToDoListTextView = toDoListTextView;
    }


    public void setReminderImageView(boolean reminderImageView)
    {
        mReminderImageView = reminderImageView;
    }

    public String getToDoListTextView()
    {
        return mToDoListTextView;
    }


    public boolean getReminderImageView()
    {
        return mReminderImageView;
    }
}
