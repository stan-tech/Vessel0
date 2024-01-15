package com.example.besoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.util.ArrayList;

public class Tables_adapter extends RecyclerView.Adapter<Tables_adapter.WorkerViewHolder> {
    private final RecyclerItemClickListener recyclerItemClickListener;

    public Tables_adapter(Context context, ArrayList<Table> tables , RecyclerItemClickListener recyclerItemClickListener) {
        this.context = context;
        this.tables = tables;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    Context context;
    ArrayList<Table> tables;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @NonNull
    @Override
    public Tables_adapter.WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_card,parent,false);

        return new WorkerViewHolder(view,recyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Tables_adapter.WorkerViewHolder holder, int position) {

        Table table = tables.get(position);
        holder.table.setText( table.getName());
        holder.imageView.setImageResource(table.getImage());

    }



    @Override
    public int getItemCount() {
        return tables.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView table ;
        ImageView imageView;
        public WorkerViewHolder(@NonNull View itemView , RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);

            table = itemView.findViewById(R.id.Table);
            imageView = itemView.findViewById(R.id.profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (recyclerItemClickListener != null){

                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerItemClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
