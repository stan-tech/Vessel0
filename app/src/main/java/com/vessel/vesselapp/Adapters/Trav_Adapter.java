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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;
import com.example.besoin.Travailleurs;
import com.example.besoin.Update;

import java.util.ArrayList;
import java.util.Arrays;


import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.DiffUtils;

public class Trav_Adapter extends RecyclerView.Adapter<Trav_Adapter.TravViewHolder>
        {
    Context context;
    ArrayList<Travailleur> travailleurs;
    private final RecyclerItemClickListener recyclerItemClickListener;
    public boolean item_deleted = false;

            public boolean isItem_deleted() {
                return item_deleted;
            }

            public Trav_Adapter getAdapter(){
        return this;
    }
    public Trav_Adapter(Context context, ArrayList<Travailleur> travailleurs,RecyclerItemClickListener recyclerItemClickListener) {
        this.context = context;
        this.travailleurs = travailleurs;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }




    public void InsertData(ArrayList<Travailleur> newData){

        DiffUtils<Travailleur> diffUtils = new DiffUtils<>(newData,travailleurs);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtils);

        travailleurs.addAll(newData);
        result.dispatchUpdatesTo(this);


    }

    public void DeleteData(ArrayList<Travailleur> newData){


                DiffUtils<Travailleur> diffUtils = new DiffUtils<Travailleur>(newData,travailleurs);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtils);

                travailleurs.remove(newData);
                result.dispatchUpdatesTo(this);

            }

      public void UpdatetData(ArrayList<Travailleur> newData){

        DiffUtils<Travailleur> diffUtils = new DiffUtils<Travailleur>(travailleurs,newData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtils);

        travailleurs.clear();
        travailleurs.addAll(newData);
        result.dispatchUpdatesTo(this);


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

                        Travailleur value = travailleurs.get(position);

                        if (dataBaseHelper.DeleteRow("worker",
                                "phone",
                                value.getPhone())) {

                            ArrayList<Travailleur> list = new ArrayList<Travailleur>(Arrays.asList(
                                    new Travailleur(value.getId(), value.getName(),value.getPhone(), value.getSalary()))) ;

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

    @NonNull
    @Override
    public Trav_Adapter.TravViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.travailleure_card,parent,false);

        view.findViewById(R.id.extra);


        return new TravViewHolder(view,recyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Trav_Adapter.TravViewHolder holder, int position) {

        Travailleur travailleur = travailleurs.get(position);

        if (travailleur.getName().length() > 12) {

            holder.name.setText(travailleur.getName().substring(0
                    ,travailleur.getName().indexOf(" "))+" ...");
        }else{
            holder.name.setText(travailleur.getName());
        }
        holder.phone.setText(travailleur.getPhone());

        try {
            if (!travailleur.getImage().equals(""))
             {
                holder.image.setImageURI(Uri.parse(travailleur.getImage()));
            }
        } catch (Exception e) {
        }

        travailleur.setLayout(holder.layout);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Opendialog(holder.getAdapterPosition());

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Update.class);
                intent.putExtra("name",holder.name.getText());
                intent.putExtra("phone",holder.phone.getText());
                intent.putExtra("salary",travailleur.getSalary());
                intent.putExtra("picture",travailleur.getImage());
                intent.putExtra("document",travailleur.getDoc());
                intent.putExtra("advances",travailleur.getAdvances());
                intent.putExtra("rest",travailleur.getRest());
                intent.putExtra("id",travailleur.getId());
                context.startActivity(intent);
                Travailleurs.finish.finish();



            }
        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_popUp(holder.name.getText().toString(),holder.phone.getText().toString()
                        ,travailleur.getSalary(),
                        travailleur.getAdvances(),
                        travailleur.getRest());
            }
        });

    }
    public void Open_popUp(String name,String phone,String salary,String advances,String reste){

        TextView name_tv,phone_tv,salary_tv,advances_tv,reste_tv;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        name_tv = dialog.findViewById(R.id.nameTV);
        phone_tv=dialog.findViewById(R.id.phone_number);
        salary_tv=dialog.findViewById(R.id.slr_value);
        advances_tv=dialog.findViewById(R.id.adv_value);
        reste_tv=dialog.findViewById(R.id.rest_value);

        name_tv.setText(name);
        phone_tv.setText(phone);
        salary_tv.setText(salary);
        advances_tv.setText(advances);
        reste_tv.setText(reste);

        dialog.show();


    }

    @Override
    public int getItemCount() {

        return travailleurs.size();

    }

    public static class TravViewHolder extends RecyclerView.ViewHolder {

        TextView name , phone;
        CircleImageView image;
        LinearLayout layout;
        Button delete , edit , info;
        ArrayList<Travailleur> travailleurs = new ArrayList<Travailleur>();
        Trav_Adapter trav_adapter ;
        public TravViewHolder(@NonNull View itemView , RecyclerItemClickListener recyclerItemClickListener) {

            super(itemView);
            DataBaseHelper dataBaseHelper = new DataBaseHelper(itemView.getContext());

            //region get list data
            Cursor cursor = dataBaseHelper.ShowWorkerData();
            if(cursor.getCount()!=0){
                while(cursor.moveToNext()){

                    travailleurs.add(new Travailleur(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5)));

                }

            }


            //endregion

            trav_adapter = new Trav_Adapter(itemView.getContext(),travailleurs,recyclerItemClickListener).getAdapter();
            name = itemView.findViewById(R.id.Nametv);
            phone = itemView.findViewById(R.id.Phonetv);
            layout = itemView.findViewById(R.id.extra);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            info = itemView.findViewById(R.id.infos);
            image = itemView.findViewById(R.id.profile_image);


            itemView.setOnClickListener(new View.OnClickListener() {
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
                    trav_adapter.notifyItemRemoved(getAdapterPosition());

                    return true;
                }
            });
        }

    }
}
