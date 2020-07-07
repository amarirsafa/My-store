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

import com.example.mystore.Client.Classes.Item;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter_Cart extends FirestoreRecyclerAdapter<Item, RecyclerViewAdapter_Cart.myViewHolder> {

    public OnItemClickListener listener;

    public RecyclerViewAdapter_Cart(@NonNull FirestoreRecyclerOptions<Item> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_cart,parent,false);
        return new myViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Item model) {
        holder.Item_Title.setText(model.getTitle());
        holder.Item_amount.setText(model.getAmount()+"");
        Picasso.get().
                load(Uri.parse(model.getPictures().get(0))).
                fit().
                centerCrop().
                into(holder.Item_Image);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView Item_Image;
        TextView Item_Title,Item_amount,remove,save;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Item_Image = itemView.findViewById(R.id.Item_image_cart);
            Item_Title = itemView.findViewById(R.id.Item_title_cart);
            Item_amount = itemView.findViewById(R.id.Item_amount_cart);
            remove = itemView.findViewById(R.id.remove);
            save = itemView.findViewById(R.id.save);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
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
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSaveClick(getSnapshots().getSnapshot(position),position);
                        }
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
        void onDeleteClick(DocumentSnapshot documentSnapshot,int position);
        void onSaveClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
