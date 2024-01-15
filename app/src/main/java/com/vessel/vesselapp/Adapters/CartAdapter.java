package com.example.besoin.Adapters;

import static com.example.besoin.Stock.PurchaseSum;
import static com.example.besoin.Stock.cartTotalSum;
import static com.example.besoin.Stock.purchase_sum;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DiffUtils;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {
private final RecyclerItemClickListener recyclerItemClickListener;

        Context context;
        ArrayList<Product> products;
        Dialog dialog;
        TextView CartSum, CartSize;
        String transType;
public CartAdapter(Context context, ArrayList<Product> products , TextView cartSize,
                   RecyclerItemClickListener recyclerItemClickListener, Dialog dialog,
                   String transType) {
        this.context = context;
        this.products = products;
        this.recyclerItemClickListener = recyclerItemClickListener;
        this.dialog = dialog;
        CartSum = dialog.findViewById(R.id.total);
        this.CartSize = cartSize;
         this.transType = transType;
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
public CartAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_card,parent,false);

        return new CartAdapter.ProductViewHolder(view,recyclerItemClickListener);
        }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ProductViewHolder holder, int position) {

        Product product = products.get(position);

        holder.Name.setText( product.getName());

        if(transType.equals("out")){
            holder.price.setText(product.getSelling_price() +" DZD");

        }else{
            holder.price.setText(product.getBuying_price() +" DZD");

        }

        if (product.getUnit() != "Unit") {
            holder.quanity.setText(product.getQuantity()+" "+ product.getUnit() );
        } else {
            holder.quanity.setText(String.valueOf(product.getQuantity()).replace(".0"," ")+" "+ product.getUnit() );

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.chosen){

                    notifyDataSetChanged();
                }
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float Quantity = products.get(holder.getAbsoluteAdapterPosition()).getQuantity()
                        ,OldPrice = Float.parseFloat(cartTotalSum.getText().toString().replace(" DZD",""));

                float NewPrice = 0;
                if(transType.equals("out")) {
                    NewPrice = products.get(holder.getAbsoluteAdapterPosition()).getSelling_price();
                }else{
                    NewPrice = products.get(holder.getAbsoluteAdapterPosition()).getBuying_price();
                }
                float NewTotal = OldPrice - (Quantity *NewPrice);

                CartSum.setText(String.format("%.2f",NewTotal)+" DZD");
                PurchaseSum = NewTotal;
                products.remove(holder.getAbsoluteAdapterPosition());

                CartSize.setText(String.valueOf(products.size()));
                purchase_sum.setText(" "+String.format("%.2f",NewTotal) + " DZD ");
                cartTotalSum.setText(" "+String.format("%.2f",NewTotal) + " DZD ");
                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            }
        });


    }




@Override
public int getItemCount() {
        return products.size();
        }

public static class ProductViewHolder extends RecyclerView.ViewHolder {
    TextView Name,price,quanity;
    CircleImageView prod_image;
    ImageButton delete;

    boolean chosen;
    public ProductViewHolder(@NonNull View itemView , RecyclerItemClickListener recyclerItemClickListener) {
        super(itemView);

        Name = itemView.findViewById(R.id.Nametv);
        price = itemView.findViewById(R.id.price);
        prod_image = itemView.findViewById(R.id.product_image);
        quanity = itemView.findViewById(R.id.qtt);
        delete = itemView.findViewById(R.id.delete_button);

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

