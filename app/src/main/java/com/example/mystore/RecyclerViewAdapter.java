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
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.myViewHolder> {
    private Context mContext;
    private List<Item> itemsList;
    public onItemClickListener listener;

    public RecyclerViewAdapter(Context mContext, List<Item> itemsList){
        this.mContext=mContext;
        this.itemsList=itemsList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item,parent,false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Item currentItem = itemsList.get(position);
        holder.item_name.setText(currentItem.getTitle());
        holder.item_price.setText(currentItem.getPrice().toString());
        Picasso.get().
                load(Uri.parse(currentItem.getPictures().get(0))).
                fit().
                centerCrop().
                into(holder.item_image);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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
    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
