package com.example.mystore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends FirestoreRecyclerAdapter<Item,RecyclerViewAdapter.myViewHolder> {

    public RecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new myViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Item model) {
        holder.item_name.setText(model.getTitle());
        holder.item_price.setText(model.getPrice().toString());
        Picasso.get().
                load(Uri.parse(model.getPictures().get(0))).
                fit().
                centerCrop().
                into(holder.item_image);

    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView item_name,item_price;
        ImageView item_image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.Item_Name);
            item_price = itemView.findViewById(R.id.Item_Price);
            item_image = itemView.findViewById(R.id.Item_image);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(position!= RecyclerView.NO_POSITION && listener != null){
//                        listener.onItemClick(get);
//                    }
//                }
//            });
        }
    }
}
