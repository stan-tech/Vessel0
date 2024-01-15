package com.example.besoin.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.util.ArrayList;

import vessel.example.vessel.DataBaseHelper;

public class BillLinesAdapter extends RecyclerView.Adapter<BillLinesAdapter.BillLineViewHolder>{
    private final RecyclerItemClickListener recyclerItemClickListener;
    Context context;
    ArrayList<Bill_line> billLines;
    DataBaseHelper dataBaseHelper;

    public BillLinesAdapter(Context context, ArrayList<Bill_line> billLines,
                            RecyclerItemClickListener recyclerItemClickListener) {

        this.context = context;
        this.billLines = billLines;
        this.recyclerItemClickListener = recyclerItemClickListener;
        dataBaseHelper = new DataBaseHelper(context);
    }


    @NonNull
    @Override
    public BillLinesAdapter.BillLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_line_card,parent,false);

        view.findViewById(R.id.extra);


        return new BillLinesAdapter.BillLineViewHolder(view,recyclerItemClickListener);    }

    @Override
    public void onBindViewHolder(@NonNull BillLinesAdapter.BillLineViewHolder holder, int position) {
        Bill_line bill_line = billLines.get(position);

        holder.name.setText(SelectProductName(bill_line.getProduct().getId()));
        holder.quantity.setText(String.valueOf(bill_line.getQuantity()));
        float price = bill_line.getSum() / bill_line.getQuantity();
        holder.price.setText(String.valueOf(price));

    }

    @Override
    public int getItemCount() {
        return billLines.size();
    }

    public class BillLineViewHolder extends RecyclerView.ViewHolder {

        TextView name,quantity,price;
        RecyclerView recyclerView;
        public BillLineViewHolder(@NonNull View itemView, RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.Nametv);
            quantity = itemView.findViewById(R.id.qtt);
            price = itemView.findViewById(R.id.price);
            recyclerView = itemView.findViewById(R.id.bill_line_rv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerItemClickListener !=null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            recyclerItemClickListener.onItemClick(pos);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(recyclerItemClickListener !=null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){

                            recyclerItemClickListener.onItemLongClick(pos);

                        }
                    }
                    notifyItemRemoved(getAbsoluteAdapterPosition());

                    return true;
                }
            });

        }
    }


    public String SelectProductName(int id){

        Cursor cursor = dataBaseHelper.SelectProductInfoWithID("name",id);
        String name = "no name";
        try {

            if (cursor.moveToNext()) {

                name = cursor.getString(0);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;

    }
    public void filterList(ArrayList<Bill_line> bill_lines){
        billLines = bill_lines;
        notifyDataSetChanged();
    }
}
