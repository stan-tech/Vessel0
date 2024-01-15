package com.example.besoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.R;
import com.example.besoin.RecyclerItemClickListener;

import java.util.ArrayList;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.Doc_view_holder> {
    ArrayList<Document> documents;
    Context context;
    RecyclerItemClickListener recyclerItemClickListener;
    public DocumentsAdapter(ArrayList<Document> documents, Context context,RecyclerItemClickListener recyclerItemClickListener) {
        this.documents = documents;
        this.context = context;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @NonNull
    @Override
    public DocumentsAdapter.Doc_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.document_card,parent,false);
        return new DocumentsAdapter.Doc_view_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsAdapter.Doc_view_holder holder, int position) {
        Document doc = documents.get(position);

        if(!isNull(doc.getPath())){
            String file_name = SetDocumentName(doc.getPath());
            holder.path.setText(file_name);
        }

    }
    public String SetDocumentName(String path){

        StringBuilder builder = new StringBuilder();
        char[] pathArray = path.toCharArray();
        for(int i=path.length()-1;i>=0;i--){
            if(pathArray[i] != '/'){
                builder.append(pathArray[i]);
            }else{
                break;
            }
        }

        builder.reverse();
        return builder.toString();

    }
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    @Override
    public int getItemCount() {
        return documents.size();
    }


    public class Doc_view_holder extends RecyclerView.ViewHolder {
        TextView path;

        public Doc_view_holder(@NonNull View itemView) {
            super(itemView);

            path = itemView.findViewById(R.id.doc_path);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerItemClickListener !=null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerItemClickListener.onItemClick(pos);
                        }
                    }
                    }

        });
    }
}
}
