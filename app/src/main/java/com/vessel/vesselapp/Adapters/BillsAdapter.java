package com.example.besoin.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vessel.example.vessel.DataBaseHelper;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillViewHolder>{
    private final RecyclerItemClickListener recyclerItemClickListener;
    Context context;
    ArrayList<Bill> bills;
    ArrayList<Bill_line> billLines;
    static DataBaseHelper dataBaseHelper;
    public BillsAdapter(Context context, ArrayList<Bill> bills,
                        RecyclerItemClickListener recyclerItemClickListener) {

        this.context = context;
        this.bills = bills;
        this.recyclerItemClickListener = recyclerItemClickListener;
        dataBaseHelper = new DataBaseHelper(context);
    }


    @NonNull
    @Override
    public BillsAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_card,parent,false);

       // view.findViewById(R.id.whole_bill);


        return new BillsAdapter.BillViewHolder(view,recyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsAdapter.BillViewHolder holder, int position) {

        Bill bill = bills.get(position);
        bill.setLayout(holder.layout);
        View layout = bill.getLayout();
        holder.layout = (ConstraintLayout) layout;
        ViewGroup.LayoutParams params = layout.getLayoutParams();

        holder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fade = new AlphaAnimation(1,0);
                fade.setInterpolator(new DecelerateInterpolator());
                fade.setDuration(400);
                        //AnimationUtils.loadAnimation(context,R.anim.rotate);
                holder.dropDown.setAnimation(fade);

                if (!holder.opened) {

                    RecyclerView bill_line_rv = layout.findViewById(R.id.bill_line_rv);
                    bill_line_rv.setLayoutManager(new LinearLayoutManager(context));
                    billLines = ShowBillLines(bill.getId());

                    BillLinesAdapter adapter = new BillLinesAdapter(context,billLines,recyclerItemClickListener);
                    bill_line_rv.setAdapter(adapter);


                    revealExtra(params,layout);
                    holder.opened = true;

                }
                holder.dropDown.setVisibility(View.INVISIBLE);
                holder.dropDown.setEnabled(false);

                fade = new AlphaAnimation(0,1);
                fade.setInterpolator(new DecelerateInterpolator());
                fade.setDuration(400);

                holder.collapse.setAnimation(fade);
                holder.collapse.setVisibility(View.VISIBLE);
                holder.collapse.setEnabled(true);

            }
        });
        holder.collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.opened){

                    close(params,layout);
                    holder.opened=false;
                    Animation fade = new AlphaAnimation(1,0);
                    fade.setInterpolator(new DecelerateInterpolator());
                    fade.setDuration(400);

                    holder.collapse.setAnimation(fade);
                    holder.collapse.setVisibility(View.INVISIBLE);
                    holder.collapse.setEnabled(false);

                     fade = new AlphaAnimation(0,1);
                    fade.setInterpolator(new DecelerateInterpolator());
                    fade.setDuration(400);

                    holder.dropDown.setAnimation(fade);
                    holder.dropDown.setVisibility(View.VISIBLE);
                    holder.dropDown.setEnabled(true);
                }
            }
        });
        holder.item_count.setText(String.valueOf(ShowBillItemCount(bill.getId())));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        holder.date.setText(sdf.format(bill.getDate()).replace("-","/"));
        holder.price.setText(bill.getSum()+" DZD");
        
        

    }

    public void close(ViewGroup.LayoutParams params, View layout){
        Animation shrink = AnimationUtils.loadAnimation(context,R.anim.shrink);
        params.width = layout.getWidth();
        params.height = 0;

        layout.setAnimation(shrink);

        layout.setLayoutParams(params);
        layout.setVisibility(View.INVISIBLE);

    }
    public void revealExtra(ViewGroup.LayoutParams params, View layout){

        Animation expand = AnimationUtils.loadAnimation(context,R.anim.expand);


        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = layout.getWidth();

        layout.setAnimation(expand);

        layout.setLayoutParams(params);
        layout.setVisibility(View.VISIBLE);

    }
    private int ShowBillItemCount(int id) {

        Cursor cursor = dataBaseHelper.ShowBillItemCount(id);
        int count = 0;
        if(cursor!=null){

            if(cursor.moveToNext()){

                count = cursor.getInt(0);

            }
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {

        TextView date,item_count,price;
        ImageButton  dropDown,collapse;
        RecyclerView recyclerView;
        ConstraintLayout layout;
        boolean opened = false;
         int opened_position;
        public BillViewHolder(@NonNull View itemView , RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);
            date = itemView.findViewById(R.id.Datetv);
            item_count = itemView.findViewById(R.id.items);
            price = itemView.findViewById(R.id.price);
            dropDown = itemView.findViewById(R.id.scrollDown);
            recyclerView = itemView.findViewById(R.id.bill_line_rv);
            layout = itemView.findViewById(R.id.detail);
            collapse = itemView.findViewById(R.id.Collapse);
            opened_position = getAbsoluteAdapterPosition();
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

    public static ArrayList<Bill_line> ShowBillLines(int id){

        Cursor cursor =  dataBaseHelper.ShowBillLines(id);
        ArrayList<Bill_line> bill_lines = new ArrayList<>();
        Bill_line bill_line;
        Product product ;
        if(cursor !=null){

            while(cursor.moveToNext()){

               product = new Product(cursor.getInt(2));
               bill_line = new Bill_line(cursor.getString(6),product,cursor.getFloat(3),
                       cursor.getFloat(4));

               bill_lines.add(bill_line);

            }

        }

        return bill_lines;

    }

    public String SelectProductInfo(String column,int id){

        Cursor cursor = dataBaseHelper.SelectProductInfoWithID(column,id);
        String value = "null";
        try {

            if (cursor.moveToNext()) {

                value = cursor.getString(0);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;

    }
}
