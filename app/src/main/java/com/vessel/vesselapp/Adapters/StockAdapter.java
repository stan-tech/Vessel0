package com.example.besoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.util.ArrayList;

import vessel.example.vessel.DiffUtils;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ProductViewHolder> {
    private final RecyclerItemClickListener recyclerItemClickListener;

    Context context;
    ArrayList<Product> products;
    String trans_type;
    public StockAdapter(Context context, ArrayList<Product> products
            , RecyclerItemClickListener recyclerItemClickListener,
                        String Trans_type) {
        this.context = context;
        this.products = products;
        this.recyclerItemClickListener = recyclerItemClickListener;
        trans_type = Trans_type;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void InsertData(ArrayList<Product> newData){

        DiffUtils<Product> diffUtils = new DiffUtils<>(newData,products);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtils);

        products.addAll(newData);
        result.dispatchUpdatesTo(this);

    }

    @NonNull
    @Override
    public StockAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card,parent,false);

        return new StockAdapter.ProductViewHolder(view,recyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ProductViewHolder holder, int position) {


        Product product = products.get(position);
        holder.Name.setText( product.getName());

        if(trans_type.equals("out")){
            holder.price.setText(product.getSelling_price() +" DZD");

        }else{
            holder.price.setText(product.getBuying_price() +" DZD");
        }

        if (!product.getUnit().equals("Unit")) {
            holder.quanity.setText(product.getQuantity()+" "+ product.getUnit() );
        } else {
            holder.quanity.setText(String.format("%.0f",product.getQuantity())+" "+ product.getUnit() );

        }


        if (holder.chosen){

            notifyDataSetChanged();
        }
        // holder.imageView.setImageResource(Uri.parse(product.getImage()));

    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView Name,price,quanity;
        ImageView prod_image;
        boolean chosen;
        public ProductViewHolder(@NonNull View itemView , RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);

            Name = itemView.findViewById(R.id.Name);
            price = itemView.findViewById(R.id.Price);
            prod_image = itemView.findViewById(R.id.prod_pic);
            quanity = itemView.findViewById(R.id.Quantity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (recyclerItemClickListener != null){

                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                           chosen = recyclerItemClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

