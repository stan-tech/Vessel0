package com.example.besoin;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public interface RecyclerItemClickListener {

        boolean onItemClick(int position);
        void onItemLongClick(int position);
}