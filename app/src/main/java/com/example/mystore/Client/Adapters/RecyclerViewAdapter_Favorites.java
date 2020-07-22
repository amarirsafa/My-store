package com.example.mystore.Client.Adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Classes.Item;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter_Favorites extends FirestoreRecyclerAdapter<Item,RecyclerViewAdapter_Favorites.myViewHolder> {
    public RecyclerViewAdapter_Favorites.OnItemClickListener listener;

    public RecyclerViewAdapter_Favorites(@NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_Favorites.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_favorites,parent,false);
        return new RecyclerViewAdapter_Favorites.myViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull RecyclerViewAdapter_Favorites.myViewHolder holder, int position, @NonNull Item model) {
        holder.item_name.setText(model.getTitle());
        holder.item_price.setText(model.getPrice().toString());
        Picasso.get().
                load(Uri.parse(model.getPictures().get(0))).
                fit().
                centerCrop().
                into(holder.item_image);

    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView item_name,item_price;
        ImageView item_image,delete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.Item_Name);
            item_price = itemView.findViewById(R.id.Item_Price);
            item_image = itemView.findViewById(R.id.Item_image);
            delete = itemView.findViewById(R.id.delete_from_favorites);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(getSnapshots().getSnapshot(position),position);
                        }
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(RecyclerViewAdapter_Favorites.OnItemClickListener listener){
        this.listener=listener;
    }
}
