package com.example.besoin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;
import com.example.besoin.SupplierInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.DiffUtils;
import vessel.example.vessel.Fournisseurs;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {

    ArrayList<Supplier> suppliers;
    Context context;
    RecyclerItemClickListener recyclerItemClickListener;
    private boolean item_deleted;

    public SupplierAdapter(ArrayList<Supplier> suppliers, Context context,
                           RecyclerItemClickListener recyclerItemClickListener) {
        this.suppliers = suppliers;
        this.context = context;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public void InsertData(ArrayList<Supplier> newData){

        DiffUtils<Supplier> diffUtils = new DiffUtils<>(newData,suppliers);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtils);

        suppliers.addAll(newData);
        result.dispatchUpdatesTo(this);

    }
    @NonNull
    @Override
    public SupplierAdapter.SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.supplier_card,parent,false);

        return new SupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.SupplierViewHolder holder, int position) {

        Supplier supplier = suppliers.get(position);

        if (supplier.getName().length() > 12) {

            holder.name.setText(supplier.getName().substring(0
                    ,supplier.getName().indexOf(" "))+" ...");
        }else{
            holder.name.setText(supplier.getName());
        }
        holder.phone.setText(supplier.getTelephone());

        try {

            if(!supplier.getImage().equals("Not available")){
                holder.picture.setImageURI(Uri.parse(supplier.getImage()));
            }
        } catch (Exception e) {
        }

        supplier.setLayout(holder.layout);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Opendialog(holder.getAdapterPosition());

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SupplierInfo.class);
                intent.putExtra("name",supplier.getName());
                intent.putExtra("phone",supplier.getTelephone());
                intent.putExtra("id",supplier.getId());
                intent.putExtra("CompanyName",supplier.getCompanyName());
                intent.putExtra("picture",supplier.getImage());
                intent.putExtra("address",supplier.getAddress());


                context.startActivity(intent);
                Fournisseurs.finish.finish();



            }
        });




    }
    public void Opendialog(int position){


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_prompt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.Yesbtt);
        Button no = dialog.findViewById(R.id.Nobtt);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                Cursor cursor = dataBaseHelper.ShowWorkerData();

                Supplier value = suppliers.get(position);

                if (dataBaseHelper.DeleteRow("supplier",
                        "id",
                        String.valueOf(value.getId()))) {


                    item_deleted = true;

                    try {
                        suppliers.remove(position);
                        notifyItemRemoved(position);

                    } catch (Exception e) {

                        Toast.makeText(context,"supplier not deleted",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                            return;
                    }

                    Toast.makeText(context,"supplier deleted",Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(context,"supplier not deleted",Toast.LENGTH_LONG).show();

                }

                dialog.hide();



            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();

            }
        });

        dialog.show();

    }

    @Override
    public int getItemCount() {

        return suppliers.size();
    }

    public class SupplierViewHolder extends  RecyclerView.ViewHolder{

        SupplierAdapter supp_adapter;
        Button edit,delete;
        TextView name, phone,productName;
        ImageButton call;
        CircleImageView picture;
        LinearLayout layout;
        boolean opened;
        public SupplierViewHolder(@NonNull View itemView) {
            super(itemView);

            supp_adapter = new SupplierAdapter(suppliers,itemView.getContext(),recyclerItemClickListener);
            opened = false;
            edit = itemView.findViewById(R.id.edit);
            delete= itemView.findViewById(R.id.delete);
            name = itemView.findViewById(R.id.Nametv);
            phone= itemView.findViewById(R.id.Phonetv);
            productName = itemView.findViewById(R.id.textView);
            call = itemView.findViewById(R.id.call_button);
            picture = itemView.findViewById(R.id.profile_image);
            layout = itemView.findViewById(R.id.extra);

            ViewGroup.LayoutParams params = layout.getLayoutParams();

            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    if(getState()){
                        setState(false);
                        close(params,layout);


                    }else{

                        if(recyclerItemClickListener !=null){
                            int pos = getAdapterPosition();
                            if(pos!=RecyclerView.NO_POSITION){
                                recyclerItemClickListener.onItemClick(pos);
                            }
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();

                    if(!getState()){
                        setState(true);


                    if(recyclerItemClickListener !=null){
                        if(pos!=RecyclerView.NO_POSITION){

                            recyclerItemClickListener.onItemLongClick(pos);

                        }
                    }
                         if(item_deleted){
                        supp_adapter.notifyItemRemoved(pos);
                             }

                    }
                  return true;
                }

            });
        }
        public boolean getState(){
            return opened;
        }
        public void setState(boolean state){
            this.opened  = state;

        }
        public void close(ViewGroup.LayoutParams params, View layout){
            Animation shrink = AnimationUtils.loadAnimation(context,R.anim.shrink);
            params.width = layout.getWidth();
            params.height = 0;

            layout.setAnimation(shrink);

            layout.setLayoutParams(params);
            layout.setVisibility(View.INVISIBLE);

        }
    }
}
