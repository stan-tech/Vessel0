package com.example.besoin.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.DiffUtils;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    ArrayList<Client> clients;
    Context context;
    RecyclerItemClickListener recyclerItemClickListener;
    public boolean item_deleted = false;

    public ClientAdapter(ArrayList<Client> clients, Context context, RecyclerItemClickListener recyclerItemClickListener) {
        this.clients = clients;
        this.context = context;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public boolean isItem_deleted() {
        return item_deleted;
    }

    public void InsertData(ArrayList<Client> newData){

        DiffUtils<Client> diffUtils = new DiffUtils<>(newData,clients);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtils);

        clients.addAll(newData);
        result.dispatchUpdatesTo(this);

    }
    @NonNull
    @Override
    public ClientAdapter.ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.travailleure_card,parent,false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.ClientViewHolder holder, int position) {
        Client client = clients.get(position);

        if (client.getName().length() > 12) {

            holder.name.setText(client.getName().substring(0
                    ,client.getName().indexOf(" "))+" ...");
        }else{
            holder.name.setText(client.getName());
        }
        holder.phone.setText(client.getPhone());

        client.setLayout(holder.layout);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Opendialog(holder.getAdapterPosition());

            }
        });

    }

    @Override
    public int getItemCount() {
        return clients.size();
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

                Client value = clients.get(position);

                if (dataBaseHelper.DeleteRow("worker",
                        "phone",
                        value.getPhone())) {

                    ArrayList<Client> list = new ArrayList<Client>(Arrays.asList(
                            new Client(value.getId(), value.getName(),value.getPhone(), value.getImage(),value.getAddress())));

                    item_deleted = true;

                    Toast.makeText(context,"worker deleted",Toast.LENGTH_LONG).show();
                    notifyItemRemoved(position);

                }else{
                    Toast.makeText(context,"worker not deleted",Toast.LENGTH_LONG).show();

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
    public class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView name,phone;
        ClientAdapter adapter;
        LinearLayout layout;
        Button delete , edit , info;

        public ClientViewHolder(@NonNull View itemView) {

            super(itemView);

            name = itemView.findViewById(R.id.Nametv);
            phone = itemView.findViewById(R.id.Phonetv);
            adapter = new ClientAdapter(clients,itemView.getContext(),recyclerItemClickListener);
            layout = itemView.findViewById(R.id.extra);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            info = itemView.findViewById(R.id.infos);            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerItemClickListener !=null){
                        int pos = getAdapterPosition();
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
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){

                            recyclerItemClickListener.onItemLongClick(pos);

                        }
                    }
                    adapter.notifyItemRemoved(getAdapterPosition());

                    return true;
                }
            });
        }
    }
}
