package com.vessel.vesselapp;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class DiffUtils<T> extends DiffUtil.Callback {
    private ArrayList<T> newList;
    private ArrayList<T> oldList;


    public DiffUtils(ArrayList<T> newList, ArrayList<T> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }




    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
